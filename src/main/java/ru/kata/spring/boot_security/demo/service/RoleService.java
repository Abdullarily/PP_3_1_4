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

        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.name IN (:rolesName)",
                Role.class
        );
        query.setParameter("rolesName", rolesName);

        return new HashSet<>(query.getResultList());
    }

    public Set<Role> getRole(String name) {
        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.name IN (:name)",
                Role.class
        );
        query.setParameter("name", name);

        return new HashSet<>(query.getResultList());
    }

    public Role getRoleByName(String name) {
        return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public Role getRoleById(Integer id) {
        return em.find(Role.class, id);
    }
}
