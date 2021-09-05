package DAO.servises;

import DAO.repository.ComPortRepository;
import DTO.ComPortDataMinMaxTemp;
import utilites.CalendarUtility;
import utilites.ComPortDataUtility;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class ComPortDataService {

    @Nullable
    public static ComPortDataMinMaxTemp getAverage24HourData(LocalDate date) {
        ComPortDataMinMaxTemp result = null;
        final var resultList = ComPortRepository.getInstance().get(date.atStartOfDay(), date.atStartOfDay().plusDays(1));

        if (resultList.size() != 0) {
            final var averageObject = ComPortDataUtility.getAverageMinMax(resultList);
            averageObject.setDate(date.atStartOfDay());
            result = ComPortDataUtility.getDailyPower(averageObject);
        }
        return result;
    }

    @Nullable
    public static ComPortDataMinMaxTemp getLastNightData() {
        ComPortDataMinMaxTemp result = null;

        final var localDateTimes = CalendarUtility.previousNight();
        final var start = localDateTimes[0];
        final var end = localDateTimes[1];
        final var resultList = ComPortRepository.getInstance().get(start, end);

        if (resultList.size() != 0) {
            final ComPortDataMinMaxTemp averageObject = ComPortDataUtility.getAverageMinMax(resultList);
            averageObject.setDate(end);
            result = averageObject;
        }
        return result;
    }
}
