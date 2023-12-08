package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.Wait;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestUseInfoResponse {
    private String start;
    private String end;
    private LocalDateTime createdAt;

    public GuestUseInfoResponse(Wait wait) {
        this.start = wait.getCarpoolNum().getStart();
        this.end = wait.getCarpoolNum().getEnd();
        this.createdAt= wait.getCreatedAt();
    }
}
