package com.project.mobile.movie_db_training.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.project.mobile.movie_db_training.service.NotificationService;

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isEnabled = sharedPreferences.getBoolean("notification", false);
        NotificationService.setNotificationServiceAlarm(context, isEnabled);
    }
}