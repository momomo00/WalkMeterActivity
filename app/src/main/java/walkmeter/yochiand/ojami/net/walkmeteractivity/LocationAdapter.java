package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 位置情報を扱う
 * Created by momomo00 on 15/02/03.
 */
public final class LocationAdapter
    implements LocationListener {

    // シングルトン
    private final static LocationAdapter mInstance = new LocationAdapter();
    private LocationAdapter() {}
    public static LocationAdapter getInstance() {
        return mInstance;
    }

    // 位置情報変化時の動作
    private OnLocationChangedListener mListener = null;

    // 位置情報
    private LocationManager mLocationManager = null;

    /**
     * 位置情報の取得開始
     * @param context コンテキスト
     * @param listener 位置情報変更後の動作
     */
    public void start(Context context, OnLocationChangedListener listener) {
        mListener = listener;

        // 位置情報の取得方法を登録
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    /**
     * 位置情報の取得終了
     */
    public void stop() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mListener == null) {
            return;
        }
        mListener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
