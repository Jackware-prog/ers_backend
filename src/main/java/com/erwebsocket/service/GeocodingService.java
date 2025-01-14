package com.erwebsocket.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

@Service // Add this annotation to register the service as a Spring Bean
public class GeocodingService {

    private static final String API_KEY = "AIzaSyATwAelFU5r5A_oYCKM1h9NDItM1DDLXIE";

    public String getStateFromLatLong(BigDecimal latitude, BigDecimal longitude) {
        String state = null;
        try {
            String urlString = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
                    latitude, longitude, API_KEY
            );
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");

            // Extract the state from the results
            if (results.length() > 0) {
                JSONArray addressComponents = results.getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject component = addressComponents.getJSONObject(i);
                    JSONArray types = component.getJSONArray("types");
                    for (int j = 0; j < types.length(); j++) {
                        if (types.getString(j).equals("administrative_area_level_1")) {
                            state = component.getString("long_name");
                            break;
                        }
                    }
                    if (state != null) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }
}


