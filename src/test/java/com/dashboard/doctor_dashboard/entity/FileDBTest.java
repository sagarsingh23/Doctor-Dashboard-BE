package com.dashboard.doctor_dashboard.entity;

import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.entity.report.FileDB;
import com.dashboard.doctor_dashboard.entity.report.ResponseFile;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class FileDBTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(FileDB.class);
        new BeanTester().testBean(ResponseFile.class);
    }
}
