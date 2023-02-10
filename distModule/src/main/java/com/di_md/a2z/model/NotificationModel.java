package com.di_md.a2z.model;

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
