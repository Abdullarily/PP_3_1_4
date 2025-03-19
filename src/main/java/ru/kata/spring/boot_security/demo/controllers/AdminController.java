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

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    @GetMapping()
    public String allUsers(Model model, Principal principal) {
//        model.addAttribute("all_users", userService.allUsers());
//        User user = userService.getUserByEmail(principal.getName());
//        model.addAttribute("user", user);
//        model.addAttribute("newUser", new User());
//        model.addAttribute("rolesName", roleService.allRoles());
        return "all_users";
    }

    @GetMapping("add")
    public String addUser(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("rolesName", roleService.allRoles());
        return "add_user";
    }

    @PostMapping("add")
    public String addDbUser(@ModelAttribute("newUser") User user,
                            @RequestParam(value = "rolesName", required = false) Set<String> rolesName) {
        if (rolesName != null) {
            Set<Role> roles = roleService.getRole(new HashSet<>(rolesName));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>()); // Если роли не выбраны, назначаем пустой список
        }
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
        if (rolesName != null) {
            Set<Role> roles = roleService.getRole(new HashSet<>(rolesName));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>()); // Если роли не выбраны, назначаем пустой список
        }
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
        model.addAttribute("showUser", user);
        return "show_user";
    }

    @GetMapping("static/js")
    public String jsPage() {
        return "index";
    }
}
