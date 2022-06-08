import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBController {
    
    private static Connection connect() {
        Connection conn = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    public static void createDB() {
        Connection conn = connect();
        String sql = "CREATE TABLE IF NOT EXISTS videos " +
                        "(video_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "file TEXT NOT NULL, " +
                        "resume_time REAL)";
        
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void insertVideo(String title, String file, double resume_time) {
        String sql = "INSERT INTO videos(title, file, resume_time) VALUES(?, ?, ?)";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, file);
            pstmt.setDouble(3, resume_time);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void insertVideo(String title, String file) {
        insertVideo(title, file, 0);
    }

    public static ArrayList<String> selectFiles() {
        ArrayList<String> files = new ArrayList<String>();
        String sql = "SELECT file FROM videos";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                files.add(rs.getString("file"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return files;
    }

}
