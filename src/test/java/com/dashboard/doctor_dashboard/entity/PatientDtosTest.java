package com.dashboard.doctor_dashboard.entity;

import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientListDto;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

 class PatientDtosTest {
    @Test
     void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(PatientDto.class);
        new BeanTester().testBean(PatientListDto.class);
    }
 }

