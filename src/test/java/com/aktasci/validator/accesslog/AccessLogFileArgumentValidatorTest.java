package com.aktasci.validator.accesslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.aktasci.data.repository.AccessLogRepository;
import com.aktasci.validator.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class AccessLogFileArgumentValidatorTest {

    private static final String ERROR_MESSAGE_NO_ARG = "Since access log file hasn't been parsed yet, accesslog argument has to be provided.";
    private static final String ERROR_MESSAGE_NO_FILE = "There is no file in the specified path: Optional[nofile.log]. Please check the path again.";

    @Mock
    private AccessLogRepository accessLogRepository;

    @InjectMocks
    private AccessLogFileArgumentValidator accessLogFileArgumentValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsErrorWhenNoArgPresentedAndNoParsingHappened() {
        when(accessLogRepository.findLatestAccessDate()).thenReturn(null);
        ValidationResult validationResult = accessLogFileArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenNotRealExistingFilePresented() {
        ValidationResult validationResult = accessLogFileArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--accesslog=nofile.log"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_FILE, validationResult.getErrors().get(0));
    }

    @Test
    public void returnsErrorWhenEmptyArgumentPresented() {
        ValidationResult validationResult = accessLogFileArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--accesslog"}));
        assertTrue(validationResult.hasErrors());
        assertEquals(1, validationResult.getErrors().size());
        assertEquals(ERROR_MESSAGE_NO_ARG, validationResult.getErrors().get(0));
    }
}
