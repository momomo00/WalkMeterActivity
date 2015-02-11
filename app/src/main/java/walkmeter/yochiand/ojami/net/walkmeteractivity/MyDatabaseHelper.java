package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by momomo00 on 15/02/04.
 */
public class MyDatabaseHelper
    extends SQLiteOpenHelper {
    private final static String DB_NAME = "MySQLite.db";
    private final static int VERSION = 1;

    private final static String TABLE_NAME = "WalkMeterDB";

    private final static String PRIMARY_ID = "PrimaryID";

    private final static String START_DATE = "StartDate";
    private final static String START_LATITUDE = "StartLatitude";
    private final static String START_LONGITUDE = "StartLongitude";
    private final static String START_ADDRESS = "StartAddress";

    private final static String STOP_DATE = "StopDate";
    private final static String STOP_LATITUDE = "StopLatitude";
    private final static String STOP_LONGITUDE = "StopLongitude";
    private final static String STOP_ADDRESS = "StopAddress";

    private final static String WALK_COUNT = "WorkCount";

    private final static String DATA_FORMAT = "yyyy'年'MM'月'dd'日'　kk'時'mm'分'ss'秒'";

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String exec =
                "CREATE TABLE " + TABLE_NAME + " (" +
                    PRIMARY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    START_DATE          + " INTEGER," +
                    START_LATITUDE      + " DOUBLE," +
                    START_LONGITUDE     + " DOUBLE," +
                    START_ADDRESS       + " TEXT," +
                    STOP_DATE           + " INTEGER," +
                    STOP_LATITUDE       + " DOUBLE," +
                    STOP_LONGITUDE      + " DOUBLE," +
                    STOP_ADDRESS        + " TEXT," +
                    WALK_COUNT          + " INTEGER" +
                        ")";
        db.execSQL(exec);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS WalkMeterDB");
        onCreate(db);
    }

    public void onTest() {
        String exec =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        PRIMARY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        START_DATE          + " TEXT," +
                        START_LATITUDE      + " DOUBLE," +
                        START_LONGITUDE     + " DOUBLE," +
                        START_ADDRESS       + " TEXT," +
                        STOP_DATE           + " TEXT," +
                        STOP_LATITUDE       + " DOUBLE," +
                        STOP_LONGITUDE      + " DOUBLE," +
                        STOP_ADDRESS        + " TEXT," +
                        WALK_COUNT          + " INTEGER" +
                        ")";
        Log.d("SQL", "MyDatabaseHelper: " + exec);
    }

    public void insertData(
            Date StartDate, Location startLocation,
            Date StopDate, Location stopLocation,
            int walkCount) {

        ContentValues cv = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);

        // 開始位置情報の設定
        cv.put(START_DATE, dateFormat.format(StartDate));
        cv.put(START_LATITUDE, startLocation.getLatitude());
        cv.put(START_LONGITUDE, startLocation.getLongitude());
        StringBuffer startAddress = new StringBuffer();
        Display.getInstance().makeAddressString(startAddress, startLocation);
        cv.put(START_ADDRESS, startAddress.toString());

        // 停止位置情報の設定
        cv.put(STOP_DATE, dateFormat.format(StopDate));
        cv.put(STOP_LATITUDE, stopLocation.getLatitude());
        cv.put(STOP_LONGITUDE, stopLocation.getLongitude());
        StringBuffer stopAddress = new StringBuffer();
        Display.getInstance().makeAddressString(stopAddress, stopLocation);
        cv.put(STOP_ADDRESS, stopAddress.toString());

        // 歩数情報の設定
        cv.put(WALK_COUNT, walkCount);

        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_NAME, null, cv);
        database.close();
    }

    public ArrayList<WalkMeterData> getAllData() {
        ArrayList<WalkMeterData> list = new ArrayList<WalkMeterData>();

        SQLiteDatabase database = getReadableDatabase();
        String[] cols = {START_DATE, START_LATITUDE, START_LONGITUDE, START_ADDRESS,
                            STOP_DATE, STOP_LATITUDE, STOP_LONGITUDE, STOP_ADDRESS,
                            WALK_COUNT};
        Cursor cursor = database.query(TABLE_NAME, cols, null, null, null, null, null, null);
        for(boolean isEof = cursor.moveToFirst(); isEof; isEof = cursor.moveToNext()) {
            WalkMeterData data = new WalkMeterData();

            data.StartDate = cursor.getString(cursor.getColumnIndex(START_DATE));
            data.StartLatitude = cursor.getDouble(cursor.getColumnIndex(START_LATITUDE));
            data.StartLongitude = cursor.getDouble(cursor.getColumnIndex(START_LONGITUDE));
            data.StartAddress = cursor.getString(cursor.getColumnIndex(START_ADDRESS));

            data.StopDate = cursor.getString(cursor.getColumnIndex(STOP_DATE));
            data.StopLatitude = cursor.getDouble(cursor.getColumnIndex(STOP_LATITUDE));
            data.StopLongitude = cursor.getDouble(cursor.getColumnIndex(STOP_LONGITUDE));
            data.StopAddress = cursor.getString(cursor.getColumnIndex(STOP_ADDRESS));

            data.WalkCount = cursor.getInt(cursor.getColumnIndex(WALK_COUNT));

            list.add(data);
        }

        cursor.close();
        database.close();

        return list;
    }
}
