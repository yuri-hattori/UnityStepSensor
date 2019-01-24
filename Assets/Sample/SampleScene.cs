using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using jp.co.sample.lib.Sensor;


public class SampleScene : MonoBehaviour
{
    [SerializeField] private Text stepCountText;
    [SerializeField] private Text enabledSensorText; 

    private bool sensorEnabledFlag = true;
    private bool serviceEnabledFlag = true;

    void Start()
    {
        StepSensor.Initialize();
        
        stepCountText.text = "0歩";
        enabledSensorText.gameObject.SetActive(false);
    }

    void Update()
    {
        serviceEnabledFlag = StepSensor.IsEnabledService();

        if (serviceEnabledFlag)
        {
            enabledSensorText.text = "計測中です";
            
            int count = StepSensor.GetStepCount();
            stepCountText.text = string.Format("{0}歩", count.ToString());
        }
        else
        {
            enabledSensorText.text = "";
        }
    }

    public void OnClickStartButton()
    {
        int stepSaveFreq = 1;
        StepSensor.StartService(stepSaveFreq);
    }

    public void OnClickStopButton()
    {
        StepSensor.StopService();
    }

    public void OnClickResetButton()
    {
        stepCountText.text = "0歩";
        StepSensor.ResetStepCount();
    }

}
