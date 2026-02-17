package br.com.bytestorm.mark_point.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO {
    private String message;
    private int code;
}
