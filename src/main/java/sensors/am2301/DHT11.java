package sensors.am2301;

import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Gpio;

public class DHT11 extends DHTxxBase {

  private static final int DHT_WAIT_INTERVAL = 2000;

  public DHT11(Pin pin) {
    super(pin);
  }

  @Override
  public DhtData getData() throws Exception {
    int atempts = 0;
    while (true) {
      try {
        int[] data = getRawData();

        /*
         * Verify checksum of received data.
         */
        if (data[4] != ((data[0] + data[1] + data[2] + data[3]) & 0xFF)) {
          throw new Exception("DHT_ERROR_CHECKSUM");
        }
        /*
         * Get humidity and temp for DHT11 sensor.
         */
        double humidity = data[0];
        if (data[1] > 0) {
          if (data[1] <= 9) {
            humidity += data[1] / 10.0;
          } else {
            humidity += data[1] / 100.0;
          }
        }
        double temperature = data[2];
        if (data[3] > 0) {
          if (data[3] <= 9) {
            temperature += data[3] / 10.0;
          } else {
            temperature += data[3] / 100.0;
          }
        }
        return new DhtData(temperature, humidity);
      } catch (Exception e) {
        atempts++;
        if (atempts <= 3) {
          Gpio.delay(DHT_WAIT_INTERVAL);
          continue;
        }
        throw new Exception("Atempts " + atempts, e);
      }
    }
  }

  @Override
  public String toString() {
    return "DHT11, pin: " + getPin();
  }
}
