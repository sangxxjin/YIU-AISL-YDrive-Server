package yiu.aisl.carpool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.Dto.SignRequest;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.service.CarpoolService;

@RestController
@RequiredArgsConstructor
public class CarpoolController {
    private final CarpoolRepository carpoolRepository;
    private final CarpoolService carpoolService;
    @PostMapping("/carpool/create")
    public ResponseEntity<Boolean> carpoolCreate(@RequestBody CarpoolRequest request) throws Exception {
        return new ResponseEntity<>(carpoolService.create(request), HttpStatus.OK);
    }
}