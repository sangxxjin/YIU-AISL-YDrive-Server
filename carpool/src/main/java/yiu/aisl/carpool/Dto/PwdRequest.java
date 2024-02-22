package yiu.aisl.carpool.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwdRequest {
    private String pwd;
    private String newPwd;

    public String getPwd() {
        return this.pwd;
    }

    public String getNewPwd() {
        return this.newPwd;
    }
}
