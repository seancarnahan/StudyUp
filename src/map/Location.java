package map;

public class Location {

	private double lat;
	private double lon;

	public Location(double lat, double lon) {
		this.lat = lat;
		this.setLat(lat);
		this.lon = lon;
		this.setLon(lon);
		// TODO Auto-generated constructor stub
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

}
