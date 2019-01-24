package jp.co.sample.lib.Sensor;

import android.content.ContextWrapper;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Resources;

public class NotificationHelper extends ContextWrapper
{

    private static final String CHANNEL_GENERAL_ID = "step_sensor";
    private NotificationManager manager;

    public NotificationHelper(Context base)
    {
        super(base);

        if (isOreoOrLater())
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_GENERAL_ID, "StepSensor Notifications", NotificationManager.IMPORTANCE_LOW);
            getManager().createNotificationChannel(channel);
        }
    }

    public Notification.Builder getNotification()
    {
        Notification.Builder builder = isOreoOrLater()
                                       ? new Notification.Builder(this, CHANNEL_GENERAL_ID)
                                       : new Notification.Builder(this);

        Resources res = this.getResources();

        int appNameID = res.getIdentifier("stepsensor_notification_title", "string", getPackageName());
        int notificationTextID = res.getIdentifier("stepsensor_notification_text", "string", getPackageName());
        int iconID = res.getIdentifier("ic_launcher", "mipmap", getPackageName());

        String title = appNameID != 0 ? res.getString(appNameID) : "歩数計";
        String text = notificationTextID != 0 ? res.getString(notificationTextID) : "計測中";

        return builder.setContentTitle(title)
               .setContentText(text)
               .setSmallIcon(iconID);
    }

    public void notify(int id, Notification.Builder builder)
    {
        getManager().notify(id, builder.build());
    }

    private NotificationManager getManager()
    {
        if (manager == null)
        {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    private boolean isOreoOrLater()
    {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    }
}
