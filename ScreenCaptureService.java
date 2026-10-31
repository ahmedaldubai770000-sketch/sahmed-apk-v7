
package com.sahmed.privatechat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
public class ScreenCaptureService extends Service {
    private static final String CHANNEL_ID = "screen_capture_channel";
    @Override public void onCreate() { super.onCreate(); createChannel(); }
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Notification n = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("مشاركة الشاشة نشطة")
            .setContentText("سـًٌِـاحـًٌِـمـًٌِـد يشاهد شاشتك الآن")
            .setSmallIcon(android.R.drawable.ic_menu_share)
            .setOngoing(true).build();
        startForeground(1, n);
        return START_STICKY;
    }
    private void createChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel c = new NotificationChannel(CHANNEL_ID,"مشاركة الشاشة",NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(c);
        }
    }
    @Override public IBinder onBind(Intent intent){ return null; }
}
