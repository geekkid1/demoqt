package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
public class DemoController {

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
        ArrayList<Map<String,String>> rideList = ApiService.getRidesList(park_id);
        List<String> headers = Arrays.asList(new String[]{"id", "name", "is_open", "wait_time"});
        model.addAttribute("headers", headers);
        rideList.sort(rideComp);
        model.addAttribute("rows", rideList);
        return "table";
    }
}
