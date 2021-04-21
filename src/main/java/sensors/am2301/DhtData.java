package sensors.am2301;

public class DhtData {
  private double temperature;
  private double humidity;

  public DhtData() {
    super();
  }

  public DhtData(double temperature, double humidity) {
    super();
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  @Override
  public String toString() {
    return "Temperature: " + temperature + "°C\nHumidity: " + humidity + "%";
  }
}
