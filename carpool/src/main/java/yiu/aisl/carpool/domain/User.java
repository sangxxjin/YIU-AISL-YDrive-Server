package yiu.aisl.carpool.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

  @Id
  private String email;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String phone;
  @Column(nullable = false)
  private String home;
  @Column(nullable = false)
  private String pwd;
  @Column
  private String carNum;
  @Column
  private int status;

  private String refreshToken;
  private String accessToken;
}
