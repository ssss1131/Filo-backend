package kz.ssss.filo.controller;

import kz.ssss.filo.model.UserPrincipal;
import kz.ssss.filo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static kz.ssss.filo.util.Constant.HOME_URL;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;

    @GetMapping(HOME_URL)
    public String showHomePage(@RequestParam(name = "path", defaultValue = "", required = false) String path,
                               @AuthenticationPrincipal UserPrincipal userDetails,
                               Model model){
        model.addAttribute("currentPath", path);
        model.addAttribute("files", fileService.getAllObjects(userDetails.getId(), path));
        return "main_page";
    }


}
