package org.roads_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import okhttp3.*;
import org.roads_service.DataReader;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private static List<String> urls = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        System.out.println("START");

        for (File file : new File("Smart City - dane do zadania").listFiles()) {
            DataReader.putKeyValuePlaces(file.getName().split("_")[0], new ArrayList<>());
        }
        File f = new File("Smart City - dane do zadania").listFiles()[0];
        List<Place> places = DataReader.ReadData(f);
        DataReader.computeRoutes(places);
        DataReader.keyValuePlaces.replace(f.getName().split("_")[0], places);

        // for(Place p : DataReader.keyValuePlaces.get("bar"))


        DataReader.getFinalJson();
        // System.out.println(DataReader.getRating(DataReader.keyValuePlaces.get("bar")));
    }
}