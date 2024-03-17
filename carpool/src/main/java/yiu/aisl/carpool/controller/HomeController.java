package yiu.aisl.carpool.controller;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.ScreenService;

@Controller
public class HomeController {

    private final ScreenService screenService;

    @Autowired
    public HomeController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/main")
    public ResponseEntity<Object> main(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(screenService.getCarpool(userDetails), headers, HttpStatus.OK);
    }

}
