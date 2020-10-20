package com.wine.to.up.notification.service.domain.model.kafka;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CatalogMessage {
    private String id;
    private String title;
    private String price;
}