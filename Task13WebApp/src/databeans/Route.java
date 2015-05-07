package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("routeName")
public class Route {
	private String routeName;
	private String routeId;
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
}
