package com.example.Rewards.Management.security.manager;

import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RewardManagementAuthenticationManager implements AuthenticationManager {
    private final AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authResult = null;
        if (authenticationProvider.supports(authentication.getClass())) {
            authResult = authenticationProvider.authenticate(authentication);

            return authResult;
        } else throw new BadCredentialsException("):");
    }
}
