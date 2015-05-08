package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Route {
	private int id;
	private String routeName;
	private String routeId;
	private String stopName;
	private String stopId;
	private String bound;
	
	public Route() {
		this("", "", "", "", "");
	}
	
	public Route(String routeId, String routeName, 
			String bound, String stopId, String stopName) {
		this.routeId = routeId;
		this.routeName = routeName;
		this.bound = bound;
		this.stopId = stopId;
		this.stopName = stopName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getStopId() {
		return stopId;
	}
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	public String getBound() {
		return bound;
	}
	public void setBound(String bound) {
		this.bound = bound;
	}
	
	public String toString() {
		return routeId + ", " + routeName + ", " + bound + ", " + stopId
				+ ", " + stopName + "\n";
	}
}
