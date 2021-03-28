package relay;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class RelayController {
//    private final GpioPinDigitalOutput p21;//relay#1
//    private final GpioPinDigitalOutput p22;//relay#2
//    private final GpioPinDigitalOutput p23;//relay#3
//    private final GpioPinDigitalOutput p24;//relay#5
//    private final GpioPinDigitalOutput p25;//relay#8
//    private final GpioPinDigitalOutput p27;//relay#4
    private final GpioPinDigitalOutput p28;//relay#6
    private final GpioPinDigitalOutput p29;//relay#7

    private static RelayController instance;

    public static RelayController getInstance(){
        if (instance == null){
            instance = new RelayController();
        }
        return instance;
    }
//it's a singleton for setting state on init
    private RelayController() {
//        this.p21 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_21);
//        this.p21.setState(true);
//        this.p22 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_22);
//        this.p22.setState(true);
//        this.p23 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_23);
//        this.p23.setState(true);
//        this.p24 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_24);
//        this.p24.setState(true);
//        this.p25 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_25);
//        this.p25.setState(true);
//        this.p27 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_27);
//        this.p27.setState(true);
        this.p28 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_28);
        this.p28.setState(true);
        this.p29 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_29);
        this.p29.setState(true);
    }

    public String stopFirstFlourHeating(){
        if(this.p29.isLow()) {
            this.p29.setState(true);
        }
        return firstFloreState();
    }

    public String startFirstFlourHeating(){
        if(this.p29.isHigh()) {
            this.p29.setState(false);
        }
        return firstFloreState();
    }

    public String stopSecondFlourHeating(){
        if(this.p29.isLow()) {
            this.p28.setState(true);
        }
        return secondFloreState();
    }

    public String startSecondFlourHeating(){
        if(this.p28.isHigh()) {
            this.p28.setState(false);
        }
        return secondFloreState();
    }

    public String secondFloreState(){
        final String stage1 = this.p28.getState().isHigh() ? "OFF" : "ON";
        final String stage2 = "OFF";
        return String.format("Second Flour \n stage 1: %s, \n stage 2: %s.\n-----\n", stage1, stage2);
    }

    public String firstFloreState(){
        final String stage1 = this.p29.getState().isHigh() ? "OFF" : "ON";
        final String stage2 = "OFF";
        return String.format("First Flour \n stage 1: %1s, \n stage 2: %2s.\n-----\n", stage1, stage2);
    }

    public boolean firstFloreStateBool(){
        return !this.p29.getState().isHigh();
    }


}
