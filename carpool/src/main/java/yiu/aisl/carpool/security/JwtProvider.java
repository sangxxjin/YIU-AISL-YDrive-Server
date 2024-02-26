package yiu.aisl.carpool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yiu.aisl.carpool.service.JpaUserDetailsService;

@RequiredArgsConstructor
@Component
public class JwtProvider {

  @Value("${jwt.secret.key}")
  private String salt;

  private Key secretKey;

  private long accessTokenValidTime = Duration.ofMinutes(30).toMillis(); // 만료시간 : 30분
  private long refreshTokenValidTime = Duration.ofDays(180).toMillis(); // 만료시간 : 180일

  private final JpaUserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
  }

  // 토큰 생성
  public String createToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + accessTokenValidTime))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // 권한정보 획득
  // Spring Security 인증과정에서 권한확인을 위한 기능
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 토큰에 담겨있는 유저 account 획득
  public String getEmail(String token) {
    // 만료된 토큰에 대해 parseClaimsJws를 수행하면 io.jsonwebtoken.ExpiredJwtException이 발생한다.
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    } catch (ExpiredJwtException e) {
      e.printStackTrace();
      return e.getClaims().getSubject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
  }

  // Authorization Header를 통해 인증을 한다.
  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      // Bearer 검증
      if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
        return false;
      } else {
        token = token.split(" ")[1].trim();
      }
      Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
          .parseClaimsJws(token);
      // 만료되었을 시 false
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  // JWT 토큰 유효성 검증 메서드
  public boolean validToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
              .parseClaimsJws(token);

      // 만료되었을 시 false
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      System.out.println("복호화 에러: " + e.getMessage());
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser() // 클레임 조회
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
  }

  // 토큰 정보 리턴
  public TokenInfo getTokenInfo(String token) {
    Claims body = getClaims(token);
    Set<String> keySet = body.keySet();
//        for (String s : keySet) {
//            System.out.println("s = " + s);
//        }

    Long studentId = body.get("studentId", Long.class);
    String nickname = body.get("nickname", String.class);
    Date issuedAt = body.getIssuedAt();
    Date expiration = body.getExpiration();
    return new TokenInfo(studentId, nickname, issuedAt, expiration);
  }

  @Getter
  public class TokenInfo {
    private Long studentId;
    private String nickname;
    private Date issuedAt;
    private Date expire;

    public TokenInfo(Long studentId, String nickname, Date issuedAt, Date expire) {
      this.studentId = studentId;
      this.nickname = nickname;
      this.issuedAt = issuedAt;
      this.expire = expire;
    }
  }
}
