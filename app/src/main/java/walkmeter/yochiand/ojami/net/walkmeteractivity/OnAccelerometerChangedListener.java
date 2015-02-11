package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.hardware.SensorEvent;

/**
 * 加速度センサー受信処理
 * Created by momomo00 on 15/01/13.
 */
public interface OnAccelerometerChangedListener {
    /**
     * 加速度センサー受信時動作
     * @param event センサーイベント
     */
    abstract public void onAccelerometerChanged(SensorEvent event);
}
