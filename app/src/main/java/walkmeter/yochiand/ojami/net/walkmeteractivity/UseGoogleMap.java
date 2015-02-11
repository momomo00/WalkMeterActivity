package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by momomo00 on 15/02/03.
 */
public final class UseGoogleMap
    implements OnLocationChangedListener {
    // シングルトン
    private final static UseGoogleMap mInstance = new UseGoogleMap();
    private UseGoogleMap() {}
    public static UseGoogleMap getInstance() {
        return mInstance;
    }

    // Googleマップ
    private GoogleMap mGoogleMap = null;

    // 位置情報センサー
    private LocationAdapter mLocationAdapter = null;

    // 開始位置
    private Location mStartLocation = null;
    // 停止位置
    private Location mStopLocation = null;

   /**
     * Googleマップ使用のための初期化
     * @param activity アクティビティ
     * @return インスタンス
     */
    public UseGoogleMap init(Activity activity) {
        // Googleマップの初期化
        MapFragment mapFragment = (MapFragment)activity.getFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = mapFragment.getMap();
        mapFragment.setRetainInstance(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

      return mInstance;
    }

    public void start(Context context) {
        if(mLocationAdapter != null) {
            return ;
        }
        mLocationAdapter = LocationAdapter.getInstance();
        mLocationAdapter.start(context, this);
    }

    public void stop() {
        if(mLocationAdapter == null) {
            return;
        }
        mLocationAdapter.stop();
        mLocationAdapter = null;

        Display.getInstance().showStopPoint(mStopLocation);
        setStopPositionBitmap();

        mStartLocation = null;
        mStopLocation = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mStartLocation == null) {
            mStartLocation = location;
            Display.getInstance().showStartPoint(mStartLocation);
            setStartPositionBitmap();
       }
        mStopLocation = location;

        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15.5f)
                        .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void setStartPositionBitmap() {
        // 地図上に矢印で開始位置を表示
        LatLng latLng = new LatLng(mStartLocation.getLatitude(), mStartLocation.getLongitude());
        GroundOverlayOptions options = new GroundOverlayOptions();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.aka);
        options.image(bitmap);
        options.anchor(0.5f, 0.5f);
        options.position(latLng, 100.0f, 100.0f);
        GroundOverlay overlay = mGoogleMap.addGroundOverlay(options);
        overlay.setTransparency(0.3f);
    }

    private void setStopPositionBitmap() {
        // 地図上に矢印で開始位置を表示
        LatLng latLng = new LatLng(mStopLocation.getLatitude(), mStopLocation.getLongitude());
        GroundOverlayOptions options = new GroundOverlayOptions();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.mizu);
        options.image(bitmap);
        options.anchor(0.5f, 0.5f);
        options.position(latLng, 100.0f, 100.0f);
        GroundOverlay overlay = mGoogleMap.addGroundOverlay(options);
        overlay.setTransparency(0.3f);
    }

    public Location getStartLocation() {
        return mStartLocation;
    }

    public Location getStopLocation() {
        return mStopLocation;
    }
}
