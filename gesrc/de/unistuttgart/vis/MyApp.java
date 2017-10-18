package de.unistuttgart.vis;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import edu.ucsd.sccn.EyePositionListener;
import edu.ucsd.sccn.TobiiEyePositionProvider;
import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GImageButton;
import g4p_controls.GLabel;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;
import processing.core.PApplet;

public class MyApp extends PApplet implements EyePositionListener {

	private String prev_country_name = "";
	private long startTime = 0;
	private long endTime = 0;

	// map objects
	UnfoldingMap mainMap;
	UnfoldingMap subMap;

	GLabel topNewsLabel;
	GLabel introductionLabel;
	GLabel governmentLabel;
	GLabel economyLabel;
	GLabel flagLabel;
	GLabel flagShowLabel;

	GTextArea topNewsTextArea;
	GTextArea introductionTextArea;
	GTextArea governmentTextArea;
	GTextArea economyTextArea;
	GTextArea countryFlagTextArea;
	GTextField searchTextField;
	GButton searchButton;
	GButton flagButton;
	GImageButton flagDisplay;
	GTextArea telephonSystemTextArea;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// Insets frameInsets = getInsets();

	int frameWidth = screenSize.width;
	int frameHeight = screenSize.height;

	private List<Feature> countries = null;

	@Override
	public void settings() {

		size(frameWidth, frameHeight - 50, P2D);

	}

	public static final int COLOR_DEFAULT_MARKER = 0x90FFFF00;
	public static final int COLOR_SELECTED_MARKER = 0x30FF00FF;
	public static final int COLOR_SELECTED_MARKER_BORDER = 0xFFFF000;

	@Override
	public void setup() {

		// GUI design

		// Top News
		topNewsLabel = new GLabel(this, 1660, 0, 300, 30);
		topNewsLabel.setText("Top News");
		topNewsLabel.setTextBold();
		topNewsTextArea = new GTextArea(this, 1480, 30, 430, 175, 5);

		// topNewsTextArea.setTextEditEnabled(true);

		// Introduction
		introductionLabel = new GLabel(this, 1660, 215, 300, 30);
		introductionLabel.setText("Introduction");
		introductionLabel.setTextBold();
		introductionTextArea = new GTextArea(this, 1480, 245, 430, 175, 5);

		// Government
		governmentLabel = new GLabel(this, 1660, 420, 300, 30);
		governmentLabel.setText("Government");
		governmentLabel.setTextBold();
		governmentTextArea = new GTextArea(this, 1480, 450, 430, 175, 5);

		// Economy
		economyLabel = new GLabel(this, 1660, 625, 300, 30);
		economyLabel.setText("Economy");
		economyLabel.setTextBold();
		economyTextArea = new GTextArea(this, 1480, 655, 430, 175, 5);

		// Flag
		flagLabel = new GLabel(this, 1660, 830, 300, 30);
		flagLabel.setText("Telephon System");
		flagLabel.setTextBold();
		telephonSystemTextArea = new GTextArea(this, 1480, 860, 430, 140, 5);

		// flagShowLabel = new GLabel(this, 1660, 860, 300, 150);
		// flagShowLabel.setText("my flag");
		// flagShowLabel.setIcon("Desktop//ag-lgflag.gif", 1, GAlign.LEFT,
		// GAlign.RIGHT);

		// flagShowLabel.setIcon("D://Saied//Infotech//Thesis//Helping
		// tools//Flag//ag-lgflag.gif", frameHeight, null, null, null);
		// flagLabel.setIcon("ag-lgflag.gif", 1, null, null);

		// searchButton = new GButton(this, 1480, 970, 430, 20);
		// searchButton.setText("SearchBTN");
		// searchButton.fireAllEvents(false);
		// searchButton.addEventHandler(this, "button_clicked");
		//
		// searchTextField = new GTextField(this, 1480, 990, 430, 20);
		// searchTextField.setText("search here...");

		// create mainMap
		mainMap = new UnfoldingMap(this, "mainMap", 0, 0, (frameWidth / 5 * 2), frameHeight, true, false, null);
		List<Feature> world = GeoJSONReader.loadData(this, "world/continent.geo.json");
		List<Marker> worldMarkers = MapUtils.createSimpleMarkers(world);

		// Set colors of Markers
		for (Marker marker : worldMarkers) {
			marker.setColor(COLOR_DEFAULT_MARKER);
			marker.setHighlightColor(COLOR_SELECTED_MARKER);
			marker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
		}

		mainMap.addMarkers(worldMarkers);
		// mainMap.zoomToLevel(2);
		Location location1 = new Location(0.456, 14.987);
		mainMap.zoomAndPanTo(location1, 2);

		// create subMap

		subMap = new UnfoldingMap(this, "subMap", (frameWidth / 5 * 2 + 10), 2, frameWidth / 5 * 2 - 70, frameHeight,
				true, false, null);
		countries = GeoJSONReader.loadData(this, "asia/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		subMap.addMarkers(countryMarkers);
		subMap.zoomToLevel(2);

		MapUtils.createDefaultEventDispatcher(this, mainMap, subMap);

		// Start gaze tracking
		TobiiEyePositionProvider.addEyePositionListener(this);
		startListeningToEyeTracker();
	}

	public void startListeningToEyeTracker() {
		// create new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				TobiiEyePositionProvider.startListeningToGazeData(false);
			}
		}).start();
	}

	// display all informationn of a country by button click

	public void button_clicked(GButton button, GEvent event) {
		DBConnection connectToDb = new DBConnection();
		ResultSet resultSet = null;

		resultSet = connectToDb.find(searchTextField.getText());

		try {

			if (button == searchButton && event == GEvent.CLICKED) {

				resultSet.next();
				topNewsTextArea.setText(resultSet.getString("top_news"));
				introductionTextArea.setText(resultSet.getString("introduction"));
				governmentTextArea.setText(resultSet.getString("government"));
				economyTextArea.setText(resultSet.getString("economy"));
				telephonSystemTextArea.setText(resultSet.getString("telephon_system"));

				// byte[] img = resultSet.getBytes("country_flag");
				// ImageIcon image = new ImageIcon(img);
				// PImage image = new PImage();

				// Image im = image.getImage();
				// PIcon im = image.get();
				//
				// Image myImage =
				// im.getScaledInstance(430,60,Image.SCALE_SMOOTH);
				// ImageIcon newImage = new ImageIcon(myImage);
				// flagDisplayLabel.setIconPos(newImage);

			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}

	}

	// get all information of a country when click on a country on subMap;

	public void get_country_info(String country_name) {

		DBConnection connectToDb = new DBConnection();
		ResultSet resultSet = null;

		resultSet = connectToDb.find(country_name);

		try {

			while (resultSet.next()) {
				topNewsTextArea.setText(resultSet.getString("top_news"));
				introductionTextArea.setText(resultSet.getString("introduction"));
				governmentTextArea.setText(resultSet.getString("government"));
				economyTextArea.setText(resultSet.getString("economy"));
				telephonSystemTextArea.setText(resultSet.getString("telephon_system"));

				// flagShowLabel.setIcon("D://Saied//Infotech//Thesis//Helping
				// tools//Flag//ag-lgflag.gif", 1, GAlign.LEFT,GAlign.RIGHT);

				// flagDisplay.setImage(resultSet.getBlob("country_flag"));
				// countryFlagTextArea.setText(resultSet.getBlob("country_flag"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public void updateSubmap(Marker mainMapMarker) {
		if (mainMapMarker != null) {
			String message = mainMapMarker.getLocation().toString();
			message += ", ID:" + mainMapMarker.getId();
			message += ", Name:" + mainMapMarker.getProperty("name");

			System.out.println(message);

			// update subMap
			countries = GeoJSONReader.loadData(this, PopUp_subMap.getContinent(mainMapMarker.getId()));
			List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);

			// subMap marker selection process//
			for (Marker subMapMarker : countryMarkers) {
				subMapMarker.setColor(COLOR_DEFAULT_MARKER);
				subMapMarker.setHighlightColor(COLOR_SELECTED_MARKER);
				subMapMarker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
			}

			subMap.getMarkers().clear();
			subMap.addMarkers(countryMarkers);
			subMap.panTo(mainMapMarker.getLocation());
			// subMap.zoomToLevel(2);

			// set zoom level according to location of continent

			if (mainMapMarker.getId().equals("AUS")) {
				Location location1 = new Location(-24.456, 137.987);
				subMap.zoomAndPanTo(location1, 4);
			} else if (mainMapMarker.getId().equals("AFC")) {
				Location location1 = new Location(9.456, 16.987);
				subMap.zoomAndPanTo(location1, 4);
			} else if (mainMapMarker.getId().equals("ASA")) {
				Location location1 = new Location(52.456, 94.987);
				subMap.zoomAndPanTo(location1, 3);
			} else if (mainMapMarker.getId().equals("EUR")) {
				Location location1 = new Location(60.456, 8.987);
				subMap.zoomAndPanTo(location1, 4);
			} else if (mainMapMarker.getId().equals("NAM")) {
				Location location1 = new Location(65.456, -99.987);
				subMap.zoomAndPanTo(location1, 3);
			} else if (mainMapMarker.getId().equals("SAM")) {
				Location location1 = new Location(-15.456, -59.987);
				subMap.zoomAndPanTo(location1, 4);
			} else if (mainMapMarker.getId().equals("ATC")) {
				Location location1 = new Location(-62.456, 17.987);
				subMap.zoomAndPanTo(location1, 3);
			}

		}
	}

	@Override
	public void mouseClicked() {
		if (mouseOnMainMap) {
			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
			updateSubmap(marker);
		} else if (mouseOnSubMap) {// get country name and ID, if click on
									// subMap's country
			Marker marker = subMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				String message = marker.getLocation().toString();
				message += ", ID:" + marker.getId();
				message += ", Name:" + marker.getProperty("name");

				// print country information on console
				System.out.println(message);

				// print country information on text area
				get_country_info(marker.getProperty("name").toString());
			}

		}
	}

	// mouse clicking
	private boolean mouseOnMainMap = false;
	private boolean mouseOnSubMap = false;
	// eye tracking
	private boolean eyeOnMainMap = false;
	private boolean eyeOnSubMap = false;

	private boolean isPositionInsideRectangle(int x, int y, ScreenPosition topLeft, ScreenPosition bottomRight) {
		if (x >= topLeft.x && x <= bottomRight.x && y >= topLeft.y && y <= bottomRight.y) {
			return true;
		}
		return false;
	}

	private final int LOCATION_TO_KEEP_IN_RECORD = 150;
	// need some explanation from R
	private LinkedHashMap mainMapLocationQueueForEye = new LinkedHashMap<Long, Marker>() {
		@Override
		protected boolean removeEldestEntry(Map.Entry<Long, Marker> eldest) {
			return this.size() > LOCATION_TO_KEEP_IN_RECORD;
		}
	};

	// mouseMoved method of PApplet
	@Override
	public void mouseMoved() {
		mouseOrEyeUpdated(mouseX, mouseY, true);
	}

	Marker eyeSelectedMareker;

	public void mouseOrEyeUpdated(final int X, final int Y, boolean isMouse) {
		if (mainMap == null || subMap == null) {
			return;
		}

		ScreenPosition screenPositionTopLeftMainMap = mainMap.getScreenPosition(mainMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRighttMainMap = mainMap.getScreenPosition(mainMap.getBottomRightBorder());
		ScreenPosition screenPositionTopLeftSubMap = subMap.getScreenPosition(subMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRightSubMap = subMap.getScreenPosition(subMap.getBottomRightBorder());

		boolean onMainMap = isPositionInsideRectangle(X, Y, screenPositionTopLeftMainMap,
				screenPositionBottomRighttMainMap);
		boolean onSubMap = isPositionInsideRectangle(X, Y, screenPositionTopLeftSubMap,
				screenPositionBottomRightSubMap);

		if (isMouse) {
			mouseOnMainMap = onMainMap;
			mouseOnSubMap = onSubMap;
		} else {
			eyeOnMainMap = onMainMap;
			eyeOnSubMap = onSubMap;
			// System.out.println(eyeOnMainMap + " " + eyeOnSubMap + " " + X + "
			// " + Y);
		}

		// show message of mouse movement
		// System.out.println(mouseX + ", " + mouseY + ", " + mouseOnMainMap +
		// ", " + mouseOnSubMap);

		// selection with mouse
		if (mouseOnMainMap) {
			// Deselect All Markers
			for (Marker marker : mainMap.getMarkers()) {
				marker.setSelected(false);
				marker.setStrokeWeight(1);
			}

			// Get Selected Marker
			Marker marker = mainMap.getFirstHitMarker(X, Y);
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
			Marker marker = subMap.getFirstHitMarker(X, Y);
			if (marker != null) {
				marker.setSelected(true);
				marker.setStrokeWeight(3);
			}
		}

		// selection with EyeTracker
		if (eyeOnMainMap) {
			// Deselect All Markers
			for (Marker marker : mainMap.getMarkers()) {
				if (eyeSelectedMareker != null && !eyeSelectedMareker.getId().equals(marker.getId())) {
					marker.setSelected(false);
					marker.setStrokeWeight(1);
				}
			}

			// Get Selected Marker
			Marker marker = mainMap.getFirstHitMarker(X, Y);
			if (marker != null) {
				// insert the marker with time stamp
				mainMapLocationQueueForEye.put(System.currentTimeMillis(), marker);

				int totalMarker = mainMapLocationQueueForEye.size();
				int sameMarker = 0;
				for (Object obj : mainMapLocationQueueForEye.values()) {
					Marker m = (Marker) obj;
					if (!m.getId().equals(marker.getId())) {
						sameMarker++;
					}
				}

				// 80 % time on same marker
				if (((sameMarker * 100.0) / totalMarker) > 80) {
					eyeSelectedMareker = marker;
					marker.setSelected(true);
					marker.setStrokeWeight(3);

					// update subMap by looking
					updateSubmap(marker);

				}
			}

		} else if (eyeOnSubMap) {
			// Deselect All Markers
			for (Marker marker : subMap.getMarkers()) {
				marker.setSelected(false);
				marker.setStrokeWeight(1);
			}

			// Get Selected Marker
			Marker marker = subMap.getFirstHitMarker(X, Y);
			if (marker != null) {
				marker.setSelected(true);
				marker.setStrokeWeight(3);
				// marker.wait(timeout =100);
				
				
				// update country information by eye tracker, first time display instantly but if changing country name it takes 5 second
				if (!prev_country_name.equals(marker.getProperty("name").toString())
						&& (((endTime - startTime) / 1000) >= 3) || (endTime == 0 && startTime == 0)) {
					startTime = System.currentTimeMillis();
					get_country_info(marker.getProperty("name").toString()); // get country information
					prev_country_name = marker.getProperty("name").toString(); // check if already connected to db or not
				}
				endTime = System.currentTimeMillis();

			}
		}
	}

	@Override
	public void draw() {
		background(200);

		mainMap.draw();
		try {
			subMap.draw();
		} catch (Exception e) {
			// TODO: handle exception
		}

		topNewsLabel.draw();
		topNewsTextArea.draw();
		introductionLabel.draw();
		introductionTextArea.draw();
		governmentLabel.draw();
		governmentTextArea.draw();
		economyLabel.draw();
		economyTextArea.draw();
		flagLabel.draw();
		// flagShowLabel.draw();
		// searchButton.draw();
		// searchTextField.draw();
		telephonSystemTextArea.draw();

		// Draw lat, long information
		fill(300, 0, 0, 500); // mouse pointer colour filling
		noFill();
		if (mouseOnMainMap) {
			Location location = mainMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
			// System.out.println("Data: " + Double.toString(mouseX) + ", " +
			// Double.toString(mouseY));
			// Location elocation = mainMap.getLocation(739, 279);
			// System.out.println(elocation.toString());

		} else if (mouseOnSubMap) {
			Location location = subMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
		}

	}

	private String button_clicked(String string) {
		return null;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MyApp.class.getName() });
		MyApp myapp = new MyApp();
	}

	@Override
	public void eyePositionUpdated(double x, double y) {
		// System.out.println(".....Hello from MyAppp, eyePositionUpdated");
		// System.out.println(x + ", " + y);
		mouseOrEyeUpdated((int) x, (int) y, false);
	}
}