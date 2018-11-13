package com.aktasci.process;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aktasci.data.repository.BlockedClientRepository;
import com.aktasci.validator.ValidationResult;
import com.aktasci.validator.accesslog.AccessLogArgumentValidator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

public class AccessLogProcessorTest {

    private static final ApplicationArguments EMPTY_ARGUMENT = new DefaultApplicationArguments(new String[]{""});
    private static final String ERROR_MESSAGE = "Error message";
    @Mock
    AccessLogArgumentValidator accessLogArgumentValidator;

    @Mock
    JobLauncher jobLauncher;

    @Mock
    Job accessLogReportJob;

    @Mock
    BlockedClientRepository blockedClientRepository;

    @InjectMocks
    private AccessLogProcessor accessLogProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processWhenArgumentsAreNotValid() throws Exception {
        when(accessLogArgumentValidator.validate(EMPTY_ARGUMENT)).thenReturn(getValidationResultWithError());
        accessLogProcessor.process(EMPTY_ARGUMENT);

        verify(blockedClientRepository, times(0)).saveAll(any());
    }

    private ValidationResult getValidationResultWithError() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(ERROR_MESSAGE);
        return validationResult;
    }

    @Test
    public void processWhenArgumentsAreValid() throws Exception {
        ApplicationArguments validArguments = getValidArguments("daily");
        when(accessLogArgumentValidator.validate(validArguments)).thenReturn(getValidationResultWithoutError());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(any());

        accessLogProcessor.process(validArguments);

        verify(blockedClientRepository, times(1)).saveAll(any());
    }

    private ValidationResult getValidationResultWithoutError() {
        return new ValidationResult();
    }

    @Test
    public void processWhenDurationDaily() throws Exception {
        ApplicationArguments dailyArguments = getValidArguments("daily");
        when(accessLogArgumentValidator.validate(dailyArguments)).thenReturn(getValidationResultWithoutError());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(LocalDateTime.class);
        when(blockedClientRepository
                .findClientsToBeBlocked(any(LocalDateTime.class), (LocalDateTime) valueCapture.capture(),
                        any(Long.class))).thenReturn(new ArrayList<>());

        accessLogProcessor.process(dailyArguments);

        assertEquals("2017-01-02T15:00", valueCapture.getValue().toString());
    }

    @Test
    public void processWhenDurationHourly() throws Exception {
        ApplicationArguments hourlyArguments = getValidArguments("hourly");
        when(accessLogArgumentValidator.validate(hourlyArguments)).thenReturn(getValidationResultWithoutError());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(LocalDateTime.class);
        when(blockedClientRepository
                .findClientsToBeBlocked(any(LocalDateTime.class), (LocalDateTime) valueCapture.capture(),
                        any(Long.class))).thenReturn(new ArrayList<>());

        accessLogProcessor.process(hourlyArguments);

        assertEquals("2017-01-01T16:00", valueCapture.getValue().toString());
    }

    private ApplicationArguments getValidArguments(String duration) {
        return new DefaultApplicationArguments(
                new String[]{"--accesslog=access-test.log", "--startDate=2017-01-01.15:00:00", "--duration=" + duration,
                        "--threshold=200"});
    }
}
