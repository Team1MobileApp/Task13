/*
 * Author: Qianwen Li
 * Date: 5/11/2015
 * 
 * */

package databeans;

public class Prediction {
	private String routeId;
	private String routeName;
	private String stopName;
	private String predictTime;
	private String waitTime;
	private String direction;
	private String busNumber;
	private double lat;
	private double lon;
	
	public Prediction(String routeId, String routeName, String stopName, String predictTime, String waitTime, String direction, String busNumber,
			double lat, double lon) {
		this.routeId = routeId;
		this.routeName = routeName;
		this.stopName = stopName;
		this.predictTime = predictTime;
		this.waitTime = waitTime;
		this.direction = direction;
		this.busNumber = busNumber;
		this.lat = lat;
		this.lon = lon;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
 	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getPredictTime() {
		return predictTime;
	}
	public void setPredictTime(String predictTime) {
		this.predictTime = predictTime;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getBusNumber() {
		return busNumber;
	}
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public String toString() {
		return routeId + " ," + predictTime + ", " + lat + ", " + lon + "\n"; 
	}
}
