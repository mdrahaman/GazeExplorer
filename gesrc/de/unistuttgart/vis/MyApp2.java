package de.unistuttgart.vis;
//
//package de.unistuttgart.vis.ge;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JOptionPane;
//import javax.swing.JTextField;
//
//import org.omg.CORBA.PUBLIC_MEMBER;
//
//import com.mysql.jdbc.Blob;
//
//import de.fhpotsdam.unfolding.UnfoldingMap;
//import de.fhpotsdam.unfolding.data.Feature;
//import de.fhpotsdam.unfolding.data.GeoJSONReader;
//import de.fhpotsdam.unfolding.geo.Location;
//import de.fhpotsdam.unfolding.marker.Marker;
//import de.fhpotsdam.unfolding.utils.MapUtils;
//import de.fhpotsdam.unfolding.utils.ScreenPosition;
//import de.unistuttgart.vis.ge.AppGUI.DBConnection;
//import edu.ucsd.sccn.EyePositionListener;
//import edu.ucsd.sccn.TobiiEyePositionProvider;
//import g4p_controls.GButton;
//import g4p_controls.GEvent;
//import g4p_controls.GImageButton;
//import g4p_controls.GLabel;
//import g4p_controls.GTextArea;
//import g4p_controls.GTextField;
//import processing.core.PApplet;
//import processing.core.PImage;
//import processing.core.PShapeSVG.Font;
//
//public class MyApp2 extends PApplet implements ActionListener, EyePositionListener {
//	// map objects
//	UnfoldingMap mainMap;
//	UnfoldingMap subMap;
//
//	GLabel topNewsLabel;
//	GLabel introductionLabel;
//	GLabel governmentLabel;
//	GLabel economyLabel;
//	GLabel flagLabel;
//	GLabel flagDisplayLabel;
//
//	GTextArea topNewsTextArea;
//	GTextArea introductionTextArea;
//	GTextArea governmentTextArea;
//	GTextArea economyTextArea;
//	GTextArea countryFlagTextArea;
//	GTextField searchTextField;
//	GButton searchButton;
//	GButton flagButton;
//	GImageButton flagDisplay;
//
//	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//	// Insets frameInsets = getInsets();
//	int frameWidth = screenSize.width;
//	int frameHeight = screenSize.height;
//
//	private List<Feature> countries = null;
//
//	public void settings() {
//
//		size(frameWidth, frameHeight, P2D);
//
//	}
//
//	public static final int COLOR_DEFAULT_MARKER = 0x90FFFF00;
//	public static final int COLOR_SELECTED_MARKER = 0x30FF00FF;
//	public static final int COLOR_SELECTED_MARKER_BORDER = 0xFFFF000;
//
//	public void setup() {
//
//		// Top News
//		topNewsLabel = new GLabel(this, 1660, 0, 300, 30);
//		topNewsLabel.setText("Top News");
//		topNewsLabel.setTextBold();
//		topNewsTextArea = new GTextArea(this, 1480, 30, 430, 175, 5);
//		// topNewsTextArea.setTextEditEnabled(true);
//
//		// Introduction
//		introductionLabel = new GLabel(this, 1660, 215, 300, 30);
//		introductionLabel.setText("Introduction");
//		introductionLabel.setTextBold();
//		introductionTextArea = new GTextArea(this, 1480, 245, 430, 175, 5);
//
//		// Government
//		governmentLabel = new GLabel(this, 1660, 420, 300, 30);
//		governmentLabel.setText("Government");
//		governmentLabel.setTextBold();
//		governmentTextArea = new GTextArea(this, 1480, 450, 430, 175, 5);
//
//		// Economy
//		economyLabel = new GLabel(this, 1660, 625, 300, 30);
//		economyLabel.setText("Economy");
//		economyLabel.setTextBold();
//		economyTextArea = new GTextArea(this, 1480, 655, 430, 175, 5);
//
//		// Flag
//		flagDisplay = new GImageButton(this, 1480, 860, 430, 60, loadImageFormats);
//		flagDisplay.setVisible(true);
//
//		// flagLabel = new GLabel(this, 1660, 830, 300, 30);
//		// flagLabel.setText("Country Flag");
//		// flagLabel.setTextBold();
//		// countryFlagTextArea = new GTextArea(this, 1480, 860, 430, 60);
//		flagDisplayLabel = new GLabel(this, 1480, 860, 430, 60);
//
//		searchButton = new GButton(this, 1480, 920, 430, 30);
//		searchButton.setText("SearchBTN");
//		searchButton.fireAllEvents(false);
//		searchButton.addEventHandler(this, "button_clicked");
//		// searchButton.addActionListener(this);
//
//		searchTextField = new GTextField(this, 1480, 960, 430, 30);
//		searchTextField.setText("search field");
//
//		// Flag
//		// flagLabel = new GLabel(this, 1660, 830, 300, 30);
//		// flagLabel.setText("Country Flag");
//		// flagLabel.setTextBold();
//		// flagButton = new GButton(this, 1480, 860, 430, 135);
//
//		// create map
//		mainMap = new UnfoldingMap(this, "mainMap", 0, 0, frameWidth / 5 * 2, frameHeight, true, false, null);
//		List<Feature> world = GeoJSONReader.loadData(this, "world/continent.geo.json");
//		List<Marker> worldMarkers = MapUtils.createSimpleMarkers(world);
//
//		// Set colors of Markers
//		for (Marker marker : worldMarkers) {
//			marker.setColor(COLOR_DEFAULT_MARKER);
//			marker.setHighlightColor(COLOR_SELECTED_MARKER);
//			marker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
//		}
//
//		mainMap.addMarkers(worldMarkers);
//		// mainMap.zoomToLevel(2);
//		Location location1 = new Location(0.456, 14.987);
//		mainMap.zoomAndPanTo(location1, 2);
//
//		// right map drawing //
//
//		subMap = new UnfoldingMap(this, "subMap", frameWidth / 5 * 2 + 10, 2, frameWidth / 5 * 2 - 70, frameHeight,
//				true, false, null);
//		countries = GeoJSONReader.loadData(this, "asia/countries.geo.json");
//		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
//		subMap.addMarkers(countryMarkers);
//		subMap.zoomToLevel(2);
//
//		MapUtils.createDefaultEventDispatcher(this, mainMap, subMap);
//		
////		TobiiEyePositionProvider.addEyePositionListener(this);
////		TobiiEyePositionProvider.startListeningToGazeData(true);
//	}
//	
//	@Override
////	public void eyePositionUpdated(double x, double y) {
////		System.out.println("Hello from MyApp, eyePositionUpdated");
////		System.out.println(x + ", " + y);
////	}
//
//	// display all informationn of a country by button click
//
//	public void button_clicked(GButton button, GEvent event) {
//		DBConnection connectToDb = new DBConnection();
//		ResultSet resultSet = null;
//
//		resultSet = connectToDb.find(searchTextField.getText());
//
//		try {
//
//			if (button == searchButton && event == GEvent.CLICKED) {
//
//				resultSet.next();
//				topNewsTextArea.setText(resultSet.getString("top_news"));
//				introductionTextArea.setText(resultSet.getString("introduction"));
//				governmentTextArea.setText(resultSet.getString("government"));
//				economyTextArea.setText(resultSet.getString("economy"));
//
//				byte[] img = resultSet.getBytes("country_flag");
//				// ImageIcon image = new ImageIcon(img);
//				PImage image = new PImage();
//
//				// Image im = image.getImage();
//				// PIcon im = image.get();
//				//
//				// Image myImage =
//				// im.getScaledInstance(430,60,Image.SCALE_SMOOTH);
//				// ImageIcon newImage = new ImageIcon(myImage);
//				// flagDisplayLabel.setIconPos(newImage);
//
//			}
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(null, ex.getMessage());
//		}
//
//	}
//
//	// get all information of a country when click on a country on subMap;
//
//	public void get_country_info(String country_name) {
//		DBConnection connectToDb = new DBConnection();
//		ResultSet resultSet = null;
//
//		resultSet = connectToDb.find(country_name);
//
//		try {
//
//			resultSet.next();
//			topNewsTextArea.setText(resultSet.getString("top_news"));
//			introductionTextArea.setText(resultSet.getString("introduction"));
//			governmentTextArea.setText(resultSet.getString("government"));
//			economyTextArea.setText(resultSet.getString("economy"));
//			// flagDisplay.setImage(resultSet.getBlob("country_flag"));
//
//			// countryFlagTextArea.setText(resultSet.getBlob("country_flag"));
//
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(null, ex.getMessage());
//		}
//	}
//
//	class DBConnection {
//		Connection con = null;
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//
//		public ResultSet find(String s) {
//			try {
//				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/worldinfo", "root", "1988");
//				ps = con.prepareStatement("select * from worldinfo.project_db where country_name = ?");
//				ps.setString(1, s);
//				rs = ps.executeQuery();
//
//			} catch (Exception ex) {
//				JOptionPane.showMessageDialog(null, ex.getMessage());
//			}
//			return rs;
//		}
//
//	}
//
//	public void mouseClicked() {
//		if (mouseOnMainMap) {
//			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
//			if (marker != null) {
//				String message = marker.getLocation().toString();
//				message += ", ID:" + marker.getId();
//				message += ", Name:" + marker.getProperty("name");
//
//				// update subMap
//				countries = GeoJSONReader.loadData(this, getContinent(marker.getId()));
//				List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
//
//				// subMap marker selection process//
//				for (Marker rightMapMarker : countryMarkers) {
//					rightMapMarker.setColor(COLOR_DEFAULT_MARKER);
//					rightMapMarker.setHighlightColor(COLOR_SELECTED_MARKER);
//					rightMapMarker.setHighlightStrokeColor(COLOR_SELECTED_MARKER_BORDER);
//				}
//
//				subMap.getMarkers().clear();
//				subMap.addMarkers(countryMarkers);
//				subMap.panTo(marker.getLocation());
//				// subMap.zoomToLevel(2);
//
//				// set zoom level according to location of continent
//
//				if (marker.getId().equals("AUS")) {
//					Location location1 = new Location(-24.456, 137.987);
//					subMap.zoomAndPanTo(location1, 4);
//				} else if (marker.getId().equals("AFC")) {
//					Location location1 = new Location(9.456, 16.987);
//					subMap.zoomAndPanTo(location1, 4);
//				} else if (marker.getId().equals("ASA")) {
//					Location location1 = new Location(52.456, 94.987);
//					subMap.zoomAndPanTo(location1, 3);
//				} else if (marker.getId().equals("EUR")) {
//					Location location1 = new Location(60.456, 8.987);
//					subMap.zoomAndPanTo(location1, 4);
//				} else if (marker.getId().equals("NAM")) {
//					Location location1 = new Location(65.456, -99.987);
//					subMap.zoomAndPanTo(location1, 3);
//				} else if (marker.getId().equals("SAM")) {
//					Location location1 = new Location(-15.456, -59.987);
//					subMap.zoomAndPanTo(location1, 4);
//				} else if (marker.getId().equals("ATC")) {
//					Location location1 = new Location(-62.456, 17.987);
//					subMap.zoomAndPanTo(location1, 3);
//				}
//
//			}
//		} else if (mouseOnSubMap) {
//			Marker marker = subMap.getFirstHitMarker(mouseX, mouseY);
//			if (marker != null) {
//				String message = marker.getLocation().toString();
//				message += ", ID:" + marker.getId();
//				message += ", Name:" + marker.getProperty("name");
//				// print countr information on console
//				System.out.println(message);
//				// print countr information on text area
//				get_country_info(marker.getProperty("name").toString());
//			}
//
//		}
//	}
//
//	private boolean mouseOnMainMap = false;
//	private boolean mouseOnSubMap = false;
//
//	private boolean isPositionInsideRectangle(int x, int y, ScreenPosition topLeft, ScreenPosition bottomRight) {
//		if (x >= topLeft.x && x <= bottomRight.x && y >= topLeft.y && y <= bottomRight.y) {
//			return true;
//		}
//		return false;
//	}
//
//	// mouseMoved method of PApplet
//	public void mouseMoved() {
//		ScreenPosition screenPositionTopLeftMainMap = mainMap.getScreenPosition(mainMap.getTopLeftBorder());
//		ScreenPosition screenPositionBottomRighttMainMap = mainMap.getScreenPosition(mainMap.getBottomRightBorder());
//		ScreenPosition screenPositionTopLeftSubMap = subMap.getScreenPosition(subMap.getTopLeftBorder());
//		ScreenPosition screenPositionBottomRightSubMap = subMap.getScreenPosition(subMap.getBottomRightBorder());
//
//		mouseOnMainMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftMainMap,
//				screenPositionBottomRighttMainMap);
//		mouseOnSubMap = isPositionInsideRectangle(mouseX, mouseY, screenPositionTopLeftSubMap,
//				screenPositionBottomRightSubMap);
//
//		System.out.println(mouseX + ", " + mouseY + ", " + mouseOnMainMap + ", " + mouseOnSubMap);
//
//		if (mouseOnMainMap) {
//			// Deselect All Markers
//			for (Marker marker : mainMap.getMarkers()) {
//				marker.setSelected(false);
//				marker.setStrokeWeight(1);
//			}
//
//			// Get Selected Marker
//			Marker marker = mainMap.getFirstHitMarker(mouseX, mouseY);
//			if (marker != null) {
//				marker.setSelected(true);
//				marker.setStrokeWeight(3);
//			}
//		} else if (mouseOnSubMap) {
//			// Deselect All Markers
//			for (Marker marker : subMap.getMarkers()) {
//				marker.setSelected(false);
//				marker.setStrokeWeight(1);
//			}
//
//			// Get Selected Marker
//			Marker marker = subMap.getFirstHitMarker(mouseX, mouseY);
//			if (marker != null) {
//				marker.setSelected(true);
//				marker.setStrokeWeight(3);
//			}
//		}
//	}
//
//	public void draw() {
//		background(200);
//
//		mainMap.draw();
//		subMap.draw();
//		topNewsLabel.draw();
//		topNewsTextArea.draw();
//		introductionLabel.draw();
//		introductionTextArea.draw();
//		governmentLabel.draw();
//		governmentTextArea.draw();
//		economyLabel.draw();
//		economyTextArea.draw();
//		// flagLabel.draw();
//		// flagButton.draw();
//		// countryFlagTextArea.draw();
//		searchButton.draw();
//		searchTextField.draw();
//		flagDisplay.draw();
//
//		// Draw lat, long information
//		fill(300, 0, 0, 500); // mouse pointer colour filling
//		noFill();
//		if (mouseOnMainMap) {
//			Location location = mainMap.getLocation(mouseX, mouseY);
//			text("geoPosition:" + location.toString(), mouseX, mouseY);
//		} else if (mouseOnSubMap) {
//			Location location = subMap.getLocation(mouseX, mouseY);
//			text("geoPosition:" + location.toString(), mouseX, mouseY);
//		}
//
//	}
//
//	// Popup continent on subMap when click over continent of mainMap //
//	// Location location1 = new Location(15.456, 20.987);
//
//	public String getContinent(String continentId) {
//		if (continentId.equals("AFC")) {
//			return "africa/countries.geo.json";
//		} else if (continentId.equals("ASA")) {
//			return "asia/countries.geo.json";
//		} else if (continentId.equals("AUS")) {
//			return "australia/countries.geo.json";
//		} else if (continentId.equals("EUR")) {
//			return "europe/countries.geo.json";
//		} else if (continentId.equals("NAM")) {
//			return "northAmerica/countries.geo.json";
//		} else if (continentId.equals("SAM")) {
//			return "southAmerica/countries.geo.json";
//		} else if (continentId.equals("ATC")) {
//			return "antarctica/countries.geo.json";
//		}
//		return null;
//	}
//	// to displaay all information into Text Area when click over a country on
//	// subMap;
//
//	private String button_clicked(String string) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public static void main(String[] args) {
//		PApplet.main(new String[] { MyApp2.class.getName() });
//	}
//}
