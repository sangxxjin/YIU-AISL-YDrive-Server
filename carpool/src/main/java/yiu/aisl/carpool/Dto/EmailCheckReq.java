package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailCheckReq {
    private String email;
    private String authNum;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthNum() {
        return authNum;
    }
}
