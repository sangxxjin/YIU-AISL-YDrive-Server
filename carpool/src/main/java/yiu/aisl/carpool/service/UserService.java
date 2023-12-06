package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.MyprofileDto;
import yiu.aisl.carpool.Dto.PwdRequest;
import yiu.aisl.carpool.Dto.TokenDto;
import yiu.aisl.carpool.domain.Token;
import yiu.aisl.carpool.repository.TokenRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.security.JwtProvider;
import yiu.aisl.carpool.Dto.SignRequest;
import yiu.aisl.carpool.Dto.SignResponse;
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


  public SignResponse login(SignRequest request) throws Exception {

    String modifiedEmail = request.getEmail() + "@yiu.ac.kr";
    User user = userRepository.findByEmail(modifiedEmail).orElseThrow(() ->
        new BadCredentialsException("잘못된 계정정보입니다."));

    if (!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
      throw new BadCredentialsException("잘못된 계정정보입니다.");
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
    try {

      if (request.getEmail().length() != 9) {
        throw new Exception("이메일은 9자리 숫자로 입력되어야 합니다.");
      }
      String modifiedEmail = request.getEmail() + "@yiu.ac.kr";
      User user = User.builder()
          .email(modifiedEmail)
          .name(request.getName())
          .phone(request.getPhone())
          .home(request.getHome())
          .pwd(passwordEncoder.encode(request.getPwd()))
          .carNum(request.getCarNum())
          .build();
      if (request.getCarNum()!=null)user.setStatus(1);
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new Exception("잘못된 요청입니다.");
    }
    return true;
  }

  public boolean changePwd(PwdRequest request) throws Exception {
    try {
      String authHeader = httpServletRequest.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // 현재 사용자의 이메일이 맞는지 검사를 못하고 있음
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("사용자의 이메일이 아닙니다."));
        user.setPwd(passwordEncoder.encode(request.getPwd()));
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new Exception("잘못된 요청입니다.");
    }
    return true;
  }

  public void delete(CustomUserDetails userDetails, SignRequest request) {
    String email = userDetails.getUser().getEmail();

    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String enteredPassword = request.getPwd();

      // 사용자의 비밀번호를 가져와서 입력된 비밀번호와 비교
      if (passwordEncoder.matches(enteredPassword, user.getPwd())) {
        userRepository.delete(user);
      }
      else {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
      }
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
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
      throw new Exception("로그인을 해주세요");
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

  public String profileUpdate(CustomUserDetails userDetails, MyprofileDto myprofileDto) {
    Optional<User> userOptional = userRepository.findByEmail(userDetails.getUser().getEmail());
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    User user = userOptional.get();

    user.setName(myprofileDto.getName());
    user.setPhone(myprofileDto.getPhone());
    user.setHome(myprofileDto.getHome());
    user.setCarNum(myprofileDto.getCarNum());
    if (myprofileDto.getCarNum()!=null) user.setStatus(1);
    userRepository.save(user);
    return "success";
  }
  public void ownerMode(CustomUserDetails customUserDetails){
    Optional<User> userOptional = userRepository.findByEmail(customUserDetails.getUser().getEmail());
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    User user = userOptional.get();
    if (user.getCarNum()==null){
      throw new IllegalArgumentException("자동차가 없어 차주 모드로 변경이 불가능합니다.");
    }
    user.setStatus(1);
    userRepository.save(user);
  }
  public void guestMode(CustomUserDetails customUserDetails){
    Optional<User> userOptional = userRepository.findByEmail(customUserDetails.getUser().getEmail());
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    User user = userOptional.get();
    user.setStatus(0);
    userRepository.save(user);
  }
}