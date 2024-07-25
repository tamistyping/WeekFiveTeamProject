package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.UserEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.sparta.zmsb.weekfiveteamproject.entities.SecurityUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MpoUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MpoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(
                SecurityUser::new
        ).orElseThrow(() -> new UsernameNotFoundException("User not found:" + username));
    }

    public boolean validateUser(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean validateUserPassword(String username, String password) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(null);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public boolean validateNewUsername(UserEntity user) {
        for(UserEntity userEntity : userRepository.findAll()) {
            if(userEntity.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public boolean validateNewEmail(UserEntity user) {
        for(UserEntity userEntity : userRepository.findAll()) {
            if(userEntity.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public boolean validateNewPassword(String password) {
        return password.length() <= 8;
    }

    public void grantAdmin(UserEntity user, String role) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(()->new UsernameNotFoundException("User not found"));
        userEntity.setRoles("ROLE_" + role);
        userRepository.save(userEntity);
    }
}
