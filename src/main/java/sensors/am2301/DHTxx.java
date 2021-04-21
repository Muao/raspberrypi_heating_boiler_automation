package sensors.am2301;

import com.pi4j.io.gpio.Pin;

public interface DHTxx {
  public void init() throws Exception;

  public Pin getPin();

  public void setPin(Pin pin);

  public DhtData getData() throws Exception;
}
