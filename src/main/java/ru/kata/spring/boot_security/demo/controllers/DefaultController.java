package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
public class DefaultController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String newAdmin() {
        userService.newAdmin();
        return "index";
    }
}
