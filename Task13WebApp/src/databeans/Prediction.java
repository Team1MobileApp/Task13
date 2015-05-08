package databeans;

public class Prediction {
	private String routeName;
	private String routeId;
	private String time;
	
	public Prediction() {
		this("", "", "");
	}
	public Prediction(String routeId, String routeName, String time) {
		this.routeName = routeName;
		this.routeId = routeId;
		this.time = time;
	}
	
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
 	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String toString() {
		return routeId + " ," + time; 
	}
}
