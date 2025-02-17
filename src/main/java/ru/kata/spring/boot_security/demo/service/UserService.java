package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.merge(user);
    }

    public List<User> allUsers() {
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        if (users.isEmpty()) {
            Role role = new Role("ROLE_ADMIN");
            Role role2 = new Role("ROLE_USER");
            em.merge(role);
            em.merge(role2);
        }
        return users;
    }

    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    public void editUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.merge(user);
    }

    public User getUserByName(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public void deleteUser(int id) {
        Query query = em.createNativeQuery("delete from users_roles where users_id = " + id);
        query.executeUpdate();
        User user = em.find(User.class, id);
        em.remove(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByName(username);
    }
}
