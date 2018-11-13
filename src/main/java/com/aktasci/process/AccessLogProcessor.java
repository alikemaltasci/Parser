package com.aktasci.process;

import com.aktasci.data.model.BlockedClient;
import com.aktasci.data.repository.BlockedClientRepository;
import com.aktasci.util.ArgumentGetter;
import com.aktasci.validator.ValidationResult;
import com.aktasci.validator.accesslog.AccessLogArgumentValidator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccessLogProcessor {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";
    private static final String ARG_ACCESS_LOG = "accesslog";
    private static final String ARG_START_DATE = "startDate";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_THRESHOLD = "threshold";
    private static final String DUR_DAILY = "daily";
    private static final String DUR_HOURLY = "hourly";
    private static final String PAR_FILE_PATH = "filePath";
    private static final String PAR_UNIQUE = "uniqueness";

    @Autowired
    private AccessLogArgumentValidator accessLogArgumentValidator;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job accessLogReportJob;

    @Autowired
    private BlockedClientRepository blockedClientRepository;

    public void process(final ApplicationArguments args)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        ValidationResult validationResult = accessLogArgumentValidator.validate(args);
        if (validationResult.hasErrors()) {
            validationResult.getErrors().forEach(log::error);
            return;
        }

        parseFileIfNeeded(args);

        String todayStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        String startDateStr = ArgumentGetter.getArgument(args, ARG_START_DATE).orElse(todayStr);
        LocalDateTime startDate = LocalDateTime
                .parse(startDateStr, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        String duration = ArgumentGetter.getArgument(args, ARG_DURATION).orElse(DUR_HOURLY);

        LocalDateTime endDate = evaluateEndDate(startDate, duration);

        String thresholdStr = ArgumentGetter.getArgument(args, ARG_THRESHOLD)
                .orElseGet(() -> String.valueOf(Long.MAX_VALUE));
        Long threshold = Long.valueOf(thresholdStr);

        List<BlockedClient> clientsToBeBlocked = blockedClientRepository
                .findClientsToBeBlocked(startDate, endDate, threshold);

        setAllDescriptions(clientsToBeBlocked, startDate, duration, endDate, threshold);

        logTheResults(startDate, duration, threshold, clientsToBeBlocked);

        blockedClientRepository.saveAll(clientsToBeBlocked);
    }

    private void logTheResults(final LocalDateTime startDate, final String duration, final Long threshold,
            final List<BlockedClient> clientsToBeBlocked) {
        log.info("***> Blocked Clients are as below(startDate: {}, duration: {}, threshold: {})", startDate,
                duration, threshold);
        log.info("***> Ip : Request Count");

        clientsToBeBlocked.stream().sorted(Comparator.comparing(BlockedClient::getRequestCount).reversed())
                .collect(Collectors.toList())
                .forEach(blockedClient -> log
                        .info("***> " + blockedClient.getIp() + " : " + blockedClient.getRequestCount()));
    }

    private void setAllDescriptions(final List<BlockedClient> clientsToBeBlocked, final LocalDateTime startDate,
            final String duration, final LocalDateTime endDate,
            final Long threshold) {
        String durationStr = DUR_HOURLY.equals(duration) ? " (one hour) " : " (24 hours) ";
        String description =
                "This ip has been blocked. Because it made more than " + threshold + " requests starting from "
                        + startDate + " to " + endDate + durationStr;
        clientsToBeBlocked.forEach(blockedClient -> blockedClient.setDescription(description));
    }

    private LocalDateTime evaluateEndDate(final LocalDateTime startDate, final String duration) {
        LocalDateTime endDate = null;
        if (DUR_HOURLY.equals(duration)) {
            endDate = startDate.plusHours(1).truncatedTo(ChronoUnit.HOURS);
        } else if (DUR_DAILY.equals(duration)) {
            endDate = startDate.plusDays(1).truncatedTo(ChronoUnit.HOURS);
        }
        return endDate;
    }

    private void parseFileIfNeeded(final ApplicationArguments args)
            throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Optional<String> accessLogArg = ArgumentGetter.getArgument(args, ARG_ACCESS_LOG);
        if (accessLogArg.isPresent()) {
            jobLauncher.run(accessLogReportJob,
                    new JobParametersBuilder().addString(PAR_FILE_PATH, accessLogArg.get())
                            .addLong(PAR_UNIQUE, System.nanoTime()).toJobParameters());

        }
    }
}
