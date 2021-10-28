package utilites;

import DAO.entities.ComPortDataEntity;
import DAO.repository.ComPortRepository;

import java.util.ArrayList;

public class ComPortDataStorage extends ArrayList<ComPortDataEntity> {

    @Override
    public boolean add(ComPortDataEntity comportData) {
        //if(super.size() == 434) //3600 sec per hour. get new data every 9 sec.
        // for 5 minutes = 30
        //for 30 min = 180
        //for 60 min 390
        if (super.size() == 200) {
            final ComPortDataEntity averageComPortData = ComPortDataUtility.getAverageObject(this);
            ComPortRepository.getInstance().save(averageComPortData);
            super.clear();
            return super.add(comportData);
        } else {
            return super.add(comportData);
        }
    }
}
