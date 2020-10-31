package com.wine.to.up.user.service.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

/**
 * Defines values from properties. Can be autowired as a regular class
 */
@SuppressWarnings("SpringPropertySource")
@PropertySources({
        @PropertySource("classpath:application-user-service-api.properties"),
        @PropertySource("classpath:application-user-service-api-${spring.profiles.active:local}.properties")
})
@ConfigurationProperties(prefix = "user.service.api")
@Setter
@Component
@Getter
public class UserServiceApiProperties {
    private String messageSentEventsTopicName;
    private String wineResponseTopic;
    private String host;
}