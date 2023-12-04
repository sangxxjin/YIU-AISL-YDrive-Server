package yiu.aisl.carpool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.ScreenService;

import java.nio.charset.Charset;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final ScreenService screenService;

    @Autowired
    public HomeController(UserRepository userRepository, ScreenService screenService) {
        this.userRepository = userRepository;
        this.screenService = screenService;
    }

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/main")
    public ResponseEntity<Object> main(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(screenService.getCarpool(), headers, HttpStatus.OK);
    }
}
