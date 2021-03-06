package com.android.remoteview;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RemoteViews remoteViews = null;
    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                showSystemNotification();
            }
        };
        handler.postDelayed(run,8000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.system_notification:
                // showSystemNotification();
                break;
            case R.id.custom_notification:
                showRemoteViewsNotification();
                break;
        }
        return true;
    }


//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void showSystemNotification() {
//        // API 16之前的方式好多都过时
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setSmallIcon(R.drawable.ic_notification);// 设置通知小图标,在下拉之前显示的图标
//        // builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification));// 落下后显示的图标
//        builder.setWhen(SystemClock.currentThreadTimeMillis());
//        builder.setContentTitle("This is title");
//        builder.setContentText("Here is content");
//        // builder.setSound(uri);//声音提示
//        // builder.setSound(sound, streamType);//科设置 streamtype
//        // builder.setStyle(style);//style设置
//        // http://developer.android.com/reference/android/app/Notification.Style.html
//        // builder.setVibrate(long[]);//设置震动
//        // builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等  （没有用）
//        builder.setAutoCancel(true);// 自动取消
//        builder.setOnlyAlertOnce(true);// 只alert一次
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setContentInfo("额外的内容");// 添加到了右下角
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setPackage(this.getPackageName());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, notification);
//    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showSystemNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setPackage(this.getPackageName());
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notification = new Notification.Builder(this);
        notification.setSmallIcon(R.drawable.ic_notification); // 没有下拉的图标显示
        notification.setContentTitle("this id title");
        notification.setContentText("here is content"); // smallicon title text 是必须的，不然会报错
        notification.setContentIntent(pIntent);
        notification.setAutoCancel(true); // 当通知被点击之后，自动消失与 build.flags = Notification.FLAG_AUTO_CANCEL；作用相同
        notification.setOnlyAlertOnce(true);// 只alert一次
        notification.setWhen(System.currentTimeMillis()); // 7.0 不显示出来
        notification.setDefaults(Notification.DEFAULT_ALL); // 获取通知的所有默认东西，包括振动，铃声等
//        notification.setVibrate();
        Notification build = notification.build();
        build.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, build);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showRemoteViewsNotification() {

        remoteViews = new RemoteViews(getPackageName(),R.layout.notification_custom_type);

        Intent intent;
        PendingIntent pIntent;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        intent = new Intent(this, MainActivity.class);
        intent.setPackage(this.getPackageName());
        pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notification = new Notification.Builder(this);
        notification.setSmallIcon(R.drawable.ic_notification);
        // notification.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_notification));

        // notification.setContentTitle("this id title");
        // notification.setContentText("here is content"); // smallicon title text 是必须的，不然会报错
        notification.setAutoCancel(true); // 当通知被点击之后，自动消失与 build.flags = Notification.FLAG_AUTO_CANCEL；作用相同
        notification.setOnlyAlertOnce(true);// 只alert一次
        notification.setWhen(System.currentTimeMillis()); // 7.0 不显示出来
        notification.setDefaults(Notification.DEFAULT_ALL); // 获取通知的所有默认东西，包括振动，铃声等

        remoteViews.setImageViewResource(R.id.notification_icon, R.mipmap.ic_launcher);
        //remoteViews.setImageViewResource(R.id.notification_image_btn,R.drawable.ic_notification);
        remoteViews.setImageViewBitmap(R.id.notification_image_btn, BitmapFactory.decodeResource(getResources(), R.drawable.notification_btn));
        remoteViews.setTextViewText(R.id.notification_title, "这是自定义view的title");
        remoteViews.setTextViewText(R.id.notification_content, "这是自定义view的content");
        remoteViews.setTextViewText(R.id.notification_time,getCurrentTime());

        intent = new Intent(this,Main2Activity.class);
        pIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        notification.setContent(remoteViews);
        notification.setContentIntent(pIntent);

        Notification build = notification.build();
        build.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, build);

        remoteViews.setOnClickPendingIntent(R.id.notification_image_btn, pIntent); // 点击btn跳转并且通知栏消失，不消失放到138行之后就行
    }

    protected String getCurrentTime() {
        String time = "unknown";
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        time = hour + ":" + minute;
        return time;
    }
}
