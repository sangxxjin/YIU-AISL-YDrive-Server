package yiu.aisl.carpool.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private String targetUserId;
    private String title;
    private String body;

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Builder
    public FCMNotificationRequestDto(String targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
    }
}
