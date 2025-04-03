package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;

@Service
public class ApiService {
    @Autowired CurrentRideRepo curr;
    @Autowired ParkRepo parks;
    private boolean populated = false;

    public ArrayList<Map<String,String>> getRidesList(int park_id) {
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

    public void cache(ArrayList<Map<String, String>> rideData) {
        for(Map<String,String> row : rideData) {
            long rideId = Long.parseLong(row.get("id"));
            String rideName = row.get("name");
            int waitTime = Integer.parseInt(row.get("wait_time"));
            boolean isOpen = Boolean.getBoolean(row.get("is_open"));
            Instant lastUpdated = Instant.parse(row.get("last_updated"));
            curr.save(new CurrentRideData(rideId, rideName, isOpen, waitTime, lastUpdated));
        }
        for(CurrentRideData c : curr.findAll()) {
            System.out.println(c.name);
        }
    }

    public void populateParks() {
        if (!populated) { // don't run this if it's been populated already
            List<Integer> park_ids = Arrays.asList(new Integer[]{64, 65, 66, 67, 8, 7, 6, 5, 17, 16, 28, 4});
            for(int id : park_ids) {
                String urt = "https://queue-times.com/parks/" + id + ".json";
                JsonNode resp;
                try {
                    URL url = new URL(urt);
                    ObjectMapper mapper = new ObjectMapper();
                    resp = mapper.readTree(url);
                } catch(Exception ignored) {
                    return;
                }
                long pid = resp.get("id").asLong();
                String name = resp.get("name").asText();
                String timezone = resp.get("timezone").asText();
                parks.save(new ParkData(pid, name, timezone));
            }
            populated = true;
        }
    }

    public ParkData getPark(long id) {
        populateParks();
        Optional<ParkData> park = parks.findById(id);
        return park.orElse(null);
    }
}
