package reports;

import DAO.entities.ComPortDataEntity;

public class LastNightReport {
    public static String get(ComPortDataEntity entity){
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
}
