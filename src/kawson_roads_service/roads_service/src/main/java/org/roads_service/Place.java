package org.roads_service;

import org.json.JSONObject;
import lombok.Builder;
import lombok.Getter;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Place {

    private Double X;

    private Double Y;

    private String title;

    // Link to google maps point
    private String url;

    // Arrive times walk, bike & tram
    private Integer walk;

    private Integer bike;

    private Integer tram;

    public void displayPlace(){
        System.out.println("X: "+this.X+" Y: "+this.Y+" Title: "+this.title+" Walk: "+this.walk+" Bike: "+this.bike+" Tram: "+this.tram);
    }

    /**
     *
     * @param start
     * @param mode type of transport: walking, bicycling, transit, driving
     * @return time needed to finish route [s]
     * @throws Exception
     */
    public Integer calculateRoute(Place start, String mode) throws Exception {
        String u = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+start.X+"%2C"+start.Y+"&destinations="+this.X+"%2C"+this.Y+"&mode="+mode+"&key=AIzaSyAhQZtsokVQdM3F4V0FGEn9wC_MoE325Eo";
        JSONObject jsonObject = new JSONObject(getHTML(u));
        try {
            return Integer.parseInt(jsonObject.getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0)
                    .getJSONObject("duration").get("value").toString());
        } catch (RuntimeException e){
            return -1;
        }
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }

    public void calculateRoutes(Place start) throws Exception {
        this.walk = calculateRoute(start, "walking");
        this.bike = calculateRoute(start, "bicycling");
        this.tram = calculateRoute(start, "transit");
    }

    public Double getShortestTime(){
        double min = Math.min(this.getWalk(), this.getBike());
        return Math.min(min, this.getTram());
    }
}
