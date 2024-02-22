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

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getNewPwd() {
        return this.newPwd;
    }
}
