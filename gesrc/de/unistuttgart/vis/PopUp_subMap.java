package de.unistuttgart.vis;

public class PopUp_subMap {

	public static String getContinent(String continentId) {
		if (continentId.equals("AFC")) {
			return "africa/countries.geo.json";
		} else if (continentId.equals("ASA")) {
			return "asia/countries.geo.json";
		} else if (continentId.equals("AUS")) {
			return "australia/countries.geo.json";
		} else if (continentId.equals("EUR")) {
			return "europe/countries.geo.json";
		} else if (continentId.equals("NAM")) {
			return "northAmerica/countries.geo.json";
		} else if (continentId.equals("SAM")) {
			return "southAmerica/countries.geo.json";
		} else if (continentId.equals("ATC")) {
			return "antarctica/countries.geo.json";
		}
		return null;
	}

}
