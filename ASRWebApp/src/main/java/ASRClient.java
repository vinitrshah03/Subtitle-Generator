import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.*;

@WebServlet("/ASRClient")
@MultipartConfig
public class ASRClient extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        Part filePart = request.getPart("mediafile");
        String sourceLang = request.getParameter("source_lang");

        String boundary = "----ASRBoundary" + System.currentTimeMillis();

        URL url = new URL("http://<your_public_ip>:8000/transcribe");

        HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary
        );

        OutputStream os = conn.getOutputStream();

        PrintWriter writer =
            new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);

        /* Send language field */

        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"source_lang\"")
              .append("\r\n\r\n");

        writer.append(sourceLang).append("\r\n");
        writer.flush();

        /* Send file */

        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"audio\"")
              .append("\r\n");

        writer.append("Content-Type: application/octet-stream")
              .append("\r\n\r\n");

        writer.flush();

        InputStream input = filePart.getInputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        input.close();

        writer.append("\r\n--" + boundary + "--\r\n");
        writer.close();

        /* Read response from Python */

        BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

        StringBuilder pythonResponse = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            pythonResponse.append(line);
        }

        reader.close();
        conn.disconnect();

        /* Convert Python response JSON */

        JSONObject json =
            new JSONObject(pythonResponse.toString());

        JSONObject translated;

        try {

            translated = translateSubtitles(json, sourceLang);

        } catch (Exception e) {
            throw new ServletException(e);
        }
        

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println(translated.toString());
        
        new Thread(() -> {

            try {

                SubtitleDBService dbService = new SubtitleDBService();

                dbService.saveSubtitleData(
                    filePart.getSubmittedFileName(),
                    getFileExtension(filePart.getSubmittedFileName()),
                    sourceLang,
                    translated.getJSONArray("subtitles")
                );

                System.out.println("Subtitles saved to database");

            } catch (Exception e) {

                e.printStackTrace();

            }

        }).start();
    }


    public static JSONObject translateSubtitles(JSONObject inputJson, String sourceLang)
            throws Exception {

        JSONArray subtitles =
            inputJson.getJSONArray("subtitles");

        JSONArray texts = new JSONArray();

        for (int i = 0; i < subtitles.length(); i++) {

            JSONObject sub =
                subtitles.getJSONObject(i);

            texts.put(
                new JSONObject()
                    .put("Text", sub.getString("text"))
            );
        }

        String translated =
            translateBatch(texts.toString(), sourceLang);

        JSONArray translatedArray =
            new JSONArray(translated);

//        for (int i = 0; i < subtitles.length(); i++) {
//
//            String english =
//                translatedArray
//                    .getJSONObject(i)
//                    .getJSONArray("translations")
//                    .getJSONObject(0)
//                    .getString("text");
//
//            subtitles.getJSONObject(i)
//                     .put("translated_text", english);
//        }

        for (int i = 0; i < subtitles.length(); i++) {

            JSONObject subtitle = subtitles.getJSONObject(i);

            String english =
                translatedArray
                    .getJSONObject(i)
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("text");

            subtitle.put("translated_text", english);
        }
        
        System.out.println("Translated subtitles: " + inputJson.toString());
        return inputJson;
    }


    public static String translateBatch(String jsonBody, String sourceLang)
            throws Exception {

        String key =
            "<your_AI_translation_secret_key>";

        String region = "centralindia";

        String endpoint =
        		"https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&from=" + sourceLang + "&to=en";
        
        @SuppressWarnings("deprecation")	
		URL url = new URL(endpoint);

        HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty(
            "Ocp-Apim-Subscription-Key", key
        );

        conn.setRequestProperty(
            "Ocp-Apim-Subscription-Region", region
        );

        conn.setRequestProperty(
            "Content-Type", "application/json"
        );

        OutputStream os =
            conn.getOutputStream();

        os.write(jsonBody.getBytes("UTF-8"));
        os.close();

        int status = conn.getResponseCode();
        if (status != 200) {
            BufferedReader errorReader =
                new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            
            String line;
            StringBuilder error = new StringBuilder();
            
            while ((line = errorReader.readLine()) != null) {
                error.append(line);
            }
            
            errorReader.close();

            throw new RuntimeException("Translation API failed: " + status + " " + error.toString());
        }

        BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(
                    conn.getInputStream(), "UTF-8"
                )
            );

        StringBuilder response =
            new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        return response.toString();
    }
    
    private String getFileExtension(String filename) {

        if (filename == null)
            return "";

        int dot = filename.lastIndexOf('.');

        if (dot > 0)
            return filename.substring(dot + 1);

        return "";
    }
}
