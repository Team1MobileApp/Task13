package databeans;

public class Prediction {
	private String routeName;
	private String routeId;
	private String time;
	private String busNumber;
	private String lat;
	private String lon;
	
	public Prediction() {
		this("", "", "", "", "", "");
	}
	public Prediction(String routeId, String routeName, String time, String busNumber,
			String lat, String lon) {
		this.routeName = routeName;
		this.routeId = routeId;
		this.time = time;
		this.busNumber = busNumber;
		this.lat = lat;
		this.lon = lon;
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
	public String getBusNumber() {
		return busNumber;
	}
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	
	public String toString() {
		return routeId + " ," + time + ", " + lat + ", " + lon + "\n"; 
	}
}
