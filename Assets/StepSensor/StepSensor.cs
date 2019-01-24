using System;
using UnityEngine;

namespace jp.co.sample.lib.Sensor
{
public class StepSensor
{
#if UNITY_ANDROID && !UNITY_EDITOR
    private static AndroidJavaClass jo = null;
#endif
    
    /// <summary>
    /// StepSensorの初期化をする
    /// </summary>
    public static void Initialize()
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (jo != null)
        {
            return;
        }
        jo = new AndroidJavaClass("jp.co.sample.lib.Sensor.StepSensor");
#else
        Debug.Log("[StepSensor] サポートしていないプラットフォームです");
#endif      
    }

    /// <summary>
    /// 歩数を返す
    /// </summary>
    public static int GetStepCount()
    {
#if UNITY_ANDROID && !UNITY_EDITOR  
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return 0;
        }
        int count = jo.CallStatic<int>("getStepCount");
        return count;
#endif
        return 0;
    }

    /// <summary>
    /// ローカルに保存されている歩数データをすべて削除する
    /// </summary>
    public static void ResetStepCount()
    {
#if UNITY_ANDROID && !UNITY_EDITOR  
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return;
        }
        jo.CallStatic("resetStepCount");
#endif
        return;
    }
    
    /// <summary>
    /// 歩数計Serviceが実行中かどうかを返す
    /// </summary>
    public static bool IsEnabledService()
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return false;
        }
        bool flag = jo.CallStatic<bool>("isEnabledService");
        return flag;
#endif
        return false;
    }

    /// <summary>
    /// 歩数計のセンサが使えるかどうかを返す
    /// </summary>
    public static bool IsEnabledSensor()
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return false;
        }
        bool flag = jo.CallStatic<bool>("isEnabledSensor");
        return flag;
#endif
        return false;
    }

    /// <summary>
    /// 歩数計Service実行開始
    /// </summary>
    public static void StartService(int freq)
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return;
        }
        jo.CallStatic("startService", freq);
#else
        Debug.Log("[StepSensor] サポートしていないプラットフォームです");
#endif
        return;
    }

    /// <summary>
    /// 期間中の歩数計Serviceを停止する
    /// </summary>
    public static void StopService()
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (jo == null)
        {
            Debug.Log("[StepSensor] 初期化されていません");
            return;
        }
        jo.CallStatic("stopService");
#else
        Debug.Log("[StepSensor] サポートしていないプラットフォームです");
#endif
        return;
    }
}
}


