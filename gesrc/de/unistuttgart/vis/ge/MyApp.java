package de.unistuttgart.vis.ge;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;

public class MyApp extends PApplet {
	// map objects
	UnfoldingMap mainMap;
	UnfoldingMap subMap;

	private List<Feature> countries = null;

	public void settings() {
		size(1920, 1020, P2D);
	}

	public static final int COLOR_DEFAULT_MARKER = 0x90FFFF00;
	public static final int COLOR_SELECTED_MARKER = 0x30FF00FF;
	public static final int COLOR_SELECTED_MARKER_BORDER = 0xFFFF000;

	public void setup() {
		// create map
		mainMap = new UnfoldingMap(this, "leftMap", 0, 10, 870, 1000, true, false, null);
		List<Feature> world = GeoJSONReader.loadData(this, "world/continent.geo.json");
		List<Marker> worldMarkers = MapUtils.createSimpleMarkers(world);

		// Set colors of Markers
		for (Marker marker : worldMarkers) {
			marker.setColor(COLOR_DEFAULT_MARKER);
			marker.setHighlightColor(COLOR_SELECTED_MARKER);
			marker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
		}

		mainMap.addMarkers(worldMarkers);
		mainMap.zoomToLevel(2);

		// Location location1 = new Location(54.456, 14.987);
		// map2.zoomAndPanTo(location1, 2);

		// right map drawing //

		subMap = new UnfoldingMap(this, "rightMap", 880, 10, 600, 1000, true, false, null);
		countries = GeoJSONReader.loadData(this, "asia/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		subMap.addMarkers(countryMarkers);
		subMap.zoomToLevel(2);

		MapUtils.createDefaultEventDispatcher(this, mainMap, subMap);
	}

	public void mouseClicked() {
		if (mouseOnMainMap) {
			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				String message = marker.getLocation().toString();
				message += ", ID:" + marker.getId();
				message += ", Name:" + marker.getProperty("name");

				// update rightMap
				countries = GeoJSONReader.loadData(this, getContinent(marker.getId()));
				List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);

				// rightMap marker selection process//
				for (Marker rightMapMarker : countryMarkers) {
					rightMapMarker.setColor(COLOR_DEFAULT_MARKER);
					rightMapMarker.setHighlightColor(COLOR_SELECTED_MARKER);
					rightMapMarker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
				}

				subMap.getMarkers().clear();
				subMap.addMarkers(countryMarkers);
				subMap.panTo(marker.getLocation());
				// subMap.zoomToLevel(2);

				// set zoom level according to location of continent

				if (marker.getId().equals("AUS")) {
					Location location1 = new Location(-24.456, 137.987);
					subMap.zoomAndPanTo(location1, 4);
				} else if (marker.getId().equals("AFC")) {
					Location location1 = new Location(9.456, 23.987);
					subMap.zoomAndPanTo(location1, 3);
				} else if (marker.getId().equals("ASA")) {
					Location location1 = new Location(52.456, 94.987);
					subMap.zoomAndPanTo(location1, 3);
				} else if (marker.getId().equals("EUR")) {
					Location location1 = new Location(60.456, 8.987);
					subMap.zoomAndPanTo(location1, 3);
				} else if (marker.getId().equals("NAM")) {
					Location location1 = new Location(65.456, -99.987);
					subMap.zoomAndPanTo(location1, 3);
				} else if (marker.getId().equals("SAM")) {
					Location location1 = new Location(-15.456, -59.987);
					subMap.zoomAndPanTo(location1, 4);
				} else if (marker.getId().equals("ATC")) {
					Location location1 = new Location(-62.456, 17.987);
					subMap.zoomAndPanTo(location1, 3);
				}

			}
		} else if (mouseOnSubMap) {
			Marker marker = subMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				String message = marker.getLocation().toString();
				message += ", ID:" + marker.getId();
				message += ", Name:" + marker.getProperty("name");
				
				System.out.println(message);
			}

		}
	}

	private boolean mouseOnMainMap = false;
	private boolean mouseOnSubMap = false;

	private boolean isPositionInsideRectangle(int x, int y, ScreenPosition topLeft, ScreenPosition bottomRight) {
		if (x >= topLeft.x && x <= bottomRight.x && y >= topLeft.y && y <= bottomRight.y) {
			return true;
		}
		return false;
	}

	// mouseMoved method of PApplet
	public void mouseMoved() {
		ScreenPosition screenPositionTopLeftMainMap = mainMap.getScreenPosition(mainMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRighttMainMap = mainMap.getScreenPosition(mainMap.getBottomRightBorder());
		ScreenPosition screenPositionTopLeftSubMap = subMap.getScreenPosition(subMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRightSubMap = subMap.getScreenPosition(subMap.getBottomRightBorder());

		mouseOnMainMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftMainMap,
				screenPositionBottomRighttMainMap);
		mouseOnSubMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftSubMap,
				screenPositionBottomRightSubMap);

		// System.out.println(mouseX + ", " + mouseY + ", " + mouseOnMainMap +
		// ", " + mouseOnSubMap);

		if (mouseOnMainMap) {
			// Deselect All Markers
			for (Marker marker : mainMap.getMarkers()) {
				marker.setSelected(false);
				marker.setStrokeWeight(1);
			}

			// Get Selected Marker
			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
				marker.setStrokeWeight(3);
			}
		} else if (mouseOnSubMap) {
			// Deselect All Markers
			for (Marker marker : subMap.getMarkers()) {
				marker.setSelected(false);
				marker.setStrokeWeight(1);
			}

			// Get Selected Marker
			Marker marker = subMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
				marker.setStrokeWeight(3);
			}
		}
	}

	public void draw() {
		background(300);

		mainMap.draw();
		subMap.draw();

		// Draw lat, long information
		fill(300, 0, 0, 500); // mouse pointer colour filling
		noFill();
		if (mouseOnMainMap) {
			Location location = mainMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
		} else if (mouseOnSubMap) {
			Location location = subMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MyApp.class.getName() });
	}

	// Popup continent on righMap when click over continent of leftMap //
	Location location1 = new Location(15.456, 20.987);

	public String getContinent(String continentId) {
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

	public String getZoom(String continentId) {
		if (continentId.equals("AFC")) {
			Location location1 = new Location(15.456, 20.987);
			subMap.zoomAndPanTo(location1, 3);
		}
		return continentId;
	}

}
