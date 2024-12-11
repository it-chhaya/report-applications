package co.istad.reporting.core.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/staffs")
public class StaffController {

    @GetMapping
    Map<String, String> routeSecured() {
        return Map.of("message", "Staff Route");
    }

}
