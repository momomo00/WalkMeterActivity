package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * 加速度センサー
 * Created by momomo00 on 15/01/13.
 */
public final class SensorAdapter
    implements SensorEventListener {

    // シングルトン使用
    private final static SensorAdapter mInstance = new SensorAdapter();
    private SensorAdapter() {}
    public static SensorAdapter getInstance() {
        return mInstance;
    }

    // センサーマネージャー
    private SensorManager mSensorManager = null;
    // 加速度データ受信時動作
    private OnAccelerometerChangedListener mListener = null;


    /**
     * 加速度センサーの起動
     * @param context コンテキスト
     * @return インスタンス
     */
    public  SensorAdapter startSensor(Context context, OnAccelerometerChangedListener onAccelerometerChangedListener) {
        if (mSensorManager != null) {
            return mInstance;
        }
        this.mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            mSensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_UI);
        }

        this.mListener = onAccelerometerChangedListener;

        return mInstance;
    }

    /**
     * 加速度センサーの停止
     */
    public void stopSensor() {
        if (mSensorManager == null) {
            return;
        }
        mSensorManager.unregisterListener(this);
        mSensorManager = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER :
                onAccelerometerChanged(event);
                break;
            default:
                break;
        }
    }

    /**
     * 加速度センサー受信処理
     * @param event センサーイベント
     */
    private void onAccelerometerChanged(SensorEvent event) {
        if (mListener == null) {
            return;
        }
        mListener.onAccelerometerChanged(event);
    }
}
