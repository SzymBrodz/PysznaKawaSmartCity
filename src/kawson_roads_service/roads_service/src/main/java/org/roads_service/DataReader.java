package org.roads_service;

import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class DataReader {

    // public static List<Map<String, Place>> keyValuePlaces = new ArrayList<>();
    public static Map<String, List<Place>> keyValuePlaces = new HashMap<>();
    static Place start = Place.builder()
            .X(54.3961355)
            .Y(18.5743202)
            .title("University of Gdańsk")
            .url("https://www.google.com/maps/place/University+of+Gda%C5%84sk/data=!4m7!3m6!1s0x46fd752f76dddae7:0x4d4128c9a5066e47!8m2!3d54.3961355!4d18.5743202!16zL20vMDQxZzhn!19sChIJ59rddi91_UYRR24GpckoQU0?authuser=0&hl=en&rclk=1")
            .build();

    public static List<Place> ReadData(File file) throws Exception {
        Scanner inputFile = new Scanner(file);
        String row;
        String[] fields;
        List<Place> places = new ArrayList<>();
        int i = 0;

        // Skip column names
        inputFile.nextLine();
        while (inputFile.hasNext()) {
            row = inputFile.nextLine();
            fields = row.split(",");
            double x = Double.parseDouble(fields[0]);
            double y = Double.parseDouble(fields[1]);
            if (!isPlaceGoodCandidate(x, y, start.getX(), start.getY()))
                continue;
            String title = fields[3];
            String url = fields[4];
            Place place = Place.builder()
                    .X(x)
                    .Y(y)
                    .title(title)
                    .url(url)
                    .build();
            places.add(place);
            i++;
        }
        return places;
    }


    public static File[] listOfFiles(final File folder) {
        return folder.listFiles();
    }

    public static void computeRoutes(List<Place> places) throws Exception {
        ForkJoinPool pool = new ForkJoinPool(30);
        pool.submit(()->{
            places.parallelStream().forEach(place -> {
                try {
                    place.calculateRoutes(start);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }).get();

//        places.parallelStream().forEach(place -> {
//            try {
//                place.calculateRoutes(start);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    public static boolean isPlaceGoodCandidate(double x1, double y1, double x2, double y2) {
        return getDistanceFromLatLonInKm(x1, y1, x2, y2) < 5 ? true : false;
    }

    public static double getDistanceFromLatLonInKm(double x1, double y1, double x2, double y2) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(x2-x1);  // deg2rad below
        double dLon = Math.toRadians(y2-y1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public static void putKeyValuePlaces(String categoryName, List<Place> places){
        keyValuePlaces.put(categoryName, places);
    }

    public static JSONObject getFinalJson(){
        List<Place> p = new ArrayList<>();
        Map<String, List<Place>> m = new HashMap<>();

        p.addAll(keyValuePlaces.get("apteka"));
        p.addAll(keyValuePlaces.get("dentysta"));
        p.addAll(keyValuePlaces.get("lekarz"));
        p.addAll(keyValuePlaces.get("ortodonta"));
        m.put("zdrowie", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("bank"));
        p.addAll(keyValuePlaces.get("biuro"));
        p.addAll(keyValuePlaces.get("drukarnia"));
        p.addAll(keyValuePlaces.get("office"));
        m.put("biurowosc", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("atrakcje"));
        p.addAll(keyValuePlaces.get("bar"));
        p.addAll(keyValuePlaces.get("impreza"));
        p.addAll(keyValuePlaces.get("las"));
        p.addAll(keyValuePlaces.get("muzeum"));
        p.addAll(keyValuePlaces.get("park"));
        p.addAll(keyValuePlaces.get("plaza"));
        m.put("atrakcje", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("fastfood"));
        p.addAll(keyValuePlaces.get("kawiarnia"));
        p.addAll(keyValuePlaces.get("cukiernia"));
        p.addAll(keyValuePlaces.get("piekarnia"));
        p.addAll(keyValuePlaces.get("piekarnia"));
        p.addAll(keyValuePlaces.get("restauracje"));
        m.put("jedzenie", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("monopolowy"));
        p.addAll(keyValuePlaces.get("shopping mall"));
        p.addAll(keyValuePlaces.get("sklep spozywczy"));
        p.addAll(keyValuePlaces.get("sklepAGD"));
        p.addAll(keyValuePlaces.get("piekarnia"));
        m.put("sklepy", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("fotograf"));
        p.addAll(keyValuePlaces.get("fryzjer"));
        p.addAll(keyValuePlaces.get("krawiec"));
        p.addAll(keyValuePlaces.get("mechanik"));
        p.addAll(keyValuePlaces.get("pralnia"));
        m.put("uslugi", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("dzieci"));
        p.addAll(keyValuePlaces.get("plac zabaw"));
        p.addAll(keyValuePlaces.get("przedszkole"));
        p.addAll(keyValuePlaces.get("szkola"));
        p.addAll(keyValuePlaces.get("zlobek"));
        m.put("dzieci", p);

        p = new ArrayList<>();
        p.addAll(keyValuePlaces.get("basen"));
        p.addAll(keyValuePlaces.get("fitness"));
        p.addAll(keyValuePlaces.get("silownia"));
        m.put("sport", p);

        // ratings: [ZDROWIE, BIUROWOSC, ATRAKCJE, JEDZENIE, SKLEPY, USLUGI, DZIECI, SPORT]
        // List<Double> ratings = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        List<Double> ratings = new ArrayList<>();

        for(List<Place> places : m.values())
            if (places.size() <= 0)
                ratings.add(0.0);
            else
                ratings.add(getRating(places));

        return generateJson(m, ratings);
    }

    public static JSONObject generateJson(Map<String, List<Place>> m, List<Double> ratings){
        return new JSONObject()
                .put("Oceny", ratings)
                .put("Kategorie", new JSONObject(m));
    }

    /**
     * get rating of specific category in places
     *
     * @param places in specific category
     * @return
     */
    public static double getRating(List<Place> places){
        List<Double> times = new ArrayList<>();
        places.stream().sorted((o1, o2) -> o1.getShortestTime().compareTo(o2.getShortestTime()))
                .limit(10)
                .forEach(place -> {times.add(place.getShortestTime());});
        double rating = 0.0;
        double subtractor = 0.1;
        int i = 0;
        for ( double x : times) {
            rating += ((15 * 60 - x ) / (15 * 60 )) * (1 - (i * subtractor)) * 5;
        }
        return rating / times.size();
    }
}