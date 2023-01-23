package ru.microhost.hoster.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.microhost.hoster.services.RegisterService;
import ru.microhost.hoster.users.User;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final RegisterService registerService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        if (registerService.addUser(user)){
            return "redirect:/login";
        } else {
            return "registration";
        }
    }
}
