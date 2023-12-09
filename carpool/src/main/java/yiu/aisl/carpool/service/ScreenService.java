package yiu.aisl.carpool.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolResponse;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.User;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.repository.CarpoolRepository;

import java.util.List;
import java.util.stream.Collectors;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreenService {
    private final CarpoolRepository carpoolRepository;
    private final WaitRepository waitRepository;
    public List<CarpoolResponse> getCarpool() {
        return carpoolRepository.findAll().stream()
                .map(CarpoolResponse::new)
                .collect(Collectors.toList());
    }

    public List<CarpoolResponse> getMyCarpool(CustomUserDetails customUserDetails) {
        String userEmail = customUserDetails.getUser().getEmail();
        // 현재 사용자의 이메일을 이용하여 카풀을 찾음
        List<Carpool> myCarpools = carpoolRepository.findByEmail(userEmail);
        // 찾은 카풀들을 CarpoolResponse로 매핑하여 리스트로 반환
        return myCarpools.stream()
                .map(CarpoolResponse::new)
                .collect(Collectors.toList());
    }

    public List<Wait> getWaitList(Integer carpoolNum, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
        String email = userDetails.getUser().getEmail();
        Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email);

        if (carpoolOptional.isPresent()) {
            Carpool carpool = carpoolOptional.get();
            // 자신의 게시물이 맞으면 해당 게시물에 대한 모든 wait 엔티티 가져오기
            List<Wait> waits = waitRepository.findByCarpoolNum(carpool);

            // 자신의 게시물에 신청한 사람들 중 checkNum이 0인 사람들만 필터링
            return waits.stream()
                .filter(wait -> wait.getCheckNum() == 0)
                .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("찾을 수 없거나 권한이 없습니다.");
        }
    }

}