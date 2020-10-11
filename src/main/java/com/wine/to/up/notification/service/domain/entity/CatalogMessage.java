package com.wine.to.up.notification.service.domain.entity;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CatalogMessage {
    private Long id;
    private String title;
    private Float price;
}