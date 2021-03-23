package DTO;

import DAO.entities.ComPortDataEntity;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class Temperature {
    private final double temperature;
    private final LocalTime time;

    public Temperature(ComPortDataEntity comPortDataMinMaxTemp) {
        this.temperature = comPortDataMinMaxTemp.getTempPort4();
        this.time = comPortDataMinMaxTemp.getDate().toLocalTime();
    }
}
