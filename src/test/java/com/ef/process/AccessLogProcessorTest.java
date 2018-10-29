package com.ef.process;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ef.data.repository.BlockedClientRepository;
import com.ef.exception.NotValidArgumentException;
import com.ef.validator.accesslog.AccessLogArgumentValidator;
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
        doThrow(new NotValidArgumentException("")).when(accessLogArgumentValidator).validate(any());
        accessLogProcessor.process(null);

        verify(blockedClientRepository, times(0)).saveAll(any());
    }

    @Test
    public void processWhenArgumentsAreValid() throws Exception {
        doNothing().when(accessLogArgumentValidator).validate(any());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(any());

        accessLogProcessor.process(getValidArguments("daily"));

        verify(blockedClientRepository, times(1)).saveAll(any());
    }

    @Test
    public void processWhenDurationDaily() throws Exception {
        doNothing().when(accessLogArgumentValidator).validate(any());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(LocalDateTime.class);
        when(blockedClientRepository
                .findClientsToBeBlocked(any(LocalDateTime.class), (LocalDateTime) valueCapture.capture(),
                        any(Long.class))).thenReturn(new ArrayList<>());

        accessLogProcessor.process(getValidArguments("daily"));

        assertEquals("2017-01-02T15:00", valueCapture.getValue().toString());
    }

    @Test
    public void processWhenDurationHourly() throws Exception {
        doNothing().when(accessLogArgumentValidator).validate(any());
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(LocalDateTime.class);
        when(blockedClientRepository
                .findClientsToBeBlocked(any(LocalDateTime.class), (LocalDateTime) valueCapture.capture(),
                        any(Long.class))).thenReturn(new ArrayList<>());

        accessLogProcessor.process(getValidArguments("hourly"));

        assertEquals("2017-01-01T16:00", valueCapture.getValue().toString());
    }

    private ApplicationArguments getValidArguments(String duration) {
        return new DefaultApplicationArguments(
                new String[]{"--accesslog=access-test.log", "--startDate=2017-01-01.15:00:00", "--duration=" + duration,
                        "--threshold=200"});
    }
}
