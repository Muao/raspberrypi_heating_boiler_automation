package reports;

import DAO.entities.ComPortDataEntity;

public class DailyReport {
    public static String get(ComPortDataEntity entity){
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
