package com.aktasci.validator.accesslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.aktasci.validator.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class DurationArgumentValidatorTest {

    private static final String ERROR_MESSAGE_NO_ARG = "duration argument has to be provided as daily or hourly";
    @InjectMocks
    DurationArgumentValidator durationArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsErrorWhenNoArgPresented() {
        ValidationResult validationResult = durationArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenWrongArgumentValuePresented() {
        ValidationResult validationResult = durationArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--duration=Daily"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenEmptyArgumentValuePresented() {
        ValidationResult validationResult = durationArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--duration"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }
}
