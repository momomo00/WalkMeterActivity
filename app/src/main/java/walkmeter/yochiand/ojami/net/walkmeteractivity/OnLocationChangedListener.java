package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.location.Location;

/**
 * 位置情報変化時の動作
 * Created by momomo00 on 15/02/03.
 */
public interface OnLocationChangedListener {
    /**
     * 位置情報変化時の動作
     * @param location 変更後の位置情報
     */
    abstract public void onLocationChanged(Location location);
}
