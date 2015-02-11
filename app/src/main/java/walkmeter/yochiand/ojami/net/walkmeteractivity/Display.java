package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 画面表示
 * Created by momomo00 on 15/01/15.
 */
public final class Display {
    // シングルトン
    private final static Display mInstance = new Display();
    private Display() {}
    public static Display getInstance() {
        return mInstance;
    }

    private TextView mStartPoint = null;
    private TextView mStopPoint = null;
    private TextView mWalkMeter = null;

    private Geocoder mGeocoder = null;

    public void init(Activity activity) {
        mGeocoder = new Geocoder(activity, Locale.getDefault());

        // 表示領域の初期化
        mStartPoint = (TextView)activity.findViewById(R.id.start_result);
        mStopPoint = (TextView)activity.findViewById(R.id.stop_result);
        mWalkMeter = (TextView)activity.findViewById(R.id.work_meter_result);
    }

    public void showStartPoint(Location location) {
        StringBuffer address = new StringBuffer();
        makeAddressString(address, location);
        mStartPoint.setText(address);
    }

    public void showStopPoint(Location location) {
        StringBuffer address = new StringBuffer();
        makeAddressString(address, location);
        mStopPoint.setText(address);
    }

    public void showWalkCount(int count) {
        mWalkMeter.setText(Integer.toString(count));
    }

    public void makeAddressString(StringBuffer buffer, Location location) {
        try {
            List<Address> addressList =
                    mGeocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(),
                            1);
            for(Address address: addressList) {
                int index = address.getMaxAddressLineIndex();
                for(int i = 1; i <= index; i++) {
                    buffer.append(address.getAddressLine(i));
                }
            }

        } catch(IOException e) {}
    }
}
