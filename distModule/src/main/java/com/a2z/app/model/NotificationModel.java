package com.a2z.app.model;

import androidx.annotation.Keep;

@Keep
public class NotificationModel {
    String Play,NotificationMessage;

    public String getPlay() {
        return Play;
    }

    public void setPlay(String play) {
        Play = play;
    }

    public String getNotificationMessage() {
        return NotificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        NotificationMessage = notificationMessage;
    }
}
