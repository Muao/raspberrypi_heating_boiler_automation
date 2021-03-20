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

    public static ComPortDataEntity getDailyPower(ComPortDataEntity input){
        final ComPortDataEntity result = new ComPortDataEntity();
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



    public static String comPortDataEntryToMessage(ComPortDataEntity entity){
        final StringBuilder sb = new StringBuilder();

        sb.append("Report for night of ").append(entity.getDate().toLocalDate()).append("\n")
                .append("1st flor average = ").append(entity.getCurrentPort1()).append("W \n")
                .append("1st flor temperature = ").append(entity.getTempPort1()).append("\u2103 \n")
                .append("2nd flor average = ").append(entity.getCurrentPort2()).append("W \n")
                .append("2nd flor temperature = ").append(entity.getTempPort2()).append("\u2103 \n")
                .append("Boiler average = ").append(entity.getCurrentPort3()).append("W \n")
                .append("Boiler max temp = ").append(entity.getTempPort3()).append("\u2103 \n")
                .append("Outdoor temp average = ").append(entity.getTempPort4()).append("\u2103 \n");
        return sb.toString();
    }

    public static String dailyReportToMessage(ComPortDataEntity entity) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Report for day: ").append(entity.getDate().toLocalDate()).append("\n")
                .append("Total daily power: ").append(getSumOfPower(entity)).append("W; \n")
                .append("-----------------------------").append("\n")
                .append("1st flor heating: ").append(entity.getCurrentPort1()).append("W, ")
                .append("Average temp: ").append(entity.getTempPort1()).append("\u2103; \n")
                .append("2nd flor heating: ").append(entity.getCurrentPort2()).append("W, ")
                .append("Average temp: ").append(entity.getTempPort2()).append("\u2103; \n")
                .append("Hot water: ").append(entity.getCurrentPort3()).append("W \n")
                .append("Max output temp: ").append(entity.getTempPort2()).append("\u2103.");
        return sb.toString();
    }

    private static String getSumOfPower(ComPortDataEntity entity){
        final double sum = entity.getCurrentPort1() + entity.getCurrentPort2() + entity.getCurrentPort3();
        return String.valueOf(sum);
    }
}
