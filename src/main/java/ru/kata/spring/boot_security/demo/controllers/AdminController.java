package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    @GetMapping()
    public String allUsers(Model model) {
        model.addAttribute("all_users", userService.allUsers());
        return "all_users";
    }

    @GetMapping("add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        return "add_user";
    }

    @PostMapping()
    public String addDbUser(@ModelAttribute("user") User user,
                            @RequestParam(value = "rolesName", required = false) Set<String> rolesName) {
        Set<Role> roles = roleService.getRole(rolesName);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("edit")
    public String editUser(Model model, @RequestParam("id") int id) {
        model.addAttribute("user1", userService. getUserById(id));
        model.addAttribute("allRoles", roleService.allRoles());
        return "edit_user";
    }

    @PostMapping("edit")
    public String editDbUser(@ModelAttribute("user1") User user, @RequestParam(value = "rolesName", required = false) Set<String> rolesName) {
        Set<Role> roles = roleService.getRole(rolesName);
        user.setRoles(roles);
        user.setRoles(roleService.getRole(rolesName));
        userService.editUser(user);
        return "redirect:/admin";
    }

    @PostMapping("delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("show")
    public String showUser(Model model, @RequestParam("id") int id, User user) {
        user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "show_user";
    }
}
