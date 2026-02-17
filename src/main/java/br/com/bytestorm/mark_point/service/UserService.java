package br.com.bytestorm.mark_point.service;

import br.com.bytestorm.user_ms.entity.dto.CustomUserDTO;
import br.com.bytestorm.user_ms.entity.dto.MessageDTO;
import br.com.bytestorm.user_ms.entity.dto.UpdateUserDTO;
import br.com.bytestorm.user_ms.entity.dto.UserDTO;

import java.util.List;

public interface UserService {
    MessageDTO save(UserDTO req);
    List<CustomUserDTO> findAll();
    CustomUserDTO findOne(Long id);
    MessageDTO update(Long id, UpdateUserDTO req);
    MessageDTO delete(Long id);
}
