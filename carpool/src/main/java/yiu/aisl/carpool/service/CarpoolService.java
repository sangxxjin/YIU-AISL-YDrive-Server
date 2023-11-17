package yiu.aisl.carpool.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.repository.CarpoolRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CarpoolService {
    private final CarpoolRepository carpoolRepository;

    public boolean create(CarpoolRequest request) throws Exception {
        try {
            Carpool carpool = Carpool.builder()
                    .carpoolNum(request.getCarpoolNum())
                    .start(request.getStart())
                    .end(request.getEnd())
                    .date(request.getDate())
                    .checkNum(request.getCheckNum())
                    .memberNum(request.getMemberNum())
                    .email(request.getEmail())
                    .createdAt(request.getCreatedAt())
                    .build();
            carpoolRepository.save(carpool);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw  new Exception("잘못된 요청입니다.");
        }
        return true;
    }
}
