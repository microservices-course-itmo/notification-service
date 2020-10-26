package com.wine.to.up.user.service.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserTokens {
    private Long userId;
    private List<String> iosTokens;
    private List<String> fcmTokens;
}