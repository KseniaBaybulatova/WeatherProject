package api.weather;

public enum City {


    NEW_YORK("New York"),
    MOSCOW("Moscow"),
    ROME("Rome"),
    TOKIO("Tokio");


    public String value;

    public String getValue() {
      return value;
    }

    City(String name) {
      this.value = name;
    }


}
