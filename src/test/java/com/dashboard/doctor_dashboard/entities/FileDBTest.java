package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.entities.report.ResponseFile;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class FileDBTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(FileDB.class);
        new BeanTester().testBean(ResponseFile.class);
    }
}
