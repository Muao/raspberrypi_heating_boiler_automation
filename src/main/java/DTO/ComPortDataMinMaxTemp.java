package DTO;

import DAO.entities.ComPortDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComPortDataMinMaxTemp extends ComPortDataEntity {
    private final Temperature minOutdoorTemp;
    private final Temperature maxOutdoorTemp;

    public ComPortDataMinMaxTemp(Temperature minOutdoorTemp, Temperature maxOutdoorTemp) {
        this.minOutdoorTemp = minOutdoorTemp;
        this.maxOutdoorTemp = maxOutdoorTemp;
    }

    public ComPortDataMinMaxTemp(ComPortDataEntity entity, Temperature minOutdoorTemp, Temperature maxOutdoorTemp) {
        super(entity);
        this.minOutdoorTemp = minOutdoorTemp;
        this.maxOutdoorTemp = maxOutdoorTemp;
    }

    @Override
    public String toString() {
        return super.getTempPort4() +
                ", minOutdoorTemp=" + minOutdoorTemp +
                ", maxOutdoorTemp=" + maxOutdoorTemp +
                '}';
    }
}
