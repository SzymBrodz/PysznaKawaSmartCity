package org.roads_service;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.concurrent.ForkJoinPool;

public class DataReader {

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
}