package com.dashboard.doctor_dashboard.exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ResourceNotFoundExceptionTest {

    @Test(expected = ResourceNotFoundException.class)
    public void resourceNotFoundTest() {
        throw new ResourceNotFoundException("test for resource not found, error code: 404");
    }

}