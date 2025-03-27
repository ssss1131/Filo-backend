package kz.ssss.filo.controller;

import kz.ssss.filo.dto.response.UserInfoResponse;
import kz.ssss.filo.model.UserPrincipal;
import kz.ssss.filo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kz.ssss.filo.util.Constant.HOME_URL;


@RestController
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;

    @GetMapping(HOME_URL)
    public String showHomePage(@RequestParam(name = "path", defaultValue = "", required = false) String path,
                               @AuthenticationPrincipal UserPrincipal userDetails,
                               Model model) {
        model.addAttribute("currentPath", path);
        model.addAttribute("files", fileService.getObjectInfoInFolder(userDetails.getId(), path));
        return "main_page";
    }

    @GetMapping("/api/user/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserInfoResponse userInfo = new UserInfoResponse(user.getUsername());
        return ResponseEntity.ok(userInfo);

    }


}
