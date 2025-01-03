package com.erwebsocket.controller;

import com.google.api.client.util.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    @Value("${google.api.key}")
    private String googleApiKey;

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyPlaces(
            @RequestParam("location") String location,
            @RequestParam("radius") int radius,
            @RequestParam("type") String type) {

        String googleApiUrl = String.format(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s&radius=%d&type=%s&key=AIzaSyATwAelFU5r5A_oYCKM1h9NDItM1DDLXIE",
                location, radius, type, googleApiKey);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(googleApiUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
