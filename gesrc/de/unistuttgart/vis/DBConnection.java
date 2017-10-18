package de.unistuttgart.vis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class DBConnection {
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	public ResultSet find(String s) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/worldinfo?useSSL=false", "root", "1988");
			ps = con.prepareStatement("select * from worldinfo.project_db where country_name = ?");
			ps.setString(1, s);
			rs = ps.executeQuery();
				
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		return rs;
	}

}