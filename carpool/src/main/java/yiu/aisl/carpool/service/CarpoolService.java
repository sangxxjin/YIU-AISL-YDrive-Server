package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.security.JwtProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class CarpoolService {
    private final CarpoolRepository carpoolRepository;
    private final JwtProvider jwtProvider;
    private final HttpServletRequest httpServletRequest;

    public boolean create(CarpoolRequest request) throws Exception {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String email = jwtProvider.getEmail(token);
                Carpool carpool = Carpool.builder()
                        .carpoolNum(request.getCarpoolNum())
                        .start(request.getStart())
                        .end(request.getEnd())
                        .date(request.getDate())
                        .checkNum(request.getCheckNum())
                        .memberNum(request.getMemberNum())
                        .email(email)
                        .createdAt(date)
                        .build();
                carpoolRepository.save(carpool);
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw  new Exception("잘못된 요청입니다.");
        }
        return true;
    }
}
