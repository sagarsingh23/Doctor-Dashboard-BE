package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.jwt.entities.DoctorClaims;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

 class DoctorDtosTest {

    @Test
     void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(DoctorListDto.class);
        new BeanTester().testBean(DoctorClaims.class);
    }


}
