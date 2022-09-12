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
                        "poster TEXT NOT NULL, " +
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

    public static int insertVideo(String title, String file, String poster, double resume_time) {
        int id = 0;
        String sql = "INSERT INTO videos(title, file, poster, resume_time) VALUES(?, ?, ?, ?)";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, file);
            pstmt.setString(3, poster);
            pstmt.setDouble(4, resume_time);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            else {
                throw new SQLException("No ID");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return id;
    }

    public static int insertVideo(String title, String file, String poster) {
        return insertVideo(title, file, poster, 0);
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

    public static ArrayList<MovieTile> selectVideosAsTiles() {
        ArrayList<MovieTile> videos = new ArrayList<MovieTile>();
        String sql = "SELECT * FROM videos";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                videos.add(new MovieTile(rs.getInt("video_id"),
                                        rs.getString("title"),
                                        rs.getString("file"),
                                        rs.getString("poster"),
                                        rs.getDouble("resume_time")));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return videos;
    }

    public static void updateVideoTime(int video_id, double resume_time) {
        String sql = "UPDATE videos SET resume_time = ? WHERE video_id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, resume_time);
            pstmt.setInt(2, video_id);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateVideoFile(int video_id, String file) {
        String sql = "UPDATE videos SET file = ? WHERE video_id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, file);
            pstmt.setInt(2, video_id);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Changing the file also resets resume time to 0
        updateVideoTime(video_id, 0);
    }

    public static void updateVideoTitle(int video_id, String title) {
        String sql = "UPDATE videos SET title = ? WHERE video_id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setInt(2, video_id);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateVideoPoster(int video_id, String poster) {
        String sql = "UPDATE videos SET poster = ? WHERE video_id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, poster);
            pstmt.setInt(2, video_id);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteVideo(int video_id) {
        String sql = "DELETE FROM videos WHERE video_id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, video_id);
                pstmt.executeUpdate();
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
