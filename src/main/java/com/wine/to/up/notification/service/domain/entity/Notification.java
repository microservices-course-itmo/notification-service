package com.wine.to.up.notification.service.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
@Setter
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "message")
    private String message;
    @Column(name = "type_id")
    private long typeId;
    @Column(name = "user_id")
    private long userId;

}

