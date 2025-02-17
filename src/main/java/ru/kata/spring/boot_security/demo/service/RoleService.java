package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class
RoleService {

    private EntityManager em;

    @Autowired
    public RoleService(EntityManager em) {
        this.em = em;
    }

    public List<Role> allRoles() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    public Set<Role> getRole(Set<String> rolesName) {

        if (rolesName.contains("ROLE_ADMIN")) {
            rolesName.add("ROLE_USER");
        }

        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.name IN (:rolesName)",
                Role.class
        );
        query.setParameter("rolesName", rolesName);

        return new HashSet<>(query.getResultList());
    }
}
