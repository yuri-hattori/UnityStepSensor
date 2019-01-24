package jp.co.sample.lib.Sensor;

import android.app.Activity;
import android.app.Service;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.unity3d.player.UnityPlayer;

public class StepSensor
{
    public static final String PACKAGE_NAME = "jp.co.sample.lib.Sensor";
    public static final String PREFERENCES_DATA_KEY = PACKAGE_NAME + ".data";
    public static final String PREFERENCES_FREQUENCY_KEY = PACKAGE_NAME + ".frequency";
    public static final String SERVICE_NAME = PACKAGE_NAME + ".StepSensorIntentService";

    // コンストラクタのアクセス禁止
    private StepSensor() {}

    /**
     * Serviceの開始
     */
    public static void startService(int frequency)
    {
        if (!isEnabledService())
        {
            StepSensorIntentService.setSaveFrequency(frequency);

            Activity activity = UnityPlayer.currentActivity;
            Intent intent = new Intent(getUnityPlayerContext(), jp.co.sample.lib.Sensor.StepSensorIntentService.class);
            activity.startService(intent);
        }
        else
        {
            Log.e("[StepSensor]", "Service already running");
        }
    }

    /**
     * Serviceの強制停止
     */
    public static void stopService()
    {
        Activity activity = UnityPlayer.currentActivity;
        Intent intent = new Intent(getUnityPlayerContext(), jp.co.sample.lib.Sensor.StepSensorIntentService.class);
        activity.stopService(intent);
    }

    /**
     * 保存された歩行数の取得
     * @return 歩数
     */
    public static int getStepCount()
    {
        int step = 0;

        SharedPreferences sharedPref;

        try
        {
            Context context = getUnityPlayerContext();

            String appPackageName = context.getPackageName();
            Context service = context.createPackageContext(appPackageName, Context.CONTEXT_RESTRICTED);
            sharedPref = service.getSharedPreferences(PREFERENCES_DATA_KEY, Context.MODE_MULTI_PROCESS);

            if(sharedPref == null)
            {
                return 0;
            }

            return sharedPref.getInt("STEP_COUNT", 0);
        }
        catch (Exception e)
        {
            Log.e("[StepSensor]", e.getMessage());
        }

        return 0;
    }

    /**
     * sharedPreferenceに保存されている歩数データをリセットする
     */
    public static void resetStepCount()
    {
        StepSensorIntentService.resetSharedPrefdata(getUnityPlayerContext());
        return;
    }

    /**
     * Service実行中かどうかの確認
     * @return true or false
     */
    public static boolean isEnabledService()
    {
        Context context = getUnityPlayerContext();
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        // TODO: deprecatedなので代替方法を考える
        // https://developer.android.com/reference/android/app/ActivityManager.html#getRunningServices(int)
        List<RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo curr : listServiceInfo)
        {
            if (curr.service.getClassName().equals(SERVICE_NAME))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Sensorが使えるかどうか
     * @return true or false
     */
    public static boolean isEnabledSensor()
    {
        Context context = getUnityPlayerContext();

        SensorManager mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);

        if(mSensorManager == null)
        {
            return false;
        }

        Sensor mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(mStepDetectorSensor == null)
        {
            return false;
        }

        return true;
    }

    private static Context getUnityPlayerContext()
    {
        Activity activity = UnityPlayer.currentActivity;
        Context context = activity.getApplicationContext();
        return context;
    }
}
