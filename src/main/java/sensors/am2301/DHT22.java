package sensors.am2301;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.concurrent.*;

public class DHT22 {

  /**
   * Time in nanoseconds to separate ZERO and ONE signals.
   */
  private static final int LONGEST_ZERO = 50000;

  /**
   * PI4J Pin number.
   */
  private int pinNumber;

  /**
   * 40 bit Data from sensor
   */
  private byte[] data = null;

  /**
   * Value of last successful humidity reading.
   */
  private Double humidity = null;

  /**
   * Value of last successful temperature reading.
   */
  private Double temperature = null;

  /**
   * Last read attempt
   */
  private Long lastRead = null;

  /**
   * Constructor with pin used for signal.  See PI4J and WiringPI for
   * pin numbering systems.....
   *
   * @param pin
   */
  public DHT22(Pin pin) {
    pinNumber = pin.getAddress();
  }

  /**
   * Communicate with sensor to get new reading data.
   *
   * @throws Exception if failed to successfully read data.
   */
  private void getData() throws Exception {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    ReadSensorFuture readSensor = new ReadSensorFuture();
    Future<byte[]> future = executor.submit(readSensor);
    // Reset data
    data = new byte[5];
    try {
      data = future.get(3, TimeUnit.SECONDS);
      readSensor.close();
    } catch (TimeoutException e) {
      readSensor.close();
      future.cancel(true);
      executor.shutdown();
      throw e;
    }
    readSensor.close();
    executor.shutdown();
  }

  /**
   * Make a new sensor reading.
   *
   * @throws Exception
   */
  public boolean read() throws Exception {
    checkLastReadDelay();
    lastRead = System.currentTimeMillis();
    getData();
    checkParity();
    humidity = getReadingValueFromBytes(data[0], data[1]);
    temperature = getReadingValueFromBytes(data[2], data[3]);
    lastRead = System.currentTimeMillis();
    return true;
  }

  private void checkLastReadDelay() throws Exception {
    if (Objects.nonNull(lastRead)) {
      if (lastRead > System.currentTimeMillis() - 2000) {
        throw new Exception("Last read was under 2 seconds ago. Please wait longer between reads!");
      }
    }
  }

  private double getReadingValueFromBytes(final byte hi, final byte low) {
    ByteBuffer bb = ByteBuffer.allocate(2);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(hi);
    bb.put(low);
    short shortVal = bb.getShort(0);
    return new Double(shortVal) / 10;
  }

  private void checkParity() throws ParityChheckException {
    if (!(data[4] == (data[0] + data[1] + data[2] + data[3] & 0xFF))) {
      throw new ParityChheckException();
    }
  }

  public Double getHumidity() {
    return humidity;
  }

  public Double getTemperature() {
    return temperature;
  }

  /**
   * Run from command line to loop and make readings.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    System.out.println("Starting DHT22");
    if (Gpio.wiringPiSetup() == -1) {
      System.out.println("GPIO wiringPiSetup Failed!");
      return;
    }

    DHT22 dht22 = new DHT22(RaspiPin.GPIO_05);
    int LOOP_SIZE = 10;
    int countSuccess = 0;
    for (int i=0; i < LOOP_SIZE; i++) {
      try {
        Thread.sleep(3000);
        System.out.println();
        dht22.read();
        System.out.println("Humidity=" + dht22.getHumidity() +
                "%, Temperature=" + dht22.getTemperature() + "*C");

        countSuccess++;
      } catch (TimeoutException e) {
        System.out.println("ERROR: " + e);
      } catch (Exception e) {
        System.out.println("ERROR: " + e);
      }
    }
    System.out.println("Read success rate: "+ countSuccess + " / " + LOOP_SIZE);
    System.out.println("Ending DHT22");
  }

  /**
   * Callable Future for reading sensor.  Allows timeout if it gets stuck.
   */
  private class ReadSensorFuture implements Callable<byte[]>, Closeable {

    private boolean keepRunning = true;

    public ReadSensorFuture() {
      Gpio.pinMode(pinNumber, Gpio.OUTPUT);
      Gpio.digitalWrite(pinNumber, Gpio.HIGH);
    }

    @Override
    public byte[] call() throws Exception {

      // do expensive (slow) stuff before we start.
      byte[] data = new byte[5];
      long startTime = System.nanoTime();

      sendStartSignal();
      waitForResponseSignal();
      for (int i = 0; i < 40; i++) {
        while (keepRunning && Gpio.digitalRead(pinNumber) == Gpio.LOW) {
        }
        startTime = System.nanoTime();
        while (keepRunning && Gpio.digitalRead(pinNumber) == Gpio.HIGH) {
        }
        long timeHight = System.nanoTime() - startTime;
        data[i / 8] <<= 1;
        if ( timeHight > LONGEST_ZERO) {
          data[i / 8] |= 1;
        }
      }
      return data;
    }

    private void sendStartSignal() {
      // Send start signal.
      Gpio.pinMode(pinNumber, Gpio.OUTPUT);
      Gpio.digitalWrite(pinNumber, Gpio.LOW);
      Gpio.delay(1);
      Gpio.digitalWrite(pinNumber, Gpio.HIGH);
    }

    /**
     * AM2302 will pull low 80us as response signal, then
     * AM2302 pulls up 80us for preparation to send data.
     */
    private void waitForResponseSignal() {
      Gpio.pinMode(pinNumber, Gpio.INPUT);
      while (keepRunning && Gpio.digitalRead(pinNumber) == Gpio.HIGH) {
      }
      while (keepRunning && Gpio.digitalRead(pinNumber) == Gpio.LOW) {
      }
      while (keepRunning && Gpio.digitalRead(pinNumber) == Gpio.HIGH) {
      }
    }

    @Override
    public void close() throws IOException {
      keepRunning = false;

      // Set pin high for end of transmission.
      Gpio.pinMode(pinNumber, Gpio.OUTPUT);
      Gpio.digitalWrite(pinNumber, Gpio.HIGH);
    }
  }

  private class ParityChheckException extends Exception {
    private static final long serialVersionUID = 1L;
  }

}
