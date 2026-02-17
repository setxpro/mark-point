package br.com.bytestorm.mark_point.entity;

import br.com.bytestorm.mark_point.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
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
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String googleId;
    private String pushNotificationId;
    private Boolean isActive;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == RoleEnum.ADMIN || this.role == RoleEnum.SUPPORT || this.role == RoleEnum.DEVELOPER) return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_COMPANY"),
                new SimpleGrantedAuthority("ROLE_BASIC")
        );
        if (this.role == RoleEnum.COMPANY) return List.of(
                new SimpleGrantedAuthority("ROLE_COMPANY"),
                new SimpleGrantedAuthority("ROLE_BASIC")
        );
        if (this.role == RoleEnum.CUSTOMER) return List.of(
                new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                new SimpleGrantedAuthority("ROLE_BASIC")
        );
        else return List.of(new SimpleGrantedAuthority("ROLE_BASIC"));
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
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
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
