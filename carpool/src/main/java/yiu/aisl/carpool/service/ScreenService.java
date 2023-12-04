package yiu.aisl.carpool.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolDto;
import yiu.aisl.carpool.repository.CarpoolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreenService {
    private final CarpoolRepository carpoolRepository;
    public List<CarpoolDto> getCarpool() {
        return carpoolRepository.findAll().stream()
                .map(CarpoolDto::new)
                .collect(Collectors.toList());
    }
}
