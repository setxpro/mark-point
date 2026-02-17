package br.com.bytestorm.mark_point.repository;

import br.com.bytestorm.mark_point.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);
}
