package api.Weather;

import static io.restassured.RestAssured.given;

import api.spec.Specifications;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Апи тесты с Pojo классами")
@Feature("Api Weather Pojo")
public class WeatherPojoTest {
    private static String URL = "http://api.weatherstack.com/";
    private static String accessKey = "1f6067f5f58ec36fa0266c24ef3e3b50";

    /**
     Позитивный тест:
     1.	Запросить текущую погоду минимум по четырем городам на свой выбор.
     2.	Распарсить результат, сравнить с ожидаемыми значениями из тестового набора (language, location и т.п.).
     Ожидаемые тестовые данные можно определить или задать для каждого города корректные
     (localtime, utc_offset и т.п.),
     либо можно задать\сгенерировать случайным образом с соблюдением формата (wind_speed, temperature и пр.).
     3.	Вывести расхождения по результату сравнения по каждому значению в лог.
     Негативный тест:
     1. Получить 4 варианта ошибок из списка API Errors (на выбор), сравнить с ожидаемым результатом.
     */


    enum City {

        NEW_YORK("New York"),
        MOSCOW("Moscow"),
        ROME("Rome"),
        TOKIO("Tokio");


        public String name;

        public String getName() {
            return name;
        }

        City(String name) {
            this.name = name;
        }

    }


    @Test
    @DisplayName("Получить ошибку 101")
    public void checkStatus101Test(){

        Response response = given()
                .when()
                .get(String.format(URL + "current?access_key=&query=%s", City.MOSCOW.getName()))
                .then()
                .log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();


        Allure.step(String.format("Получена ошибка: %s, %s", jsonPath.get("error.code"),
            jsonPath.get("error.info")));

    }

    @Test
    @DisplayName("Получить ошибку 105")
    public void checkStatus105Test(){

        URL = "https://api.weatherstack.com/";
        Response response = given()
            .when()
            .get(String.format(URL + "current?access_key=&query=%s", City.MOSCOW.getName()))
            .then()
            .log().all()
            .extract().response();

        JsonPath jsonPath = response.jsonPath();


        Allure.step(String.format("Получена ошибка: %s, %s", jsonPath.get("error.code"),
            jsonPath.get("error.info")));

    }

    @Test
    @DisplayName("Получить ошибку 404")
    public void checkStatus404Test(){

        Response response = given()
            .when()
            .get(String.format(URL + "current?access_key=%s&query=%s", accessKey, City.MOSCOW.getName()))
            .then()
            .log().all()
            .extract().response();

        JsonPath jsonPath = response.jsonPath();


        Allure.step(String.format("Получена ошибка: %s, %s", jsonPath.get("error.code"),
            jsonPath.get("error.info")));

    }


    @ParameterizedTest
    @EnumSource(City.class)
    @DisplayName("Запросить текущую погоду по конкретному городу")
    public void checkAvatarContainsIdTest(City cityName){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        
        Response response = given()
            .when()
            .get(String.format("current?access_key=%s&query=%s", accessKey, cityName))
            .then()
            .log().all()
            .extract().response();

        JsonPath jsonPath = response.jsonPath();

        String nowInNY = ZonedDateTime.now(ZoneId.of(jsonPath.get("location.timezone_id")))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String utcOffset = ZonedDateTime.now(ZoneId.of(jsonPath.get("location.timezone_id")))
            .getOffset().getId().substring(1,5).replace(":", ".").replace("+", "");

        jsonPath.get("utc_offset");

        Assertions.assertEquals(jsonPath.get("location.utc_offset"), utcOffset);


        Assertions.assertEquals(jsonPath.get("location.name"), cityName.getName());
        Allure.step("Название города совпадает с заданным");


        Allure.step(String.format("Температура в городе %s : %s", cityName.getName(),
            jsonPath.get("current.temperature")));

        Assertions.assertEquals(jsonPath.get("location.localtime"), nowInNY);
    }
}
