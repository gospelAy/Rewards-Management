package com.example.Rewards.Management.config;

import com.example.Rewards.Management.Utils.JwtUtil;
import com.example.Rewards.Management.security.manager.RewardManagementAuthenticationManager;
import com.example.Rewards.Management.security.provider.RewardManagementAuthenticationProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class AppConfig {

    @Value("${jwt.signing.secret}")
    private String jwt_secret;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwt_secret);
    }
    @Bean
    @Lazy
    public RewardManagementAuthenticationManager rewardManagementAuthenticationManager(AuthenticationProvider authenticationProvider) {
        return new RewardManagementAuthenticationManager(authenticationProvider);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
