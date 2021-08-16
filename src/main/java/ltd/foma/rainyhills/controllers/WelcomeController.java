package ltd.foma.rainyhills.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redirect
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String redirect(){
        return "redirect:/rainyhills";
    }
}
