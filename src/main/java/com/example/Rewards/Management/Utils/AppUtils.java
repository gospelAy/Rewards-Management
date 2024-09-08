package com.example.Rewards.Management.Utils;

import java.util.List;

public class AppUtils {
    public static final String CLAIMS_VALUE = "ROLES";
    public static final String EMPTY_SPACE_VALUE=" ";
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String CLAIM_VALUE = "claim";
    public static final String MESSAGE_FOR_LOGIN_RESPONSE ="SUCCESSFUL";


    public static List<String> getAuthWhiteList() {
        return List.of(
                "/api/rewards/register",
                "/api/rewards/transfer",
                "/api/rewards/balance/**",
                "/api/rewards/history/**",
                "/api/rewards/login",
                "/api/rewards/reset-password"
        );
    }
}
