/******************************
 * Author: Biqiu Li
 * Date: 2015/05/09
 ******************************/
package databeans;

import java.util.ArrayList;
import java.util.List;

/* This class contains a certain route's each steps
 * summary and estimatedArrivalTime are used for frontend display*/
public class CandidateRoute {
    public List<RouteStep> Steps;
    private List<WayPoint> wayPoints;

    private String summary;
    private String estimatedArrivalTime;

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String sum) {
    }

    public String getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(String time) {
        estimatedArrivalTime = time;
    }

    public CandidateRoute() {
        Steps = new ArrayList<RouteStep>();
    }

    public void ComputeSummary() {
        wayPoints = new ArrayList<WayPoint>();
        StringBuilder buses = new StringBuilder();
        buses.append("Via ");
        boolean first = true;
        boolean containsBus = false;
        for (RouteStep s : Steps) {
            if (s.BusRoute != null) {
                if (!first)
                    buses.append(", ");
                buses.append(s.BusRoute);
                first = false;
                containsBus = true;
            }
        }
        if (containsBus)
            summary = buses.toString();
        else
            summary = "Walk";

    }
}
