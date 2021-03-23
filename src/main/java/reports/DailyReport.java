package reports;

import DAO.entities.ComPortDataEntity;
import DTO.ComPortDataMinMaxTemp;

import java.time.format.DateTimeFormatter;

public class DailyReport {
    public static String get(ComPortDataMinMaxTemp entity){
        final StringBuilder sb = new StringBuilder();
        sb.append("Report for day: ").append(entity.getDate().toLocalDate()).append("\n")
                .append("---------------------------------\n")
                .append("Total daily power: ").append(getSumOfPower(entity)).append("W; \n")
                .append("---------------------------------\n")
                .append("1st flor heating: ").append(entity.getCurrentPort1()).append("W, ")
                .append("Average temp: ").append(entity.getTempPort1()).append("\u2103; \n")
                .append("---------------------------------\n")
                .append("2nd flor heating: ").append(entity.getCurrentPort2()).append("W, ")
                .append("Average temp: ").append(entity.getTempPort2()).append("\u2103;\n")
                .append("---------------------------------\n")
                .append("Hot water: ").append(entity.getCurrentPort3()).append("W \n")
                .append("Max hot water temp: ").append(entity.getTempPort2()).append("\u2103;\n")
                .append("---------------------------------\n")
                .append("Average outdoor temp: ").append(entity.getTempPort4()).append("\u2103;\n")
                .append("Min outdoor temp: ").append(entity.getMinOutdoorTemp().getTemperature()).append("\u2103, at ")
                .append(entity.getMinOutdoorTemp().getTime().format(DateTimeFormatter.ofPattern("hh:mm"))).append(";\n")
                .append("Max outdoor temp: ").append(entity.getMaxOutdoorTemp().getTemperature()).append("\u2103, at ")
                .append(entity.getMaxOutdoorTemp().getTime().format(DateTimeFormatter.ofPattern("hh:mm"))).append(".");
        return sb.toString();
    }

    private static String getSumOfPower(ComPortDataEntity entity){
        final double sum = entity.getCurrentPort1() + entity.getCurrentPort2() + entity.getCurrentPort3();
        return String.valueOf(sum);
    }
}
