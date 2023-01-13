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

public class Main {
    private static List<String> urls = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        System.out.println("START");
        File[] f = new File("Smart City - dane do zadania").listFiles();
        for (File file : f){
//            List<Place> places = DataReader.ReadData(new File("Smart City - dane do zadania").listFiles()[1]);
//            System.out.println(file.getName());
            List<Place> places = DataReader.ReadData(file);
            DataReader.computeRoutes(places);
        }

    }
}