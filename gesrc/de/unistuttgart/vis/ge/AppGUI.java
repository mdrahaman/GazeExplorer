package de.unistuttgart.vis.ge;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppGUI extends JFrame implements ActionListener {

	private ImageIcon icon;
	private ImageIcon worldMap;
	private ImageIcon searchIcon;
	private static Container container; // container declare to keep window

	private Font font;
	private Font textFieldFont;

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JLabel label9;
	private JLabel label10;
	private JLabel label11;

	private static JTextArea newsTextArea;
	private static JTextArea introTextArea;
	private static JTextArea economyTextArea;
	private static JTextArea communicationTextArea;
	private static JTextArea governmentTextArea;
	private static JTextArea mousePositionTextArea;

	private JTextField searchTextField;

	private JTextArea mapTextArea;

	private JButton searchButton;

	private Cursor cursor1;

	private JScrollPane scrolltextArea;

	private JComboBox comboBox1;

	public static JMenuBar menubar;
	public static JMenu homeMenu;
	public static JMenuItem open;

	// constructor
	public AppGUI() {
		/* method */
		initComponent();
	}

	public static JLabel createLabel(String text, int x, int y, int width, int height, Font font) {
		JLabel label = new JLabel();
		label.setText(text);
		label.setBounds(x, y, width, height);
		label.setFont(font);

		// label.setForeground(Color.green);
		// label.setBackground(Color.darkGray);
		// label.setOpaque(true);
		// label.setToolTipText("You Are Exploring Top News of Country");

		return label;
	}

	private void initComponent() {
		/* bring window in a container and Set background color */

		container = this.getContentPane();
		container.setBackground(Color.GRAY);
		container.setLayout(null);
		font = new Font("Serif", Font.BOLD, 28);
		textFieldFont = new Font("Serif", Font.PLAIN, 18);

		icon = new ImageIcon(getClass().getResource("/images/EyeTrack.png"));
		this.setIconImage(icon.getImage());

		label1 = createLabel("Top News", 1480, 0, 440, 30, font);
		container.add(label1);

		newsTextArea = new JTextArea();
		// textArea.setBounds(1480, 45, 440, 155);
		// container.add(textArea);
		newsTextArea.setFont(textFieldFont);
		newsTextArea.setLineWrap(true);
		newsTextArea.setWrapStyleWord(true);
		newsTextArea.setEditable(false);

		scrolltextArea = new JScrollPane(newsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolltextArea.setBounds(1480, 30, 430, 170);
		container.add(scrolltextArea);

		/* Image adding to label */
		// worldMap = new ImageIcon(getClass().getResource("europeMap.jpg"));
		//
		// label11 = new JLabel(worldMap);
		// label11.setBounds(0, 0, 1480, 965);
		// container.add(label11);

		mapTextArea = new JTextArea();
		mapTextArea.setBounds(0, 40, 1480, 965);
		container.add(mapTextArea);
		mapTextArea.setFont(font);

		/* add label11 to mapTextArea */

		// mapTextArea.add(label11);

		// find mouse position in a text area //
		//
		// mapTextArea.addMouseMotionListener(new MouseMotionListener()
		//
		// {
		//
		// @Override
		// public void mouseDragged(MouseEvent me) {
		// mousePositionTextArea.setText("Mouse drag");
		//
		// }
		//
		// @Override
		// public void mouseMoved(MouseEvent me) {
		// mousePositionTextArea.setText("Mouse Position X = " + me.getX() +
		// "\nMouse Position Y = " + me.getY());
		//
		// }
		//
		// });

		label2 = new JLabel();
		label2.setText("Introduction & Geography");
		container.add(label2);
		label2.setForeground(Color.green);
		label2.setBounds(1480, 200, 440, 30);
		label2.setFont(new Font("Serif", Font.BOLD, 28));
		label2.setOpaque(true);
		label2.setBackground(Color.darkGray);
		label2.setToolTipText("You Are Exploring Introduction of Country");

		introTextArea = new JTextArea();
		// textArea.setBounds(1480, 45, 440, 155);
		// container.add(textArea);
		introTextArea.setFont(textFieldFont);
		introTextArea.setLineWrap(true);
		introTextArea.setWrapStyleWord(true);
		introTextArea.setEditable(false);

		scrolltextArea = new JScrollPane(introTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolltextArea.setBounds(1480, 230, 430, 170);
		container.add(scrolltextArea);

		label3 = createLabel("Government & Peoples,Mouse Position", 1480, 395, 440, 30, font);
		container.add(label3);

		label4 = new JLabel();
		label4.setText("Economy & Energy");
		container.add(label4);
		label4.setForeground(Color.green);
		label4.setBounds(1480, 595, 440, 30);
		label4.setFont(new Font("Serif", Font.BOLD, 28));
		label4.setOpaque(true);
		label4.setBackground(Color.darkGray);
		label4.setToolTipText("You Are Exploring People & Society of Country");

		economyTextArea = new JTextArea();
		// textArea.setBounds(1480, 45, 440, 155);
		// container.add(textArea);
		economyTextArea.setFont(textFieldFont);
		economyTextArea.setLineWrap(true);
		economyTextArea.setWrapStyleWord(true);
		economyTextArea.setEditable(false);

		scrolltextArea = new JScrollPane(economyTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolltextArea.setBounds(1480, 620, 430, 170);
		container.add(scrolltextArea);

		label5 = new JLabel();
		label5.setText("Communication & Transportation");
		container.add(label5);
		label5.setForeground(Color.green);
		label5.setBounds(1480, 790, 440, 30);
		label5.setFont(new Font("Serif", Font.BOLD, 28));
		label5.setOpaque(true);
		label5.setBackground(Color.darkGray);
		label5.setToolTipText("You Are Exploring Energy System");

		communicationTextArea = new JTextArea();
		// textArea.setBounds(1480, 45, 440, 155);
		// container.add(textArea);
		communicationTextArea.setFont(textFieldFont);
		communicationTextArea.setLineWrap(true);
		communicationTextArea.setWrapStyleWord(true);
		communicationTextArea.setEditable(false);

		scrolltextArea = new JScrollPane(communicationTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolltextArea.setBounds(1480, 820, 430, 170);
		container.add(scrolltextArea);

		searchTextField = new JTextField("search country");
		searchTextField.setBounds(1100, 0, 230, 40);
		searchTextField.setFont(font);
		container.add(searchTextField);

		// label6 = new JLabel();
		// label6.setText("Military & Security");
		// container.add(label6);
		// label6.setForeground(Color.green);
		// label6.setBounds(1290, 790, 275, 30);
		// label6.setFont(new Font("Serif", Font.BOLD, 28));
		// label6.setOpaque(true);
		// label6.setBackground(Color.darkGray);
		// label6.setToolTipText("You Are Exploring Military & Security
		// Status");
		//
		// label7 = new JLabel();
		// label7.setText("Transportation");
		// container.add(label7);
		// label7.setForeground(Color.green);
		// label7.setBounds(980, 790, 275, 30);
		// label7.setFont(new Font("Serif", Font.BOLD, 28));
		// label7.setOpaque(true);
		// label7.setBackground(Color.darkGray);
		// label7.setToolTipText("You Are Exploring Transportation System");
		//
		// label8 = new JLabel();
		// label8.setText("Communication");
		// container.add(label8);
		// label8.setForeground(Color.green);
		// label8.setBounds(670, 790, 270, 30);
		// label8.setFont(new Font("Serif", Font.BOLD, 28));
		// label8.setOpaque(true);
		// label8.setBackground(Color.darkGray);
		// label8.setToolTipText("You Are Exploring Communication System");
		//
		// label9 = new JLabel();
		// label9.setText("Economy");
		// container.add(label9);
		// label9.setForeground(Color.green);
		// label9.setBounds(360, 790, 275, 30);
		// label9.setFont(new Font("Serif", Font.BOLD, 28));
		// label9.setOpaque(true);
		// label9.setBackground(Color.darkGray);
		// label9.setToolTipText("You Are Exploring Economic Conditions");
		//
		// label10 = new JLabel();
		// label10.setText("Government");
		// container.add(label10);
		// label10.setForeground(Color.green);
		// label10.setBounds(50, 790, 275, 30);
		// label10.setFont(new Font("Serif", Font.BOLD, 28));
		// label10.setOpaque(true);
		// label10.setBackground(Color.darkGray);
		// label10.setToolTipText("You Are Exploring Government System");

		mousePositionTextArea = new JTextArea();
		mousePositionTextArea.setFont(textFieldFont);
		mousePositionTextArea.setLineWrap(true);
		mousePositionTextArea.setWrapStyleWord(true);
		mousePositionTextArea.setEditable(false);

		// mousePositionTextArea = new JTextArea();
		// container.add(mousePositionTextArea);
		// mousePositionTextArea.setBounds(1480, 430, 440, 170);
		// mousePositionTextArea.setFont(new Font("Serif", Font.ITALIC, 18));
		// mousePositionTextArea.setOpaque(true);
		// mousePositionTextArea.setBackground(Color.GREEN);

		scrolltextArea = new JScrollPane(mousePositionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolltextArea.setBounds(1480, 430, 430, 165);
		container.add(scrolltextArea);

		searchIcon = new ImageIcon(getClass().getResource("/images/search.jpg"));

		searchButton = new JButton(searchIcon);
		container.add(searchButton);
		searchButton.setBounds(1330, 0, 150, 40);
		searchButton.setFont(new Font("Serif", Font.BOLD, 22));
		searchButton.addActionListener(this);
		// searchButton.addActionListener(new ActionListener()
		// {
		//
		// @Override
		// public void actionPerformed(ActionEvent e)
		// {
		// searchButton.setText("");
		// }
		//
		// });
		cursor1 = new Cursor(Cursor.HAND_CURSOR);
		searchButton.setCursor(cursor1);

		comboBox1 = new JComboBox();
		comboBox1.setBounds(870, 0, 230, 40);
		comboBox1.setEditable(true);
		comboBox1.setFont(font);
		container.add(comboBox1);
		comboBox1.addItem("Albania");
		comboBox1.addItem("Andorra");
		comboBox1.addItem("Armenia");
		comboBox1.addItem("Austria");
		comboBox1.addItem("Azerbaijan");
		comboBox1.addItem("Belarus");
		comboBox1.addItem("Belgium");
		comboBox1.addItem("Bosnia and Herzegovina");
		comboBox1.addItem("Bulgaria");
		comboBox1.addItem("Croatia");
		comboBox1.addItem("Cyprus");
		comboBox1.addItem("Czech Republic");
		comboBox1.addItem("Denmark");
		comboBox1.addItem("Estonia");
		comboBox1.addItem("Finland");
		comboBox1.addItem("France");
		comboBox1.addItem("Georgia");
		comboBox1.addItem("Germany");
		comboBox1.addItem("Greece");
		comboBox1.addItem("Hungary");
		comboBox1.addItem("Iceland");
		comboBox1.addItem("Ireland");
		comboBox1.addItem("Italy");
		comboBox1.addItem("Kazakhstan");
		comboBox1.addItem("Kosovo");
		comboBox1.addItem("Latvia");
		comboBox1.addItem("Liechtenstein");
		comboBox1.addItem("Lithuania");
		comboBox1.addItem("Luxembourg");
		comboBox1.addItem("Macedonia (FYROM)");
		comboBox1.addItem("Malta");
		comboBox1.addItem("Moldova");
		comboBox1.addItem("Monaco");
		comboBox1.addItem("Montenegro");
		comboBox1.addItem("Netherlands");
		comboBox1.addItem("Norway");
		comboBox1.addItem("Poland");
		comboBox1.addItem("Portugal");
		comboBox1.addItem("Romania");
		comboBox1.addItem("Russia");
		comboBox1.addItem("San Marino");
		comboBox1.addItem("Serbia");
		comboBox1.addItem("Slovakia");
		comboBox1.addItem("Slovenia");
		comboBox1.addItem("Spain");
		comboBox1.addItem("Sweden");
		comboBox1.addItem("Switzerland");
		comboBox1.addItem("Turkey");
		comboBox1.addItem("Ukraine");
		comboBox1.addItem("United Kingdom (UK)");
		comboBox1.addItem("Vatican City (Holy See)");

	}

	public static void main(String[] args) {

		AppGUI frame = new AppGUI();
		frame.setVisible(true);

		/* without constructor we can do like below */

		frame.setBounds(0, 0, 1920, 1040);
		// frame.setSize(1920, 1040);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocale(null);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setTitle("Welcome to Gaze Exploration");
		frame.setBackground(Color.GREEN);

		// combination PApplet with JFrame

		// create a panel for the applet and the button panel
		JPanel panel = new JPanel();

		// create an instance of your processing applet
		// final MainApp applet = new MainApp();

		// start the applet
		// applet.init();

		JMenuBar menubar = new JMenuBar();
		menubar.setBounds(0, 0, 870, 40);
		menubar.setBackground(Color.DARK_GRAY);
		container.add(menubar);

		JMenu file = new JMenu("File");
		file.setFont(new Font("serif", Font.BOLD, 22));
		file.setForeground(Color.MAGENTA);
		menubar.add(file);

		JMenuItem open = new JMenuItem("Open");
		open.setFont(new Font("serif", Font.BOLD, 15));
		file.add(open);

		JMenuItem exit = new JMenuItem("Exit");
		open.setFont(new Font("serif", Font.BOLD, 15));
		file.add(exit);

		// JDBC connecion and displying data to textarea //

		// get a connection to database

		//
		// try {
		// Connection myConn =
		// DriverManager.getConnection("jdbc:mysql://localhost:3306/worldinfo","root","1988");
		//
		// // create statement //
		//
		// Statement myState = myConn.createStatement();
		//
		// // execute SQL query //
		//
		// ResultSet communicationRset = myState.executeQuery("SELECT
		// communication_transportation FROM worldinfo.project_db");
		//
		// //ResultSet economyRset = myState.executeQuery("SELECT
		// communication_transportation FROM worldinfo.project_db");
		//
		//
		//
		//
		// StringBuilder strBuilder = new StringBuilder();
		//
		//
		// //process the result set //
		//
		// while (communicationRset.next())
		// {
		//
		// //System.out.println(myRset.getString("communication_transportation"));
		//
		// strBuilder.append(communicationRset.getString("communication_transportation"));
		//
		//
		//// System.out.println(myRset.getString("country_name") + "\n" +
		// ("top_news") + " , "
		//// + myRset.getString("introduction_geography") + " \n " +
		// myRset.getString("government_people") + " \n " +
		// myRset.getString("economy_energy") + " \n " +
		// myRset.getString("communication_transportation"));
		// }
		//
		// communicationTextArea.setText(strBuilder.toString());
		// myConn.close();
		//
		//
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// actionListener method for data retrieve on desired TeatArea //

	@Override
	public void actionPerformed(ActionEvent e) {
		Function connectToDb = new Function();
		ResultSet resultSet = null;
		String news = "top_news";
		String introduction = "introduction_geography";
		String economy = "economy_energy";
		String communication = "communication_transportation";
		String government = "government_people";

		resultSet = connectToDb.find(searchTextField.getText());

		try {
			if (resultSet.next()) {
				newsTextArea.setText(resultSet.getString("top_news"));
				introTextArea.setText(resultSet.getString("introduction_geography"));
				economyTextArea.setText(resultSet.getString("economy_energy"));
				communicationTextArea.setText(resultSet.getString("communication_transportation"));
				mousePositionTextArea.setText(resultSet.getString("government_people"));

			} else {
				JOptionPane.showMessageDialog(null, "No data for this country");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}

	}

	class Function {
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

}
