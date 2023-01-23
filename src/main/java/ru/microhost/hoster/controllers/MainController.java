package ru.microhost.hoster.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    @GetMapping(path ="/main")
    public String hello(Model model){
        model.addAttribute("username", getCurrentUsername());
        return "main";
    }

    @GetMapping( "/")
    public String defaultPage(Model model){
        model.addAttribute("username", getCurrentUsername());
        return "main";
    }

/*    @PostMapping("/main")
    public String addFile(
            @RequestParam("file")MultipartFile file
            ) {

    }*/

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
