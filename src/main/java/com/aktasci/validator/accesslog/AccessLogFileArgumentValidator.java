package com.aktasci.validator.accesslog;

import com.aktasci.data.repository.AccessLogRepository;
import com.aktasci.util.ArgumentGetter;
import com.aktasci.validator.ArgumentValidator;
import com.aktasci.validator.ValidationResult;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class AccessLogFileArgumentValidator implements ArgumentValidator {

    @Autowired
    private AccessLogRepository accessLogRepository;

    private static final String ARG_ACCESS_LOG = "accesslog";

    @Override
    public ValidationResult validate(final ApplicationArguments args) {
        ValidationResult validationResult = new ValidationResult();
        Optional<String> accessLog = ArgumentGetter.getArgument(args, ARG_ACCESS_LOG);
        if (!accessLog.isPresent()) {
            Optional<LocalDateTime> latestAccessDate = Optional.ofNullable(accessLogRepository.findLatestAccessDate());
            if (!latestAccessDate.isPresent()) {
                validationResult.addError("Since access log file hasn't been parsed yet, " + ARG_ACCESS_LOG
                        + " argument has to be provided.");
            }
        } else if (!isFileReallyExists(accessLog.get())) {
            validationResult.addError(
                    "There is no file in the specified path: " + accessLog + ". Please check the path again.");
        }
        return validationResult;
    }

    private boolean isFileReallyExists(final String fileName) {
        return (new File(fileName)).exists();
    }
}
