package dev.shah.mcp.weather;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

@Service
public class WeatherService {

    private static final String BASE_URL = "https://api.weather.gov";

    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "Shah Minul Amin")
                .build();
    }


    /**
     * Get forecast for a specific latitude/longitude
     *
     * @param latitude  Latitude
     * @param longitude Longitude
     * @return The forecast for the given location
     * @throws RestClientException if the request fails
     */
    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        var points = restClient.get()
                .uri("/points/{latitude},{longitude}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        assert points != null;
        var forecast = restClient.get().uri(points.properties().forecast()).retrieve().body(Forecast.class);

        assert forecast != null;
        return forecast.properties().periods().stream().map(p -> String.format("""
                        %s:
                        Temperature: %s %s
                        Wind: %s %s
                        Forecast: %s
                        """, p.name(), p.temperature(), p.temperatureUnit(), p.windSpeed(), p.windDirection(),
                p.detailedForecast())).collect(Collectors.joining());
    }

    /**
     * Get alerts for a specific area
     *
     * @param state Area code. Two-letter US state code (e.g. CA, NY)
     * @return Human-readable alert information
     * @throws RestClientException if the request fails
     */
    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
    public String getAlerts(@ToolParam(description = "Two-letter US state code (e.g. CA, NY") String state) {
        Alert alert = restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

        assert alert != null;
        return alert.features()
                .stream()
                .map(f -> String.format("""
                                Event: %s
                                Area: %s
                                Severity: %s
                                Description: %s
                                Instructions: %s
                                """, f.properties().event(), f.properties().areaDesc(), f.properties().severity(),
                        f.properties().description(), f.properties().instruction()))
                .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        WeatherService client = new WeatherService();
        System.out.println(client.getWeatherForecastByLocation(47.6062, -122.3321));
        System.out.println(client.getAlerts("NY"));
    }

}