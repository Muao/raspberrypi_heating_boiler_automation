package utilites;

import DAO.repository.ComPortRepository;

import java.util.ArrayList;

public class ComPortDataStorage extends ArrayList<ComportData> {
    private final ComPortRepository repository = new ComPortRepository();
    @Override
    public boolean add(ComportData comportData) {
//
        //if(super.size() == 434) //3600 sec per hour. get new data every 9 sec.
        // for 5 minutes = 30
        if (super.size() == 60) {
            final ComportData averageComPortData = new ComportData(
                    super.stream().mapToDouble(ComportData::getCurrentPort1).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getCurrentPort2).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getCurrentPort3).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getCurrentPort4).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getTempPort1).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getTempPort2).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getTempPort3).average().stream().map(Math::round).findFirst().orElse(0),
                    super.stream().mapToDouble(ComportData::getTempPort4).average().stream().map(Math::round).findFirst().orElse(0));
            repository.save(averageComPortData);
            super.clear();
            System.out.println("flushed!!!!!!!!!!!!!!!!!!");
            return super.add(comportData);
        } else {
            System.out.println("saved!");
            return super.add(comportData);
        }
    }
}
