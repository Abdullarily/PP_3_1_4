package ru.kata.spring.boot_security.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.HashSet;
import java.util.Set;

public class UserRequestDTO {
    private String name;
    private String lastName;
    private int age;
    private String email;
    private String password;

    @JsonProperty("roleIds")
    private Set<Integer> roles = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public String toString() {
        return name + " = name\n" + lastName + " = lastName\n" + age + " = age\n" + email + " = email\n" + password + " = password";
    }
}
