package com.project.mobile.movie_db_training.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.project.mobile.movie_db_training.BuildConfig;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.detail.MovieDetailActivity;
import com.project.mobile.movie_db_training.network.NetworkModule;
import com.project.mobile.movie_db_training.utils.Constants;
import com.project.mobile.movie_db_training.utils.NetworkChecking;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends IntentService {

    private String CHANNEL_ID = "1";
    private static final String ACTION_MOVIE = "movie";

    public NotificationService() {
        super("NotificationService");
    }


//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, NotificationService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MOVIE.equals(action)) {
                if (!NetworkChecking.checkInternetAvailability(this)) {
                    return;
                }
                Random random = new Random();
                int randId = random.nextInt(901) + 2;
                NetworkModule.getTMDbService().getMovieDetail(randId + "", BuildConfig.TMDB_API_KEY)
                        .enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    movieNotification(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                //TODO
                            }
                        });
            }
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }

    public static void setNotificationServiceAlarm(Context context, boolean isNotificationEnabled) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = NotificationService.newIntent(context);
        intent.setAction(ACTION_MOVIE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        if (isNotificationEnabled) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        }
    }

    private PendingIntent getIntentToStartMovieDetail(Movie movie) {
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE_KEY, movie);
        movieDetailIntent.putExtras(extras);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(movieDetailIntent);
        return stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void movieNotification(Movie movie) {
        createNotificationChannel();
        PendingIntent movieDetailPendingIntent = getIntentToStartMovieDetail(movie);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Discover")
                .setContentText(movie.getTitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(movieDetailPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
