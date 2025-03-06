package kz.ssss.filo.controller;

import jakarta.servlet.http.HttpSession;
import kz.ssss.filo.dto.request.SignUpDto;
import kz.ssss.filo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kz.ssss.filo.util.Constant.*;

@Controller
@RequestMapping(BASE_AUTH_URL)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping(LOGIN_ENDPOINT)
    public String showLoginPage(HttpSession session, Model model) {
        String attribute = (String) session.getAttribute("flash_error_message");
        if (attribute != null) {
            model.addAttribute("error_message", attribute);
            session.removeAttribute("flash_error_message");
        }

        return LOGIN_PAGE;
    }

    @GetMapping(REGISTER_ENDPOINT)
    public String showRegisterPage(@ModelAttribute(USER_ATTRIBUTE) SignUpDto user) {
        return REGISTER_PAGE;
    }

    @PostMapping(REGISTER_ENDPOINT)
    public String registration(@Validated @ModelAttribute(USER_ATTRIBUTE) SignUpDto user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REGISTER_PAGE;
        }
        userService.save(user.username(), user.password());

        return String.format("redirect:%s%s?success", BASE_AUTH_URL, LOGIN_ENDPOINT);
    }

}
