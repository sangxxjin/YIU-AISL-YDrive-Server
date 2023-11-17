package yiu.aisl.carpool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import yiu.aisl.carpool.repository.UserRepository;

@Controller
public class HomeController {
    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) { this.userRepository = userRepository; }

    @GetMapping("/")
    public String main() {
        return "main";
    }
}
