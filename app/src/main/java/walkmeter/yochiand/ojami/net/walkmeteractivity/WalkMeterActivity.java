package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class WalkMeterActivity
    extends Activity
    implements MainClickListener{

    // 表示処理
    private Display mDisplay = null;

    // 散歩計サービス
    private WalkMeterService mWalkMeterService = null;

    private UseGoogleMap mUseGoogleMap = null;

    private MyDatabaseHelper mDatabase = null;

    private Date mStartDate = null;

    private int mWalkCount = 0;

    // サービス接続・切断時のコールバック
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWalkMeterService = ((WalkMeterService.WalkMeterBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWalkMeterService = null;
        }
    };

    // ブーロキャスト受信時の処理
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 歩数を表示
            mWalkCount = intent.getIntExtra("WalkingCount", -1);
            mDisplay.showWalkCount(mWalkCount);
        }
    };

    // 初回呼び出し
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 画面表示の設定
        mDisplay = Display.getInstance();
        mDisplay.init(this);

        // ボタン動作の設定
        MyOnClickListener.setMainClickListener(this);
        Button startButton = (Button)findViewById(R.id.button_start);
        startButton.setOnClickListener(MyOnClickListener.getInstance());
        Button stopButton = (Button)findViewById(R.id.button_stop);
        stopButton.setOnClickListener(MyOnClickListener.getInstance());

        // データベースの準備
        mDatabase = new MyDatabaseHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopWalkMeterService();
    }

    /**
     * 開始ボタン押下時の処理
     * @param v ビュー
     */
    @Override
    public void onStartClick(View v) {

        // GPSの有効・無効を確認
        boolean result = checkGPSService();
        if (! result) {
            return;
        }

        // サービスの起動
        startWalkService();
    }

    /**
     * 停止ボタン押下時の処理
     * @param v ビュー
     */
    @Override
    public void onStopClick(View v) {
        stopWalkMeterService();
    }

    /**
     * 散歩計サービス起動
     */
    private void startWalkService() {
        if(mUseGoogleMap != null) {
            return;
        }

        mUseGoogleMap = UseGoogleMap.getInstance();
        mUseGoogleMap.init(this).start(this);

        if (mWalkMeterService != null) {
            return;
        }
        // サービスの起動
        Intent intent = new Intent(this, WalkMeterService.class);
        startService(intent);
        IntentFilter intentFilter = new IntentFilter(WalkMeterService.ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        // 開始時刻を保存
        mStartDate = new Date();
    }

    /**
     * 散歩計サービス停止
     */
    private void stopWalkMeterService() {
        if (mWalkMeterService == null) {
            return;
        }
        // サービスの停止
        unbindService(mServiceConnection);
        unregisterReceiver(mBroadcastReceiver);
        mWalkMeterService.stopSelf();
        mWalkMeterService = null;

        // データベースに登録
        mDatabase.insertData(mStartDate, mUseGoogleMap.getStartLocation(),
                            new Date(), mUseGoogleMap.getStopLocation(), mWalkCount);

        if(mUseGoogleMap == null) {
            return;
        }
        mUseGoogleMap.stop();
        mUseGoogleMap = null;

    }

    /**
     * GPSの有効・無効の確認
     */
    private boolean checkGPSService() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        boolean result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(result) {
            return true;
        }
        AlertDialog.Builder checkMapDialog = new AlertDialog.Builder(this);
        checkMapDialog.setTitle("GPSが使用できません")
                .setMessage("GPS機能を有効にしてください")
                .setPositiveButton("OK", null)
                .show();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.walk_meter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                onHistoryButton();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void onHistoryButton() {
        final ArrayList<WalkMeterData> list = mDatabase.getAllData();
        ListView lv = new ListView(this);

        lv.setAdapter(new WalkMeterListAdapter(this, R.layout.list_row, list));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WalkMeterData data = list.get(position);
                Log.d("TEST", "WalkMeterActivity: onItemClick: " + data.StartDate);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("履歴")
                .setPositiveButton("キャンセル", null)
                .setView(lv);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
