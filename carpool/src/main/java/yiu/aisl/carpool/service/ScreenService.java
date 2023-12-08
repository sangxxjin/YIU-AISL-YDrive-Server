package yiu.aisl.carpool.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolDto;
import yiu.aisl.carpool.Dto.CarpoolResponse;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.repository.CarpoolRepository;

import java.util.List;
import java.util.stream.Collectors;
import yiu.aisl.carpool.security.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreenService {
    private final CarpoolRepository carpoolRepository;
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

}