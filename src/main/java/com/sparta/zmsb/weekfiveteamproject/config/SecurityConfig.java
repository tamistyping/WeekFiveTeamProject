package com.sparta.zmsb.weekfiveteamproject.config;


import com.sparta.zmsb.weekfiveteamproject.service.MpoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final MpoUserDetailsService mpoUserDetailsService;

    @Autowired
    public SecurityConfig(MpoUserDetailsService mpoUserDetailsService) {
        this.mpoUserDetailsService = mpoUserDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/index","/login","/register","/images/**").permitAll()
                .requestMatchers("/auth/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/secure/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/auth/home", true)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                                .expiredUrl("/login?expired=true"))
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                                .permitAll())
                .userDetailsService(mpoUserDetailsService).build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
