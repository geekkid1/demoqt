package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class DemoController {
    @Autowired ApiService as;

    public static Comparator<Map<String,String>> rideComp = new Comparator<Map<String, String>>() {
        @Override
        public int compare(Map<String, String> o1, Map<String, String> o2) {
            int wait1 = Integer.parseInt(o1.get("wait_time"));
            int wait2 = Integer.parseInt(o2.get("wait_time"));
            return wait2 - wait1;
        }
    };

    @GetMapping("/table/{park_id}")
    public String table(Model model, @PathVariable int park_id) {
        ParkData park = as.getPark(park_id);
        ArrayList<Map<String,String>> rideList = as.getRidesList(park_id);
        Instant lastUpdated = Instant.parse(rideList.get(0).get("last_updated"));
        List<String> headers = Arrays.asList(new String[]{"id", "name", "is_open", "wait_time"});
        model.addAttribute("headers", headers);
        rideList.sort(rideComp);
        model.addAttribute("rows", rideList);
        ZonedDateTime localTime = lastUpdated.atZone(ZoneId.of(park.timezone));
        String date = localTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String time = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        model.addAttribute("pname", park.name);
        return "table";
    }
}
