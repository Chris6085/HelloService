package chrisit_chang.mycompany.helloservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MyForegroundService extends Service {

    static final String ACTION_FOREGROUND = "FOREGROUND";
    static final String ACTION_BACKGROUND = "BACKGROUND";
    private MediaPlayer player;

    public MyForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this,R.raw.correct_sound);
        player.setLooping(true);
        player.start();

        Toast.makeText(this, "Foreground Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("onHandleIntent", "in onHandleIntent");
        if (ACTION_FOREGROUND.equals(intent.getAction())) {
            Log.d("onHandleIntent", "start Play music in foreground");
            startForeground(12, getCompatNotification());

        } else if (ACTION_BACKGROUND.equals(intent.getAction())) {
            Log.d("onHandleIntent", "move service to background or starts a new background service");
            stopForeground(true);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public Notification getCompatNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("service running")
                .setTicker("music playing")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);

        Intent startIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();

        return notification;
    }
        @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Foreground Service destroyed", Toast.LENGTH_SHORT).show();

        if (player.isPlaying()) {
            Log.d("onDestroyed", "player released");
            player.release();
            player = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
