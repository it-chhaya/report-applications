package co.istad.reporting.domain.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, unique = true, length = 256)
    private String email;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(nullable = false, columnDefinition = "Text")
    private String familyName;

    @Column(nullable = false, columnDefinition = "Text")
    private String givenName;

    @Column(unique = true)
    private String phoneNumber;

    private String gender;

    private LocalDate dob;

    @Column(length = 256)
    private String profileImage;

    @Column(length = 256)
    private String ipAddress;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonExpired;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonLocked;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean credentialsNonExpired;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEnabled;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean emailVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_security_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "security_group_id"))
    private Set<SecurityGroup> securityGroups;

}
