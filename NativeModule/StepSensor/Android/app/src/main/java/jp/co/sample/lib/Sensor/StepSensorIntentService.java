package jp.co.sample.lib.Sensor;

import android.app.Notification;
import android.app.IntentService;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;

public class StepSensorIntentService extends IntentService implements SensorEventListener
{
    private final static int DEFAULT_FREQUENCY = 1;
    private final static String FREQUENCY_KEY = "frequency";

    private static int stepSaveFrequency = DEFAULT_FREQUENCY;

    private SensorManager mSensorManager = null;
    private Sensor mStepDetectorSensor = null;
    private int iter = 0;

    public StepSensorIntentService (String name)
    {
        super(name);
    }

    public StepSensorIntentService ()
    {
        super("StepSensorIntentService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        runForgoundExec();

        NotificationHelper notificationHelper = new NotificationHelper(this);
        Notification.Builder builder = notificationHelper.getNotification();
        startForeground(2, builder.build());

        // 保存
        if(StepSensorIntentService.stepSaveFrequency != DEFAULT_FREQUENCY)
        {
            StepSensorIntentService.editSharedPrefInt(this, StepSensor.PREFERENCES_FREQUENCY_KEY, StepSensorIntentService.FREQUENCY_KEY, StepSensorIntentService.stepSaveFrequency);
        }

        // 再度、読み直し
        StepSensorIntentService.stepSaveFrequency = StepSensorIntentService.readSharedPrefInt(this, StepSensor.PREFERENCES_FREQUENCY_KEY, StepSensorIntentService.FREQUENCY_KEY, StepSensorIntentService.DEFAULT_FREQUENCY);
    }

    @Override
    public void onHandleIntent (Intent intent)
    {
        try
        {

            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            if(mSensorManager == null)
            {
                return;
            }

            mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            if(mStepDetectorSensor == null)
            {
                return;
            }

            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

            if(mStepDetectorSensor == null)
            {
                return;
            }

            // 明示的な停止がない場合にはServiceは起動し続ける
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Log.e("[StepSensor]", "Sleep interrupted.");
                }
            }
        }
        catch (Exception e)
        {
            Log.e("[StepSensor]", "An error occurred while executing StepSensor Service");
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        iter = (iter + 1) % StepSensorIntentService.stepSaveFrequency;
        if(iter != 0)
        {
            return;
        }

        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR)
        {
            long nowTime = System.currentTimeMillis();
            String nowTimeString = String.valueOf(nowTime);

            int nowStep = StepSensorIntentService.readSharedPrefInt(this, StepSensor.PREFERENCES_DATA_KEY, "STEP_COUNT", 0);
            StepSensorIntentService.editSharedPrefInt(this, StepSensor.PREFERENCES_DATA_KEY, "STEP_COUNT", nowStep + StepSensorIntentService.stepSaveFrequency);
        }

        return;
    }

    /**
     * Serviceのフォアグラウンド実行への昇格
     */
    private void runForgoundExec()
    {
        Intent intent = new Intent(this, StepSensorIntentService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            this.startForegroundService(intent);
        }
        else
        {
            this.startService(intent);
        }
    }

    /**
     * 歩数を保存する間隔を設定する
     * @param frequency 保存間隔（歩）
     */
    public static void setSaveFrequency(int frequency)
    {
        StepSensorIntentService.stepSaveFrequency = frequency;
        return;
    }

    public static synchronized void editSharedPrefInt(Context context, String dataPath, String key, int value)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(dataPath, Context.MODE_MULTI_PROCESS);

        if(sharedPref == null)
        {
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static synchronized int readSharedPrefInt(Context context, String dataPath, String key, int initValue)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(dataPath, Context.MODE_MULTI_PROCESS);


        if(sharedPref == null)
        {
            return initValue;
        }

        return sharedPref.getInt(key, initValue);
    }

    public static synchronized void resetSharedPrefdata(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(StepSensor.PREFERENCES_DATA_KEY, Context.MODE_MULTI_PROCESS);

        if(sharedPref == null)
        {
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
