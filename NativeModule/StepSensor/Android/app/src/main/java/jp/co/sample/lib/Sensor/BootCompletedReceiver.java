/*package jp.co.sample.lib.Sensor;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Build;

public class BootCompletedReceiver extends BroadcastReceiver
{

    public BootCompletedReceiver()
    {
        // コンストラクタ
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED))
        {
            // 起動するサービスを指定
            Intent intent_service = new Intent(context, jp.co.drecom.lib.Sensor.StepSensorIntentService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                // Forgroundで起動しないとクラッシュする
                context.startForegroundService(intent_service);
            }
            else
            {
                context.startService(intent_service);
            }
        }
    }
}
*/
