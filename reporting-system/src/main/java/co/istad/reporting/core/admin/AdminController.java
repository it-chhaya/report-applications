package co.istad.reporting.core.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @GetMapping
    Map<String, String> routeSecured() {
        return Map.of("message", "Admin Route");
    }

}
