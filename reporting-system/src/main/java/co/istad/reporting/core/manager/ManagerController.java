package co.istad.reporting.core.manager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/managers")
public class ManagerController {

    @GetMapping
    Map<String, String> routeSecured() {
        return Map.of("message", "Manager Route");
    }

}
