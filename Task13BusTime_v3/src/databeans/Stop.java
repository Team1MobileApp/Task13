package databeans;
import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Stop {
	private int id;
	private String bound;
	private String stopName;
	private String stopId;

	public Stop(String bound, String stopName, String stopId) {
		this.bound = bound;
		this.stopName = stopName;
		this.stopId = stopId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBound() {
		return bound;
	}
	public void setBound(String bound) {
		this.bound = bound;
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

}
