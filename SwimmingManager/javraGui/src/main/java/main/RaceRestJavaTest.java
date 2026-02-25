package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RaceRestJavaTest {
    private static final String BASE_URL = "http://localhost:5000/races";

    public static void main(String[] args) throws IOException {
        int id=createRace();
        getAllRaces();
        updateRace(id);
        deleteRace(id);
    }

    private static int createRace() throws IOException {
        String jsonInput = """
        {
            "Distance": 200,
            "Style": "freestyle",
            "TotalParticipants": 5
        }
    """;
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        String response = readResponse(connection);
        System.out.println("POST Response Code: " + connection.getResponseCode());
        System.out.println("Response: " + response);


        String idString = response.replaceAll("[^0-9]", "");
        return Integer.parseInt(idString);
    }


    private static void getAllRaces() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
        connection.setRequestMethod("GET");

        System.out.println("GET ALL Response Code: " + connection.getResponseCode());
        System.out.println("Response: " + readResponse(connection));
    }

    private static void updateRace(int id) throws IOException {
        String jsonInput = """
        {
            "Distance": 800,
            "Style": "butterfly",
            "TotalParticipants": 12
        }
        """;

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("PUT Response Code: " + connection.getResponseCode());
    }

    private static void deleteRace(int id) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        connection.setRequestMethod("DELETE");

        System.out.println("DELETE Response Code: " + connection.getResponseCode());
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        InputStream stream;
        if (conn.getResponseCode() < 400) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            return response.toString();
        }
    }
}
