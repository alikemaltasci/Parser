package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class DurationArgumentValidatorTest {

    @InjectMocks
    DurationArgumentValidator durationArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNoArgPresented() throws Exception {
        durationArgumentValidator.validate(new DefaultApplicationArguments(new String[]{}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenWrongArgumentValuePresented() throws Exception {
        durationArgumentValidator.validate(new DefaultApplicationArguments(new String[]{"--duration=Daily"}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenEmptyArgumentValuePresented() throws Exception {
        durationArgumentValidator.validate(new DefaultApplicationArguments(new String[]{"--duration"}));
    }
}
