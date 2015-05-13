package databeans;

/*It indicates a certain step's type(walk|bus), start position, end position
 * bus route, full route name, direction and duration*/
public class RouteStep {
    public StepType Type;
    public RouteLocation StartPos, EndPos;
    public String BusRoute, FullRouteName, HeadSign;
    public int Duration;
}
