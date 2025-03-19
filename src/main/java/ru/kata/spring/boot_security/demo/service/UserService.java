package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleService rs = new RoleService(em);

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public void save(User user) {
        Set<Role> userRoles = new HashSet<>(user.getRoles());

        Role userRole = em.createQuery("SELECT r FROM Role r WHERE r.name = 'ROLE_USER'", Role.class)
                .getSingleResult();

        Role adminRole = em.createQuery("SELECT r FROM Role r WHERE r.name = 'ROLE_ADMIN'", Role.class)
                .getSingleResult();

        // Если назначена роль ADMIN, автоматически добавляем USER
        if (userRoles.contains(adminRole)) {
            userRoles.add(userRole);
        }

        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
    }

    public void newAdmin() {
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        List<Role> rolesList = rs.allRoles();
        Set<Role> roles = new HashSet<>(rolesList);
        if (users.isEmpty()) {

            User admin = new User("admin", "admin", "admin@mail.ru", 30, roles);
            admin.setPassword(passwordEncoder.encode("admin"));
            em.persist(admin);
        }
    }

    public List<User> allUsers() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    public void editUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.merge(user);
    }

    public void apiEditUser(User user, User updateUser) {
        if (user.getFirstName() != null) {
            updateUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            updateUser.setLastName(user.getLastName());
        }
        if (user.getAge() != 0) {
            updateUser.setAge(user.getAge());
        }
        if (user.getPassword() != null) {
            updateUser.setPassword(user.getPassword());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (!user.getRoles().isEmpty()) {
            if (user.getRoles().size() > 1) {
                user.setRoles(new HashSet<>(rs.allRoles()));
            } else {
                user.setRoles(rs.getRole("ROLE_USER"));
            }
            updateUser.setRoles(user.getRoles());
        }
    }

    public User getUserByEmail(String email) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public void deleteUser(int id) {
        Query query = em.createNativeQuery("delete from users_roles where users_id = " + id);
        query.executeUpdate();
        User user = em.find(User.class, id);
        em.remove(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserByEmail(email);
    }

    public User convertDTO(UserDTO userRequestDTO) {
        System.out.println("Полученные роли11: " + userRequestDTO.getRoles());
        User user = new User();
        user.setFirstName(userRequestDTO.getName());
        user.setLastName(userRequestDTO.getLastName());
        user.setAge(userRequestDTO.getAge());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        Set<Role> roles = new HashSet<>();
        for (String roleName : userRequestDTO.getRoles()) {
            roles.add(rs.getRoleByName(roleName));
        }
        user.setRoles(roles);
        return user;
    }

    public List<UserDTO> getAllUsersDTO(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }
}
