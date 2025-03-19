package ru.kata.spring.boot_security.demo.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class RoleInitializer implements CommandLineRunner {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) {
        if (em.createQuery("SELECT r FROM Role r WHERE r.name = 'ROLE_ADMIN'", Role.class)
                .getResultList().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");

            em.persist(userRole);
            em.persist(adminRole);
        }
    }
}
