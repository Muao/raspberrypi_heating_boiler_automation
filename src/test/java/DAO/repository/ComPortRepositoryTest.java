package DAO.repository;

import DAO.entities.ComPortDataEntity;
import DAO.servises.ComPortDataService;
import DTO.ComPortDataMinMaxTemp;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ComPortRepositoryTest {

    @Ignore
    @Test
    public void getLastNightData() {
        final ComPortDataMinMaxTemp lastNightData = ComPortDataService.getLastNightData();
        System.out.println(lastNightData);
    }

    @Ignore
    @Test
    public void getAverageDayData(){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy");

        final String date = "20.03.21";

        final LocalDate localDate = LocalDate.parse(date, formatter);

        final ComPortDataEntity averageDay = ComPortDataService.getAverage24HourData(localDate);
        System.out.println(averageDay);
    }
}
