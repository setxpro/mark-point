package br.com.bytestorm.mark_point.entity.dto;

public record UserDTO(
        String name,
        String email,
        String phone,
        String googleId,
        String pushNotificationId
) {
}
