package com.ef.validator.accesslog;

import com.ef.data.repository.AccessLogRepository;
import com.ef.exception.NotValidArgumentException;
import com.ef.util.ArgumentGetter;
import com.ef.validator.ArgumentValidator;
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
    public void validate(final ApplicationArguments args) throws NotValidArgumentException {
        Optional<String> accessLog = ArgumentGetter.getArgument(args, ARG_ACCESS_LOG);
        if (!accessLog.isPresent()) {
            Optional<LocalDateTime> latestAccessDate = Optional.ofNullable(accessLogRepository.findLatestAccessDate());
            if (!latestAccessDate.isPresent()) {
                throw new NotValidArgumentException("Since access log file hasn't been parsed yet, " + ARG_ACCESS_LOG
                        + " argument has to be provided.");
            }
        } else if (!isFileReallyExists(accessLog.get())) {
            throw new NotValidArgumentException(
                    "There is no file in the specified path: " + accessLog + ". Please check the path again:");
        }
    }

    private boolean isFileReallyExists(final String fileName) {
        return (new File(fileName)).exists();
    }
}
