package com.example.Rewards.Management.Utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Rewards.Management.exceptions.CustomerNotFound;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

import static com.example.Rewards.Management.Utils.AppUtils.CLAIMS_VALUE;

@AllArgsConstructor
@Getter
public class JwtUtil {
    private final String secret;

    public Map<String, Claim> extractClaimsFrom(String token) throws CustomerNotFound {
        DecodedJWT decodedJwt = validateToken(token);
        if (decodedJwt.getClaim(CLAIMS_VALUE) == null) {
            throw new CustomerNotFound("Claim " + CLAIMS_VALUE + " not found in token");
        }
        return decodedJwt.getClaims();
    }

    private DecodedJWT validateToken(String token) {
        return com.auth0.jwt.JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token);
    }
}
