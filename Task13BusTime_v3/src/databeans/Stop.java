/*
 * Author: Qianwen Li
 * Date: 5/11/2015
 * 
 * */

package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Stop {
	private int id;
	private String stopName;
	private String direction;
	private String stopId;
	private String routeId;
	private double stopLat;
	private double stopLon;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public double getStopLat() {
		return this.stopLat;
	}

	public void setStopLat(double stopLat) {
		this.stopLat = stopLat;
	}
	public double getStopLon() {
		return this.stopLon;
	}
	public void setStopLon(double stopLon) {
		this.stopLon = stopLon;
	}
}
