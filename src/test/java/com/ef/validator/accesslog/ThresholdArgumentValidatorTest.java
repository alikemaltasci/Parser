package com.ef.validator.accesslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ef.validator.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class ThresholdArgumentValidatorTest {

    private static final String ERROR_MESSAGE_NO_ARG = "threshold argument has to be provided.";
    @InjectMocks
    ThresholdArgumentValidator thresholdArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsErrorWhenNoArgumentValuePresented() {
        ValidationResult validationResult = thresholdArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{""}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenEmptyArgumentValuePresented() {
        ValidationResult validationResult = thresholdArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--threshold"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }
}
