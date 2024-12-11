package co.istad.reporting.security;

import co.istad.reporting.domain.primary.Authority;
import co.istad.reporting.domain.primary.SecurityGroup;
import co.istad.reporting.domain.primary.User;
import co.istad.reporting.core.authority.AuthorityRepository;
import co.istad.reporting.core.securitygroup.SecurityGroupRepository;
import co.istad.reporting.core.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final AuthorityRepository authorityRepository;
    private final SecurityGroupRepository securityGroupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //@PostConstruct
    void init() {

        // User Management
        Authority userRead = new Authority();
        userRead.setName("user:read");

        Authority userWrite = new Authority();
        userWrite.setName("user:write");

        // Report Management
        Authority reportRead = new Authority();
        reportRead.setName("report:read");

        Authority reportWrite = new Authority();
        reportWrite.setName("report:write");

        authorityRepository.saveAll(
                List.of(userRead, userWrite, reportRead, reportWrite)
        );

        SecurityGroup administrator = new SecurityGroup();
        administrator.setName("administrator");
        administrator.setAuthorities(
                List.of(userRead, userWrite, reportRead, reportWrite)
        );

        SecurityGroup staff = new SecurityGroup();
        staff.setName("staff");
        staff.setAuthorities(
                List.of(userRead, reportRead)
        );

        securityGroupRepository.saveAll(
                List.of(administrator, staff)
        );

        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("qwerqwer"));
        user.setGender("Male");
        user.setDob(LocalDate.of(2000, 1, 1));
        user.setFamilyName("John");
        user.setGivenName("Cena");
        user.setPhoneNumber("098459947");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setIsEnabled(true);
        user.setEmailVerified(true);
        user.setSecurityGroups(
                Set.of(administrator)
        );

        User hello = new User();
        hello.setUsername("staff");
        hello.setEmail("staff@gmail.com");
        hello.setPassword(passwordEncoder.encode("qwerqwer"));
        hello.setGender("Male");
        hello.setDob(LocalDate.of(2000, 1, 1));
        hello.setFamilyName("John");
        hello.setGivenName("Hello");
        hello.setPhoneNumber("077459947");
        hello.setAccountNonExpired(true);
        hello.setAccountNonLocked(true);
        hello.setCredentialsNonExpired(true);
        hello.setIsEnabled(true);
        hello.setEmailVerified(true);
        hello.setSecurityGroups(
                Set.of(staff)
        );

        userRepository.save(user);
        userRepository.save(hello);
    }

}
