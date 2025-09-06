package smartnotes_console.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	public static void testUser() {
		String sqlSelectAllPersons = "SELECT * FROM users";
		try (Connection conn = DriverManager.getConnection(Storage.DB_CONNECTION_URL, Storage.DB_USERNAME, Storage.DB_PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); 
				ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
		            long id = rs.getLong("id");
		            String name = rs.getString("name");

		            // do something with the extracted data...
		            System.out.println("ID: " + id + ", " + name);
		        }
		} catch (SQLException e) {
		    // handle the exception
			System.out.println("Error: " + e.toString());
		}
	}
}
