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
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    @Column(name = "message")
    private String message;
    @Getter
    @Setter
    @Column(name = "type_id")
    private long typeId;
    @Getter
    @Setter
    @Column(name = "user_id")
    private long userId;

}

