package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.*;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.Token;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.TokenRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.security.JwtProvider;
import yiu.aisl.carpool.domain.User;
import yiu.aisl.carpool.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final TokenRepository tokenRepository;
  private final HttpServletRequest httpServletRequest;
  private final WaitRepository waitRepository;
  private final CarpoolRepository carpoolRepository;


  public SignResponse login(SignRequest request) throws Exception {
    if (request.getEmail().isEmpty() || request.getPwd().isEmpty()) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    String modifiedEmail = request.getEmail() + "@yiu.ac.kr";

    User user = userRepository.findByEmail(modifiedEmail).orElseThrow(() ->
        new CustomException(ErrorCode.NOT_EXIST));

    if (!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
      throw new CustomException(ErrorCode.VALID_NOT_PWD);
    }

    user.setRefreshToken(createRefreshToken(user));

    return SignResponse.builder()
        .email(user.getEmail())
        .name(user.getName())
        .phone(user.getPhone())
        .home(user.getHome())
        .carNum(user.getCarNum())
        .token(TokenDto.builder()
            .access_token(jwtProvider.createToken(user.getEmail()))
            .refresh_token(user.getRefreshToken())
            .build())
        .build();
  }

  public boolean join(SignRequest request) throws Exception {
    // 데이터 없음
    if (request.getEmail().isEmpty() || request.getPwd().isEmpty() || request.getDistrict()
        .isEmpty() || request.getCity().isEmpty() || request.getName().isEmpty()) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    // 이미 존재하는 학번
    if (userRepository.findByEmail(request.getEmail() + "@yiu.ac.kr").isPresent()) {
      throw new CustomException(ErrorCode.DUPLICATE);
    }

    try {

      if (request.getEmail().length() != 9) {
        throw new IllegalArgumentException("이메일은 9자리 숫자로 입력되어야 합니다.");
      }
      String modifiedEmail = request.getEmail() + "@yiu.ac.kr";
      User user = User.builder()
          .email(modifiedEmail)
          .name(request.getName())
          .phone(request.getPhone())
          .home(request.getCity() + " " + request.getDistrict())
          .pwd(passwordEncoder.encode(request.getPwd()))
          .carNum(request.getCarNum())
          .build();
      if (request.getCarNum() != null) {
        user.setStatus(1);
      }
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }

  public boolean changePwd(CustomUserDetails customUserDetails, PwdRequest request) throws Exception {
    User user = customUserDetails.getUser();
    if (request.getPwd().isEmpty() || request.getNewPwd().isEmpty()) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    if (passwordEncoder.matches(request.getPwd(), customUserDetails.getPassword())) {
      user.setPwd(passwordEncoder.encode(request.getNewPwd()));
    } else {
      throw new CustomException(ErrorCode.VALID_NOT_PWD);
    }
    userRepository.save(user);
    return true;
  }

  public void delete(CustomUserDetails userDetails, SignRequest request) {
    // 사용자 정보가 이미 토큰에서 인증되었으므로 다시 데이터베이스에서 찾아올 필요 없음
    User user = userDetails.getUser();

    if (request.getPwd() == null) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    String enteredPassword = request.getPwd();

    if (passwordEncoder.matches(enteredPassword, user.getPwd())) {
      userRepository.delete(user);
    } else {
      throw new CustomException(ErrorCode.VALID_NOT_PWD);
    }
  }


  public SignResponse getUser(String email) throws Exception {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
    return new SignResponse(user);
  }

  public String createRefreshToken(User user) {
    Token token = tokenRepository.save(
        Token.builder()
            .email(user.getEmail())
            .refresh_token(UUID.randomUUID().toString())
            .expiration(300)//5분
            .build()
    );
    return token.getRefresh_token();
  }

  public Token validRefreshToken(User user, String refreshToken) throws Exception {
    Token token = tokenRepository.findById(user.getEmail())
        .orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
    // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
    if (token.getRefresh_token() == null) {
      return null;
    } else {
      // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
      if (token.getExpiration() < 10) {
        token.setExpiration(1000);
        tokenRepository.save(token);
      }

      // 토큰이 같은지 비교
      if (!token.getRefresh_token().equals(refreshToken)) {
        return null;
      } else {
        return token;
      }
    }
  }

  public TokenDto refreshAccessToken(TokenDto token) throws Exception {
    String email = jwtProvider.getEmail(token.getAccess_token());
    User user = userRepository.findByEmail(email).orElseThrow(() ->
        new BadCredentialsException("잘못된 계정정보입니다."));
    Token refreshToken = validRefreshToken(user, token.getRefresh_token());

    if (refreshToken != null) {
      return TokenDto.builder()
          .access_token(jwtProvider.createToken(email))
          .refresh_token(refreshToken.getRefresh_token())
          .build();
    } else {
      throw new IllegalArgumentException("로그인을 해주세요");
    }
  }

  public Object getProfile(CustomUserDetails userDetails) {
    Optional<User> user = userRepository.findByEmail(userDetails.getUser().getEmail());
    return MyprofileDto.builder()
        .name(user.get().getName())
        .phone(user.get().getPhone())
        .home(user.get().getHome())
        .carNum(user.get().getCarNum())
        .build();
  }

  public Object getUserStatus(CustomUserDetails userDetails) {
    Optional<User> user = userRepository.findByEmail(userDetails.getUser().getEmail());
    return UserStateDto.builder()
        .state(user.get().getStatus())
        .build();
  }

  public String profileUpdate(CustomUserDetails userDetails, MyprofileDto myprofileDto) {
    // 사용자 정보가 이미 토큰에서 인증되었으므로 다시 데이터베이스에서 찾아올 필요 없음
    User user = userDetails.getUser();

    if (myprofileDto.getName().isEmpty() || myprofileDto.getPhone().isEmpty()
        || myprofileDto.getHome().isEmpty()) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    user.setName(myprofileDto.getName());
    user.setPhone(myprofileDto.getPhone());
    user.setHome(myprofileDto.getHome());
    user.setCarNum(myprofileDto.getCarNum());

    if (myprofileDto.getCarNum() != null) {
      user.setStatus(1);
    }

    userRepository.save(user);
    return "success";
  }

  public void ownerMode(CustomUserDetails customUserDetails) {
    Optional<User> userOptional = userRepository.findByEmail(
        customUserDetails.getUser().getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (user.getCarNum() == null) {
        throw new CustomException(ErrorCode.NOT_EXIST);
      }
      user.setStatus(1);
      userRepository.save(user);
    } else {
      throw new IllegalArgumentException("User not found");
    }
  }

  public void guestMode(CustomUserDetails customUserDetails) {
    Optional<User> userOptional = userRepository.findByEmail(
        customUserDetails.getUser().getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setStatus(0);
      userRepository.save(user);
    } else {
      throw new IllegalArgumentException("User not found");
    }
  }

  public List<GuestUseInfoResponse> getGuestList(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    String email = userDetails.getUsername();
    List<Wait> waits = waitRepository.findByCheckNumAndGuestWithCarpool(email);
    return waits.stream()
        .map(GuestUseInfoResponse::new)
        .collect(Collectors.toList());
  }

  public List<OwnerUseInfoResponse> getOwnerList(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    String email = userDetails.getUsername();
    List<Carpool> carpools = carpoolRepository.findByCheckNumAndOwnerWithCarpool(email);
    return carpools.stream()
        .map(OwnerUseInfoResponse::new)
        .collect(Collectors.toList());
  }
}