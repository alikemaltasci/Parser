package com.ef.validator.accesslog;

import static org.mockito.Mockito.when;

import com.ef.data.repository.AccessLogRepository;
import com.ef.exception.NotValidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;

public class AccessLogFileArgumentValidatorTest {

    @Mock
    private AccessLogRepository accessLogRepository;

    @InjectMocks
    private AccessLogFileArgumentValidator accessLogFileArgumentValidator;

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNoArgPresentedAndNoParsingHappened() throws Exception {
        when(accessLogRepository.findLatestAccessDate()).thenReturn(null);
        accessLogFileArgumentValidator.validate(new DefaultApplicationArguments(new String[]{}));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenNotRealExistingFilePresented() throws Exception {
        accessLogFileArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--accesslog=nofile.log"}));
    }

    @Test(expected = NotValidArgumentException.class)
    public void throwsExceptionWhenEmptyArgumentPresented() throws Exception {
        accessLogFileArgumentValidator
                .validate(new DefaultApplicationArguments(new String[]{"--accesslog"}));
    }
}
