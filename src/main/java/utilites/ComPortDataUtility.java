package utilites;

import DAO.entities.ComPortDataEntity;

import java.util.ArrayList;

public class ComPortDataUtility {

    static ComPortDataEntity getAverageObject(ArrayList<ComPortDataEntity> input){
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
}
