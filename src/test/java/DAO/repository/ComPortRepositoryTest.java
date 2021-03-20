package DAO.repository;

import DAO.entities.ComPortDataEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComPortRepositoryTest {

    @Ignore
    @Test
    public void getLastNightData() {
        final ComPortDataEntity lastNightData = new ComPortRepository().getLastNightData();
        System.out.println(lastNightData);
    }
}
