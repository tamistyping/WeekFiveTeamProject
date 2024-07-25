package com.sparta.zmsb.weekfiveteamproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.sparta.zmsb.weekfiveteamproject.entities.UserEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import com.sparta.zmsb.weekfiveteamproject.service.MpoUserDetailsService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MpoUserDetailsService mpoUserDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(userEntity));
        UserDetails userDetails = mpoUserDetailsService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testValidateUserExists() {
        boolean expected = false;
        String username = "testUser";
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new UserEntity()));
        boolean actual = mpoUserDetailsService.validateUser(username);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateUserNotExists() {
        boolean expected = true;
        String username = "testUser";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        boolean actual = mpoUserDetailsService.validateUser(username);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateUserPasswordSuccess() {
        boolean expected = true;
        String username = "testUser";
        String password = "password";
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(encodedPassword);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        boolean actual = mpoUserDetailsService.validateUserPassword(username, password);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateUserPasswordFailure() {
        boolean expected = false;
        String username = "testUser";
        String password = "password";
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(password);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        boolean actual = mpoUserDetailsService.validateUserPassword(username, password);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateNewUsernameUserExists() {
        boolean expected = true;
        UserEntity user = new UserEntity();
        user.setUsername("existingUser");
        when(userRepository.findAll()).thenReturn(List.of(user));
        boolean actual = mpoUserDetailsService.validateNewUsername(user);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateNewUsernameUserNotExists() {
        boolean expected = false;
        UserEntity user = new UserEntity();
        user.setUsername("newUser");
        when(userRepository.findAll()).thenReturn(List.of());
        boolean actual = mpoUserDetailsService.validateNewUsername(user);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateNewEmailEmailExists() {
        boolean expected = true;
        UserEntity user = new UserEntity();
        user.setEmail("existing@example.com");
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("existing@example.com");
        when(userRepository.findAll()).thenReturn(List.of(existingUser));
        boolean actual = mpoUserDetailsService.validateNewEmail(user);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateNewEmailEmailNotExists() {
        boolean expected = false;
        UserEntity user = new UserEntity();
        user.setEmail("new@example.com");
        when(userRepository.findAll()).thenReturn(List.of());
        boolean actual = mpoUserDetailsService.validateNewEmail(user);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateNewPassword_TooShort() {
        String password = "pass";
        boolean expected = true;
        boolean actual = mpoUserDetailsService.validateNewPassword(password);
        assertEquals(expected,actual);
    }

    @Test
    void testValidateNewPassword_Valid() {
        String password = "password";
        boolean expected = false;
        boolean actual = mpoUserDetailsService.validateNewPassword(password);
        assertEquals(expected, actual);
    }

    @Test
    void testChangeRoleSuccess() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRoles("ROLE_USER");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));
        mpoUserDetailsService.changeRole(userEntity, "ADMIN");
        verify(userRepository).save(userEntity);
        assertEquals("ROLE_ADMIN", userEntity.getRoles());
    }

    @Test
    void testDeleteUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        mpoUserDetailsService.deleteUser(userEntity);
        verify(userRepository).delete(userEntity);
    }
}