package com.dashboard.doctor_dashboard.exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class GoogleLoginExceptionTest {

    @Test(expected = GoogleLoginException.class)
    public void googleLoginExceptionTest() {
        throw new GoogleLoginException("test for Google Login Exception,error code:401");
    }
}