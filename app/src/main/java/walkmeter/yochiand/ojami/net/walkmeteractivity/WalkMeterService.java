package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 歩数系サービス
 * Created by momomo00 on 15/01/15.
 */
public class WalkMeterService
    extends Service
    implements OnAccelerometerChangedListener {

    // サービス名
    public static final String ACTION = "Walk Meter Service";
    // 重み付け用しきい値
    private final static float OFFSET = 0.3f;

    // 散歩計センサー
    private SensorAdapter mSensorAdapter = null;

    // 歩数
    private int mWalkingCount;
    // 前回の動作データ
    private float mPrevMove;
    // 動作チェック用
    private boolean mCountUp;

    /**
     * コンストラクタ
     */
    public WalkMeterService() {
        this.mWalkingCount = 0;
        this.mPrevMove = 0.0f;
        this.mCountUp = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TEST", "WalkMeterService : onCreate");
    }

    /**
     * バインドの定義
     */
    public class WalkMeterBinder extends Binder {
        /**
         * サービスの返却
         * @return サービス
         */
        public WalkMeterService getService() {
            return WalkMeterService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TEST", "WalkMeterService : onBind");
        return new WalkMeterBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startWalkMeterSensor();
        Log.d("TEST", "WalkMeterService : onStartCommand");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopWalkMeterSensor();
        Log.d("TEST", "WalkMeterService : onDestroy");
    }

    /**
     * 散歩計センサー起動
     */
    private void startWalkMeterSensor() {
        if (this.mSensorAdapter != null) {
            return;
        }
        this.mSensorAdapter = SensorAdapter.getInstance().startSensor(this, this);
    }

    /**
     * 散歩計センサー停止
     */
    private void stopWalkMeterSensor() {
        if (this.mSensorAdapter == null) {
            return;
        }
        this.mSensorAdapter.stopSensor();
        this.mSensorAdapter = null;
    }

    @Override
    public void onAccelerometerChanged(SensorEvent event) {
        if (! checkWalking(event)) {
            return;
        }
        this.mWalkingCount++;
        Intent intent = new Intent(ACTION);
        intent.putExtra("WalkingCount", mWalkingCount);
        sendBroadcast(intent);
    }

    /**
     * 歩いたか確認
     * @param event センサーイベント
     * @return 動作がある場合 true
     */
    private boolean checkWalking(SensorEvent event) {
        boolean result = false;
        float nextMove = Math.abs(event.values[0] + Math.abs(event.values[1])
                                    + Math.abs(event.values[2]));

        if (this.mCountUp) {
            if((this.mPrevMove - OFFSET) > nextMove) {
                this.mCountUp = false;
                this.mPrevMove = nextMove;
            }
        } else {
            if ((this.mPrevMove + OFFSET) < nextMove) {
                this.mCountUp = true;
                this.mPrevMove = nextMove;
                result = true;
            }
        }

        return result;
    }
}
