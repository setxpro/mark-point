package br.com.bytestorm.mark_point.entity;

import br.com.bytestorm.mark_point.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "E-mail inválido.")
    private String email;
    private String phone;

    @Column(unique = true)
    private String googleId;

    @Column(unique = true)
    private String pushNotificationId;
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Ticket> tickets = new ArrayList<>();


    public User(String name, String email, String phone, String googleId, String pushNotificationId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.googleId = googleId;
        this.pushNotificationId = pushNotificationId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case ADMIN, SUPPORT, DEVELOPER -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COMPANY"),
                    new SimpleGrantedAuthority("ROLE_BASIC")
            );
            case COMPANY -> List.of(
                    new SimpleGrantedAuthority("ROLE_COMPANY"),
                    new SimpleGrantedAuthority("ROLE_BASIC")
            );
            case CUSTOMER -> List.of(
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                    new SimpleGrantedAuthority("ROLE_BASIC")
            );
            default -> List.of(new SimpleGrantedAuthority("ROLE_BASIC"));
        };
    }

    @PrePersist
    public void prePersist() {
        if (role == null) role = RoleEnum.BASIC;
        if (isActive == null) isActive = true;
    }

    private boolean active() {
        return Boolean.TRUE.equals(isActive);
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active();
    }

    @Override
    public boolean isAccountNonLocked() {
        return active();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active();
    }

    @Override
    public boolean isEnabled() {
        return active();
    }
}
