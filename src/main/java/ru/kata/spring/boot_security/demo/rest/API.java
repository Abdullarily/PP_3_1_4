package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class API {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsersDTO(userService.allUsers()));
    }


    /**
     *  {
     *     "age": 30,
     *     "email": "test12@mail.com",
     *     "first_name": "Test12",
     *     "last_name": "Test12",
     *     "password": "test12",
     *     "roles": [
     *         {"id": 1, "name": "ROLE_USER"},
     *         {"id": 2, "name": "ROLE_ADMIN"}
     *     ]
     * }
     */

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        System.out.println("Полученные данные: " + userDTO.getRoles().toString());
        User user = userService.convertDTO(userDTO);
        System.out.println("После конвертации: " + user.getRoles().toString());
        System.out.println(user.getRoles().toString());
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        User updateUser = userService.getUserById(id);
        User user = userService.convertDTO(userDTO);
        userService.apiEditUser(user, updateUser);
        userService.editUser(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getAuthenticatedUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(new UserDTO(user));
    }
}
