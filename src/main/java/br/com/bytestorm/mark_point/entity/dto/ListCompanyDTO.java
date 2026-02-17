package br.com.bytestorm.mark_point.entity.dto;

import java.util.UUID;

public record ListCompanyDTO(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String imgUrl,
        String icon,
        UUID owner
) {
}
