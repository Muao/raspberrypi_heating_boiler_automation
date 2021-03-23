package utilites;

import DAO.entities.ComPortDataEntity;
import DTO.ComPortDataMinMaxTemp;
import DTO.Temperature;

import java.util.ArrayList;

public class ComPortDataUtility {

    public static ComPortDataMinMaxTemp getAverageMinMax(ArrayList<ComPortDataEntity> input){
        final ComPortDataEntity averageEntity = getAverageObject(input);

        final Temperature minOutdoorTemp = input.stream().min(new ComPortOutdoorTemperatureComparator()).map(Temperature::new).orElse(null);
        final Temperature maxOutdoorTemp = input.stream().max(new ComPortOutdoorTemperatureComparator()).map(Temperature::new).orElse(null);

        return new ComPortDataMinMaxTemp(averageEntity, minOutdoorTemp, maxOutdoorTemp);
    }

        //hibernate can't save inheritance object like a supper
    public static ComPortDataEntity getAverageObject(ArrayList<ComPortDataEntity> input) {
        final Double[] averageData = new Double[]{
                input.stream().mapToDouble(ComPortDataEntity::getCurrentPort1).average().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getCurrentPort2).average().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getCurrentPort3).average().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getCurrentPort4).average().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getTempPort1).average().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getTempPort2).average().stream().map(Math::round).findFirst().orElse(0),
                //it's the hot water. needs max result
                input.stream().mapToDouble(ComPortDataEntity::getTempPort3).max().stream().map(Math::round).findFirst().orElse(0),
                input.stream().mapToDouble(ComPortDataEntity::getTempPort4).average().stream().map(Math::round).findFirst().orElse(0)
        };
        return new ComPortDataEntity(averageData);
    }


    public static ComPortDataMinMaxTemp getDailyPower(ComPortDataMinMaxTemp input){
        final ComPortDataMinMaxTemp result = new ComPortDataMinMaxTemp(input.getMinOutdoorTemp(), input.getMaxOutdoorTemp());
        result.setCurrentPort1(input.getCurrentPort1() * 24);
        result.setCurrentPort2(input.getCurrentPort2() * 24);
        result.setCurrentPort3(input.getCurrentPort3() * 24);
        result.setCurrentPort4(input.getCurrentPort4() * 24);
        result.setTempPort1(input.getTempPort1());
        result.setTempPort2(input.getTempPort2());
        result.setTempPort3(input.getTempPort3());
        result.setTempPort4(input.getTempPort4());
        result.setDate(input.getDate());
        return result;
    }
}
