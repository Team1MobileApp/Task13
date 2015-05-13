package databeans;

public class WayPoint {
    private double longitute;
    private double latitute;
    private int type;
    public int getType()
    {
        return type;
    }
    public void setType(int val)
    {
        type = val;
    }
    public double getLongitute()
    {
        return longitute;
    }
    public double getLatitute()
    {
        return latitute;
    }
    public void setLongitute(double val)
    {
        longitute = val;
    }
    public void setLatitute(double val)
    {
        latitute = val;
    }
}
