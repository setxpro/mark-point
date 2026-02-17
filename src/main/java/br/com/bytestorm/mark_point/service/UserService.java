package br.com.bytestorm.mark_point.service;

import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.entity.dto.ListUsersDTO;
import br.com.bytestorm.mark_point.entity.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(UserDTO userDTO);
    void update(UUID oid, UserDTO userDTO);
    List<ListUsersDTO> list();
    ListUsersDTO findOne(UUID uid);
    void delete(UUID uid);
}
