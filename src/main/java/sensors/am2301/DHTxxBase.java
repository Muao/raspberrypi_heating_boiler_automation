package sensors.am2301;

import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Gpio;

public abstract class  DHTxxBase implements DHTxx {
  private static final int DHT_MAXCOUNT = 32000;
  private static final int DHT_PULSES = 41;

  private Pin pin;

  public DHTxxBase(Pin pin) {
    this.pin = pin;
  }

  public Pin getPin() {
    return pin;
  }

  public void setPin(Pin pin) {
    this.pin = pin;
  }

  @Override
  public void init() throws Exception {
    /*
     * Initialize GPIO library.
     */
    int wiringPiSetup = Gpio.wiringPiSetup();
    System.out.println("wiringPiSetup = " + wiringPiSetup);
    if (wiringPiSetup == -1) {
      throw new Exception("DHT_ERROR_GPIO");
    }
  }

  protected int[] getRawData() throws Exception {
    /*
     * Store the count that each DHT bit pulse is low and high. Make sure array
     * is initialized to start at zero.
     */
    int pulseCounts[] = new int[DHT_PULSES * 2];

    /*
     * Set pin to output.
     */
    int address = pin.getAddress();
    System.out.println("pin addr " + address);
    try {
      Gpio.pinMode(address, Gpio.OUTPUT);
    } catch(Exception e) {
      System.out.println("----> Gpio.pinMode " + e.getMessage());
    }
    /*
     * Bump up process priority and change scheduler to try to try to make
     * process more 'real time'.
     */
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    /*
     * Set pin high for ~500 milliseconds.
     */
    Gpio.digitalWrite(pin.getAddress(), Gpio.HIGH);
    Gpio.delay(500);

    /*
     * The next calls are timing critical and care should be taken to ensure no
     * unnecessary work is done below.
     */

    /*
     * Set pin low for ~20 milliseconds.
     */
    Gpio.digitalWrite(pin.getAddress(), Gpio.LOW);
    Gpio.delay(500);

    /*
     * Set pin at input.
     */
    Gpio.pinMode(pin.getAddress(), Gpio.INPUT);

    /*
     * Need a very short delay before reading pins or else value is sometimes
     * still low.
     */
    /*
     * for (int i = 0; i < 50; ++i) {}
     */

    /*
     * Wait for DHT to pull pin low.
     */
    long count = 0;
    while (Gpio.digitalRead(pin.getAddress()) == 1) {

      if (++count >= DHT_MAXCOUNT) {
        /*
         * Timeout waiting for response.
         */
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        throw new Exception("DHT_ERROR_TIMEOUT");
      }
    }

    // Record pulse widths for the expected result bits.
    for (int i = 0; i < DHT_PULSES * 2; i += 2) {
      /*
       * Count how long pin is low and store in pulseCounts[i]
       */
      while (Gpio.digitalRead(pin.getAddress()) == 0) {
        if (++pulseCounts[i] >= DHT_MAXCOUNT) {
          /*
           * Timeout waiting for response.
           */
          Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
          throw new Exception("DHT_ERROR_TIMEOUT: " + pulseCounts[i] + " pulses, " + i);
        }
      }
      /*
       * Count how long pin is high and store in pulseCounts[i+1]
       */
      while (Gpio.digitalRead(pin.getAddress()) == 1) {
        if (++pulseCounts[i + 1] >= DHT_MAXCOUNT) {
          /*
           * Timeout waiting for response.
           */
          Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
          throw new Exception("DHT_ERROR_TIMEOUT: " + pulseCounts[i + 1] + " pulses, " + i);
        }
      }
    }

    /*
     * Done with timing critical code, now interpret the results.
     */

    /*
     * Drop back to normal priority.
     */
    Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

    /*
     * Compute the average low pulse width to use as a 50 microsecond reference
     * threshold. Ignore the first two readings because they are a constant 80
     * microsecond pulse.
     */
    long threshold = 0;
    for (int i = 2; i < DHT_PULSES * 2; i += 2) {
      threshold += pulseCounts[i];
    }
    threshold /= DHT_PULSES - 1;

    /*
     * Interpret each high pulse as a 0 or 1 by comparing it to the 50us
     * reference. If the count is less than 50us it must be a ~28us 0 pulse, and
     * if it's higher then it must be a ~70us 1 pulse.
     */
    int data[] = new int[5];
    for (int i = 3; i < DHT_PULSES * 2; i += 2) {
      int index = (i - 3) / 16;
      data[index] <<= 1;
      if (pulseCounts[i] >= threshold) {
        /*
         * One bit for long pulse.
         */
        data[index] |= 1;
      }
      /*
       * Else zero bit for short pulse.
       */
    }
    return data;
  }

}
