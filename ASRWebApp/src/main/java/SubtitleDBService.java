import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class SubtitleDBService {

    private static final String DB_URL =
    "<your_db_connection_url>";

    private static final String DB_USER =
    "azureuser";

    private static final String DB_PASSWORD =
    "<your_db_password>";


    /* Get database connection */

    private Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }


    /* Main method to save everything using ONE connection */

    public void saveSubtitleData(
            String filename,
            String extension,
            String language,
            JSONArray subtitles
    ) throws Exception {

        Connection conn = null;

        try {

            conn = getConnection();
            conn.setAutoCommit(false);

            double duration = calculateDuration(subtitles);
            int fileId = insertFileMeta(conn, filename, extension, language, duration);

            int[] subtitleIds = insertSubtitles(conn, fileId, subtitles);

            insertTranslations(conn, subtitleIds, subtitles, language);

            conn.commit();

            System.out.println("All DB inserts completed");

        }
        catch (Exception e) {

            if (conn != null) {
                conn.rollback();
            }

            throw e;
        }
        finally {

            if (conn != null) {
                conn.close();
            }
        }
    }


    /* Insert into files table */

    private int insertFileMeta(
            Connection conn,
            String filename,
            String extension,
            String language,
            double duration
    ) throws Exception {

        String sql =
        "INSERT INTO files(file_name, audio_type, language, duration) VALUES (?,?,?,?) RETURNING id";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, filename);
        ps.setString(2, extension);
        ps.setString(3, language);
        ps.setDouble(4, duration);

        ResultSet rs = ps.executeQuery();

        int fileId = 0;

        if (rs.next()) {
            fileId = rs.getInt(1);
        }

        rs.close();
        ps.close();

        return fileId;
    }


    /* Insert subtitles */

    private int[] insertSubtitles(
            Connection conn,
            int fileId,
            JSONArray subtitles
    ) throws Exception {

        String sql =
        "INSERT INTO subtitles(file_id,start_time,end_time,text) VALUES (?,?,?,?) RETURNING id";

        PreparedStatement ps =
        conn.prepareStatement(sql);
        
        int[] ids = new int[subtitles.length()];

        for (int i = 0; i < subtitles.length(); i++) {

            JSONObject s = subtitles.getJSONObject(i);

            ps.setInt(1, fileId);
            ps.setDouble(2, s.getDouble("start"));
            ps.setDouble(3, s.getDouble("end"));
            ps.setString(4, s.getString("text"));

//            ps.addBatch();


//        ps.executeBatch();

//        ResultSet rs = ps.getGeneratedKeys();
           ResultSet rs = ps.executeQuery();
           
           if (rs.next()) {
           	ids[i] = rs.getInt(1);
           }
           
           rs.close();
        }
//        int index = 0;

//        while (rs.next()) {
//            ids[index++] = rs.getInt(1);
//        }
        ps.close();
        return ids;
    }


    /* Insert translations */

    private void insertTranslations(
            Connection conn,
            int[] subtitleIds,
            JSONArray subtitles,
            String sourceLang
    ) throws Exception {

        String sql =
        "INSERT INTO translations(subtitle_id,source_lang,target_lang,translated_text) VALUES (?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < subtitles.length(); i++) {

            JSONObject s = subtitles.getJSONObject(i);

            ps.setInt(1, subtitleIds[i]);
            ps.setString(2, sourceLang);
            ps.setString(3, "en");
            ps.setString(4, s.getString("translated_text"));

            ps.addBatch();
        }

        ps.executeBatch();

        ps.close();
    }
    
    private double calculateDuration(JSONArray subtitles) {

        if (subtitles.length() == 0)
            return 0;

        JSONObject lastSubtitle =
            subtitles.getJSONObject(subtitles.length() - 1);

        return lastSubtitle.getDouble("end");
    }
}
