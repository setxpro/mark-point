package br.com.bytestorm.mark_point.repository;

import br.com.bytestorm.mark_point.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);

    @Query(value = """
    select exists(
      select 1
      from tb_users
      where email = :email
        and id <> :id
    )
    """, nativeQuery = true)
    boolean existsByEmailAndIdNot(@Param("email") String email,
                                  @Param("id") UUID id);

    @Query(value = "SELECT COUNT(*) FROM tb_users WHERE phone = :phone", nativeQuery = true)
    Long userPhoneExists(@Param("phone") String phone);

    @Query(value = """
    select exists(
      select 1
      from tb_users
      where phone = :phone
        and id <> :id
    )
    """, nativeQuery = true)
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("id") UUID id);
}
