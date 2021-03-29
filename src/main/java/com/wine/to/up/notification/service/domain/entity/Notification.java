package com.wine.to.up.notification.service.domain.entity;

import lombok.*;

import javax.persistence.*;

import com.wine.to.up.notification.service.domain.util.NotificationType;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "notification")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "message")
    private String message;

    @Column(name = "type_id")
    @Enumerated(EnumType.ORDINAL)
    private NotificationType type;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "wine_id")
    private String wineId;

    @Column(name = "viewed")
    @Builder.Default
    private boolean viewed = false;

    public void setCurrentTime() {
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
}

