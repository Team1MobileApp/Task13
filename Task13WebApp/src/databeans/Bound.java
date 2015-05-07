package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("routeName,stopName,boundName")
public class Bound {
	private String routeName;
	private String stopName;
	private String boundType;
	private String boundName;
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
	public String getBoundType() {
		return boundType;
	}
	public void setBoundType(String boundType) {
		this.boundType = boundType;
	}
	public String getBoundName() {
		return boundName;
	}
	public void setBoundName(String boundName) {
		this.boundName = boundName;
	}
}
