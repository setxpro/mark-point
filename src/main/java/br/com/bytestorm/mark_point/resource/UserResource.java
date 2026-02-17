package br.com.bytestorm.mark_point.resource;

import br.com.bytestorm.mark_point.entity.dto.ListUsersDTO;
import br.com.bytestorm.mark_point.entity.dto.UserDTO;
import br.com.bytestorm.mark_point.service.UserService;
import br.com.bytestorm.mark_point.utils.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Const.USER)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) {
        this.userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping
    public ResponseEntity<List<ListUsersDTO>> list() {
        return ResponseEntity.ok(this.userService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListUsersDTO> findOne(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.userService.findOne(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok().body("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().body("OK");
    }

}
