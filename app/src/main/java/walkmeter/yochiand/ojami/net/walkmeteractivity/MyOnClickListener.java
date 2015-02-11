package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.view.View;

/**
 *
 * Created by momomo00 on 15/01/29.
 */
public final class MyOnClickListener
    implements View.OnClickListener {
    // シングルトン
    private final static MyOnClickListener mInstance = new MyOnClickListener();
    private MyOnClickListener() {}
    public static MyOnClickListener getInstance() {
        return mInstance;
    }

    private static MainClickListener mMainClickListener = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                // メイン画面： 表示ボタン
                mMainClickListener.onStartClick(v);
                break;
            case R.id.button_stop:
                // メイン画面： 停止ボタン
                mMainClickListener.onStopClick(v);
                break;
        }
    }

    public static void setMainClickListener(MainClickListener listener) {
        mMainClickListener = listener;
    }
}
