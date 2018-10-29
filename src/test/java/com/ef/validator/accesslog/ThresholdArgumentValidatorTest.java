package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class ThresholdArgumentValidatorTest {

    @InjectMocks
    ThresholdArgumentValidator thresholdArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNoArgumentValuePresented() throws Exception {
        thresholdArgumentValidator.validate(new DefaultApplicationArguments(new String[]{""}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenEmptyArgumentValuePresented() throws Exception {
        thresholdArgumentValidator.validate(new DefaultApplicationArguments(new String[]{"--threshold"}));
    }
}
