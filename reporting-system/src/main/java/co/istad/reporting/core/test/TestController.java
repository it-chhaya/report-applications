package co.istad.reporting.core.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/anonymous")
    Map<String, String> routeAnonymous() {
        return Map.of("message", "Anonymous Route");
    }

    @GetMapping("/secured")
    Map<String, String> routeSecured() {
        return Map.of("message", "Secured Route");
    }

}
