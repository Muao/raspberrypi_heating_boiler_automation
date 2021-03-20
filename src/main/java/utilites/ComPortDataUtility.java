package utilites;

import DAO.entities.ComPortDataEntity;

import java.util.ArrayList;

public class ComPortDataUtility {

    public static ComPortDataEntity getAverageObject(ArrayList<ComPortDataEntity> input){
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

    public static String comPortDataEntryToMessage(ComPortDataEntity entity){
        final StringBuilder sb = new StringBuilder();

        sb.append("Report for night of ").append(entity.getDate().toLocalDate()).append("\n")
                .append("1st flor average = ").append(entity.getCurrentPort1()).append("W \n")
                .append("1st flor temperature = ").append(entity.getTempPort1()).append("\u2103 \n")
                .append("2st flor average = ").append(entity.getCurrentPort2()).append("W \n")
                .append("2st flor temperature = ").append(entity.getTempPort2()).append("\u2103 \n")
                .append("Boiler average = ").append(entity.getCurrentPort3()).append("W \n")
                .append("Boiler max t = ").append(entity.getTempPort3()).append("\u2103 \n")
                .append("Outdoor temp average = ").append(entity.getTempPort4()).append("\u2103 \n");
        return sb.toString();
    }
}
