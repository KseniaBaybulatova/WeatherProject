package api.Weather;

public class POJOWeather {

    private String name;
    private String observation_time;
    private Integer temperature;
    private Integer weather_code;
    private Integer wind_speed;

    public POJOWeather(String name, String observation_time, Integer temperature, Integer weather_code,
        Integer wind_speed) {
        this.name = name;
        this.observation_time = observation_time;
        this.temperature = temperature;
        this.weather_code = weather_code;
        this.wind_speed = wind_speed;
    }

    public String getObservation_time() {
        return observation_time;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getWeather_code() {
        return weather_code;
    }

    public Integer getWind_speed() {
        return wind_speed;
    }

    public Object getName() {
        return name;
    }
}
