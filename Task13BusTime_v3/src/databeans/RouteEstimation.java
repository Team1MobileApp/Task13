/******************************
 * Author: Biqiu Li
 * Date: 2015/05/09
 ******************************/
package databeans;

import java.util.Calendar;
import java.util.List;

/*This class contains steps that belongs to transit travel model
 * and the arrival time of each segment*/
public class RouteEstimation {
    public Calendar ArrivalTime;
    public List<SegmentEstimation> Segments;
}