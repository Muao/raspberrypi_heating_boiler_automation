package utilites;

import DAO.entities.ComPortDataEntity;

import java.util.Comparator;

public class ComPortOutdoorTemperatureComparator implements Comparator<ComPortDataEntity> {


    @Override
    public int compare(ComPortDataEntity o1, ComPortDataEntity o2) {
        return (int) (o1.getTempPort4() - o2.getTempPort4());
    }
}
