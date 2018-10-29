package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class StartDateArgumentValidatorTest {

    @InjectMocks
    StartDateArgumentValidator startDateArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNoArgumentPresented() throws Exception {
        startDateArgumentValidator.validate(new DefaultApplicationArguments(new String[]{""}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenEmptyArgumentValuePresented() throws Exception {
        startDateArgumentValidator.validate(new DefaultApplicationArguments(new String[]{"--startDate"}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNotParsableDatePresented() throws Exception {
        startDateArgumentValidator.validate(new DefaultApplicationArguments(new String[]{"--startDate=10.01.2018"}));
    }
}
