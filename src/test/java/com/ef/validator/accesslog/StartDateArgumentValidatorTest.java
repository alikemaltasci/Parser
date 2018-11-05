package com.ef.validator.accesslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ef.validator.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class StartDateArgumentValidatorTest {

    private static final String ERROR_MESSAGE_NO_ARG = "startDate argument has to be provided in the format: yyyy-MM-dd.HH:mm:ss";
    @InjectMocks
    StartDateArgumentValidator startDateArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsErrorWhenNoArgumentPresented() {
        ValidationResult validationResult = startDateArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{""}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenEmptyArgumentValuePresented() {
        ValidationResult validationResult = startDateArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--startDate"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenNotParsableDatePresented() {
        ValidationResult validationResult = startDateArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--startDate=10.01.2018"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }
}
