package com.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> getWeatherData() throws IOException, InterruptedException {
        String today = LocalDate.now().toString();
        String url = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=24.985525&longitude=121.571797" +
                "&current=temperature_2m,relative_humidity_2m,weathercode" +
                "&daily=precipitation_probability_max" +
                "&start_date=" + today +
                "&end_date=" + today +
                "&timezone=auto";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());

        Map<String, Object> result = new HashMap<>();
        result.put("weatherCode", root.path("current").path("weathercode").asInt());
        result.put("temperature", root.path("current").path("temperature_2m").asDouble());
        result.put("precipitation", root.path("daily").path("precipitation_probability_max").get(0).asInt());

        return result;
    }

    public String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "晴天";
            case 1, 2, 3 -> "多雲";
            case 45, 48 -> "霧";
            case 51, 53, 55 -> "毛毛雨";
            case 61, 63, 65 -> "雨";
            case 71, 73, 75 -> "雪";
            case 95 -> "雷暴";
            default -> "未知天氣";
        };
    }
}