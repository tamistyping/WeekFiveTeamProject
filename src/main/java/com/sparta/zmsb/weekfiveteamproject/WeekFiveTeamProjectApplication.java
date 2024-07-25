package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.UserEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WeekFiveTeamProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeekFiveTeamProjectApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            userRepository.save(new UserEntity("admin", passwordEncoder.encode("password"), "admin.site@mpo.com", "ROLE_ADMIN"));
//        };
//    }
}
