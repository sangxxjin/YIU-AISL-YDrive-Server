package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PwdResponse {
    private String pwd;

    public PwdResponse(User user) {
        this.pwd = user.getPwd();
    }
}
