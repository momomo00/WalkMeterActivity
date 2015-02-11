package walkmeter.yochiand.ojami.net.walkmeteractivity;

/**
 * Created by momomo00 on 15/02/06.
 */
public class WalkMeterData {
    public String   StartDate;
    public Double   StartLatitude;
    public Double   StartLongitude;
    public String   StartAddress;

    public String   StopDate;
    public Double   StopLatitude;
    public Double   StopLongitude;
    public String   StopAddress;

    public int      WalkCount;

    public WalkMeterData() {
        StartDate = "";
        StartLatitude = 0.0d;
        StartLongitude = 0.0d;
        StartAddress = "";

        StopDate = "";
        StopLatitude = 0.0d;
        StopLongitude = 0.0d;
        StopAddress = "";

        WalkCount = 0;
    }
}
