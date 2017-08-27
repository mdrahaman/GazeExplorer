package de.unistuttgart.vis;

import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import edu.ucsd.sccn.EyePositionListener;
import edu.ucsd.sccn.Hello;
import edu.ucsd.sccn.TobiiEyePositionProvider;
import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GImageButton;
import g4p_controls.GLabel;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;
import g4p_controls.GTextIconBase;
import processing.core.PApplet;
import processing.core.PImage;

public class MyApp extends PApplet implements ActionListener {
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
		flagLabel.setText("Country Flag");

		flagShowLabel = new GLabel(this, 1660, 860, 300, 150);
		flagShowLabel.setText("my flag");
		flagShowLabel.setIcon("Desktop//ag-lgflag.gif", 1, GAlign.LEFT, GAlign.RIGHT);

		// flagShowLabel.setIcon("D://Saied//Infotech//Thesis//Helping
		// tools//Flag//ag-lgflag.gif", frameHeight, null, null, null);
		// flagLabel.setIcon("ag-lgflag.gif", 1, null, null);

		searchButton = new GButton(this, 1480, 970, 430, 20);
		searchButton.setText("SearchBTN");
		searchButton.fireAllEvents(false);
		searchButton.addEventHandler(this, "button_clicked");

		searchTextField = new GTextField(this, 1480, 990, 430, 20);
		searchTextField.setText("search field");

		// create map
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

		// right map drawing //

		subMap = new UnfoldingMap(this, "subMap", (frameWidth / 5 * 2 + 10),2, frameWidth / 5 * 2 - 70, frameHeight,
				true, false, null);
		countries = GeoJSONReader.loadData(this, "asia/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		subMap.addMarkers(countryMarkers);
		subMap.zoomToLevel(2);

		MapUtils.createDefaultEventDispatcher(this, mainMap, subMap);

		
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

			resultSet.next();
			topNewsTextArea.setText(resultSet.getString("top_news"));
			introductionTextArea.setText(resultSet.getString("introduction"));
			governmentTextArea.setText(resultSet.getString("government"));
			economyTextArea.setText(resultSet.getString("economy"));
			// flagShowLabel.setIcon("D://Saied//Infotech//Thesis//Helping
			// tools//Flag//ag-lgflag.gif", 1, GAlign.LEFT,GAlign.RIGHT);

			// flagDisplay.setImage(resultSet.getBlob("country_flag"));
			// countryFlagTextArea.setText(resultSet.getBlob("country_flag"));

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	class DBConnection {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		public ResultSet find(String s) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/worldinfo", "root", "1988");
				ps = con.prepareStatement("select * from worldinfo.project_db where country_name = ?");
				ps.setString(1, s);
				rs = ps.executeQuery();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}

	}

	@Override
	public void mouseClicked() {
		if (mouseOnMainMap) {
			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				String message = marker.getLocation().toString();
				message += ", ID:" + marker.getId();
				message += ", Name:" + marker.getProperty("name");

				// update subMap
				countries = GeoJSONReader.loadData(this, getContinent(marker.getId()));
				List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);

				// subMap marker selection process//
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
					Location location1 = new Location(9.456, 16.987);
					subMap.zoomAndPanTo(location1, 4);
				} else if (marker.getId().equals("ASA")) {
					Location location1 = new Location(52.456, 94.987);
					subMap.zoomAndPanTo(location1, 3);
				} else if (marker.getId().equals("EUR")) {
					Location location1 = new Location(60.456, 8.987);
					subMap.zoomAndPanTo(location1, 4);
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

			// get country name and ID, if click on subMap's country
		} else if (mouseOnSubMap) {
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

	// mouseMoved method of PApplet
	@Override
	public void mouseMoved() {
		ScreenPosition screenPositionTopLeftMainMap = mainMap.getScreenPosition(mainMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRighttMainMap = mainMap.getScreenPosition(mainMap.getBottomRightBorder());
		ScreenPosition screenPositionTopLeftSubMap = subMap.getScreenPosition(subMap.getTopLeftBorder());
		ScreenPosition screenPositionBottomRightSubMap = subMap.getScreenPosition(subMap.getBottomRightBorder());

		mouseOnMainMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftMainMap,
				screenPositionBottomRighttMainMap);
		mouseOnSubMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftSubMap,
				screenPositionBottomRightSubMap);

		eyeOnMainMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftMainMap,
				screenPositionBottomRighttMainMap);

		// show message of mouse movement
		
//		 System.out.println(mouseX + ", " + mouseY + ", " + mouseOnMainMap +
//		 ", " + mouseOnSubMap);

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

	@Override
	public void draw() {
		background(200);

		mainMap.draw();
		subMap.draw();
		topNewsLabel.draw();
		topNewsTextArea.draw();
		introductionLabel.draw();
		introductionTextArea.draw();
		governmentLabel.draw();
		governmentTextArea.draw();
		economyLabel.draw();
		economyTextArea.draw();
		flagLabel.draw();
		flagShowLabel.draw();
		searchButton.draw();
		searchTextField.draw();

		// Draw lat, long information
		fill(300, 0, 0, 500); // mouse pointer colour filling
		noFill();
		if (mouseOnMainMap) {
			Location location = mainMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
//			System.out.println("Data: " + Double.toString(mouseX) + ", " + Double.toString(mouseY));
//			Location elocation = mainMap.getLocation(739, 279);
//			System.out.println(elocation.toString());
			
		} else if (mouseOnSubMap) {
			Location location = subMap.getLocation(mouseX, mouseY);
			text("geoPosition:" + location.toString(), mouseX, mouseY);
		}

	}

	// Popup continent on subMap when click over continent of mainMap //
	// Location location1 = new Location(15.456, 20.987);

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
	// to displaay all information into Text Area when click over a country on
	// subMap;

	private String button_clicked(String string) {	
		
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		
	}

	// using eye tracker

	private static final String rexLSLStreamerLocation = "D:\\Saied\\Eclipse\\Workspace\\RexLSLStreamer\\RexLSLStreamer\\build\\Release\\rexLSLStreamer.exe";
	private static Process process = null;
	private static boolean debug = false;

	private static List<EyePositionListener> eyePositionListeners = new ArrayList<>();

	private static boolean start_rexLSLStreamer() {
		try {
			process = new ProcessBuilder(rexLSLStreamerLocation).start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void start_resLSLStreamer() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (process == null) {
						start_rexLSLStreamer();
					} else if (!process.isAlive()) {
						start_rexLSLStreamer();
					}

					try {
						Thread.sleep(100);
					} catch (Exception e) {
					}

					if (debug) {
						System.out.println("resLSLStreamer is running...? " + (process != null && process.isAlive()));
					}
				}
			}
		}).start();
	}

	public String startListeningToGazeData(boolean debug) {
		TobiiEyePositionProvider.debug = debug;

		start_resLSLStreamer();

		LSL.StreamInfo[] results = LSL.resolve_stream("type", "Gaze");
		// open an inlet
		LSL.StreamInlet inlet = new LSL.StreamInlet(results[0]);
		// receive data
		float[] sample;
		try {
			sample = new float[inlet.info().channel_count()];
			while (true) {
				inlet.pull_sample(sample);

				while (true) {
					inlet.pull_sample(sample);

					// Need to read documentation
					if (sample.length == 2) {
						if (debug) {
							
							System.out.println("Data: " + Double.toString(sample[0]) + ", " + Double.toString(sample[1]));
							
							if ((sample[0])>0 && (sample[0]) < (frameWidth / 5 * 2))								
							
							{
//								System.out.println("Data: " + Double.toString(mouseX) + ", " + Double.toString(mouseY));
								System.out.println("you are looking at mainMap");
								if(((sample[1]) > 194 && (sample[1]) < 498) &&((sample[0]) > 441 && (sample[0]) < 757))
								{
									System.out.println("this is Asia ");
									return "asia/countries.geo.json";
								}
								
								
//								Location elocation = mainMap.getLocation(mouseX, mouseY);
//								System.out.println(elocation.toString());
								
//								int x = (int)Math.round(sample[0]);
//								int y = (int)Math.round(sample[1]);
//								System.out.println("Data: " + Double.toString(x) + ", " + Double.toString(y));
////								Location location = mainMap.getLocation(mouseX, mouseY);
//								Location location = mainMap.getLocation((float)739,(float)279);
////								System.out.println(location.x);
////								System.out.println(eyelocation.toString());
////								text("geoPosition:" + location.toString(), mouseX, mouseY);
////								text("eyePosition:" + eyelocation.toString(), sample[0], sample[1]);
////								System.out.println("Data: " + Double.toString(x) + ", " + Double.toString(y));
//								
								
								
							} else if((sample[0]) > (frameWidth / 5 * 2 + 10) && (sample[0]) < (frameWidth / 5 * 4 - 70))
							{
								System.out.println("you are looking at subMap");
								
							} else {
								System.out.println("you are looking out of Map");
							}
							
							
							if((frameHeight > 194 && frameHeight < 498) &&(frameWidth > 441 && frameWidth < 757))
							{
								System.out.println("this is Asia ");
							}
								System.out.println(eyePositionListeners.size());
						}
						for (EyePositionListener listener : eyePositionListeners) {
							listener.eyePositionUpdated(sample[0], sample[1]);
							if (debug) {
								System.out.println(Double.toString(sample[0]) + ", " + Double.toString(sample[1]));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addEyePositionListener(EyePositionListener listener) {
		if (listener != null) {
			eyePositionListeners.add(listener);
		}
	}

	public interface EyePositionListener {
		public void eyePositionUpdated(double x, double y);
	}

	class MyClass implements EyePositionListener {

		@Override
		public void eyePositionUpdated(double x, double y) {
			System.out.println("Hello from MyClass, eyePositionUpdated");
			System.out.println(x + ", " + y);
		}
	}

	class MyOtherClass implements EyePositionListener {

		@Override
		public void eyePositionUpdated(double x, double y) {
			System.out.println("Hello from MyOtherClass, eyePositionUpdated");
			System.out.println(x + ", " + y);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MyApp.class.getName() });
		MyApp myapp = new MyApp();
		myapp.startListeningToGazeData(true);

	}
}
