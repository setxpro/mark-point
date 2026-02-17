package br.com.bytestorm.mark_point.entity.mapper;

import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.entity.dto.ListUsersDTO;
import br.com.bytestorm.mark_point.entity.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntityUser(UserDTO userDTO) {
        return new User(
                userDTO.name(),
                userDTO.email(),
                userDTO.phone(),
                userDTO.googleId(),
                userDTO.pushNotificationId()
        );
    }

    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getGoogleId(),
                user.getPushNotificationId()
        );
    }

    public ListUsersDTO toListDto(User user) {
        return new ListUsersDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getGoogleId(),
                user.getPushNotificationId()
        );
    }
}
