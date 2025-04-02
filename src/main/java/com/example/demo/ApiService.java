package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class ApiService {
    public static ArrayList<Map<String,String>> getRidesList(int park_id) {
        StringBuilder sb = new StringBuilder("https://queue-times.com/parks/");
        sb.append(""+park_id).append("/queue_times.json");
        String url = sb.toString();
        JsonNode resp;
        try {
            URL url1 = new URL(url);
            ObjectMapper mapper = new ObjectMapper();
            resp = mapper.readTree(url1);
        } catch (Exception e) {
            return null;
        }
        // ==========================================
        //  Should have data by now, start collating
        // ==========================================
        ArrayList<Map<String,String>> rideList = new ArrayList<>();
        ArrayNode lands = (ArrayNode) resp.get("lands");
        for (JsonNode land : lands) {
            ArrayNode rides = (ArrayNode) land.get("rides");
            for(JsonNode ride : rides) {
                Map<String,String> item = new HashMap<>();
                java.util.Iterator<Map.Entry<String,JsonNode>> i = ride.fields();
                while (i.hasNext()) {
                    Map.Entry<String, JsonNode> entry = i.next();
                    String evalString = entry.getValue().toString();
                    evalString = evalString.replaceAll("^\"|\"$", "");
                    evalString = evalString.replaceAll("\\\\","");
                    item.put(entry.getKey(), evalString);
                }
                rideList.add(item);
            }
        }
        return rideList;
    }
}
