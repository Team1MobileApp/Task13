package databeans;


import org.genericdao.PrimaryKey;

@PrimaryKey("stopName")
public class Stop {
	private String stopName;
	private String stopId;
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
}
