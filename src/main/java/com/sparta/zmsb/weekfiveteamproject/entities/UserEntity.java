package com.sparta.zmsb.weekfiveteamproject.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", schema = "world")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 20, message = "Username must be 20 characters or less.")
    @Pattern(regexp = "[a-zA-Z]+", message = "Username can only contain alphabetical characters!")
    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Pattern(regexp = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$", message = "Please enter a valid email address")
    private String email;

    private String roles;

    public UserEntity(String username, String password, String email, String roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(max = 20, message = "Username must be 20 characters or less.") @Pattern(regexp = "[a-zA-Z]+", message = "Username can only contain alphabetical characters!") String getUsername() {
        return username;
    }

    public void setUsername(@Size(max = 20, message = "Username must be 20 characters or less.") @Pattern(regexp = "[a-zA-Z]+", message = "Username can only contain alphabetical characters!") String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @Pattern(regexp = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$", message = "Please enter a valid email address") String getEmail() {
        return email;
    }

    public void setEmail(@Pattern(regexp = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$", message = "Please enter a valid email address") String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
