package reports;

import DTO.ComPortDataMinMaxTemp;

import java.time.format.DateTimeFormatter;

public class LastNightReport {
    public static String get(ComPortDataMinMaxTemp entity){
        final StringBuilder sb = new StringBuilder();

        sb.append("Report for night of ").append(entity.getDate().toLocalDate()).append("\n")
                .append("1st flor average: ").append(entity.getCurrentPort1()).append("W \n")
                .append("1st flor temperature: ").append(entity.getTempPort1()).append("\u2103 \n")
                .append("2nd flor average: ").append(entity.getCurrentPort2()).append("W \n")
                .append("2nd flor temperature: ").append(entity.getTempPort2()).append("\u2103 \n")
                .append("Boiler average: ").append(entity.getCurrentPort3()).append("W \n")
                .append("Boiler max temp: ").append(entity.getTempPort3()).append("\u2103 \n")
                .append("--------------------------------\n")
                .append("Average outdoor temp: ").append(entity.getTempPort4()).append("\u2103 \n")
                .append("Min outdoor temp: ").append(entity.getMinOutdoorTemp().getTemperature()).append("\u2103 at ")
                .append(entity.getMinOutdoorTemp().getTime().format(DateTimeFormatter.ofPattern("hh:mm"))).append("\n")
                .append("Max outdoor temp: ").append(entity.getMaxOutdoorTemp().getTemperature()).append("\u2103 at ")
                .append(entity.getMaxOutdoorTemp().getTime().format(DateTimeFormatter.ofPattern("hh:mm")));
        return sb.toString();
    }
}
