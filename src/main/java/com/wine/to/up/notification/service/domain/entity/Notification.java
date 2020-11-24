package com.wine.to.up.notification.service.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wine.to.up.notification.service.domain.util.NotificationType;

import java.sql.Timestamp;
import java.util.Date;

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
    @Enumerated(EnumType.ORDINAL)
    private NotificationType type;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "wine_id")
    private long wineId;

    public static Builder newBuilder() {
        return new Notification().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setUserId(long userId) {
            Notification.this.userId = userId;
            return this;
        }

        public Builder setMessage(String message) {
            Notification.this.message = message;
            return this;
        }

        public Builder setTypeId(NotificationType type) {
            Notification.this.type = type;
            return this;
        }

        public Builder setWineId(long wineId) {
            Notification.this.wineId = wineId;
            return this;
        }

        public Builder setCurrentTime() {
            Date date = new Date();
            Notification.this.timestamp = new Timestamp(date.getTime());
            return this;
        }

        public Notification build() {
            return Notification.this;
        }
    }
}

