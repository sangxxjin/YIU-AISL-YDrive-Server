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

    public String getEmail() {
        return email;
    }

    public String getAuthNum() {
        return authNum;
    }
}
