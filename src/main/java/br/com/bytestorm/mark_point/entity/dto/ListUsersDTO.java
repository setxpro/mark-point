package br.com.bytestorm.mark_point.entity.dto;

import java.util.UUID;

public record ListUsersDTO(
        UUID id,
        String name,
        String email,
        String phone,
        String googleId,
        String pushNotificationId
) {
}
