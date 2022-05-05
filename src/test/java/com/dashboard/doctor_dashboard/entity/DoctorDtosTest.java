package com.dashboard.doctor_dashboard.entity;

import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.jwt.entity.DoctorClaims;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

 class DoctorDtosTest {

    @Test
     void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(DoctorListDto.class);
        new BeanTester().testBean(DoctorClaims.class);
    }


}
