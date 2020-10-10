package com.wine.to.up.notification.service.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FcmPushConfiguration {
    private String title;
    private String body;
    private String icon;
    private String clickAction;
    private String ttlInSeconds;
}
