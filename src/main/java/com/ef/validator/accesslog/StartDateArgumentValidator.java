package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import com.ef.util.ArgumentGetter;
import com.ef.validator.ArgumentValidator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartDateArgumentValidator implements ArgumentValidator {

    private static final String ARG_START_DATE = "startDate";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";

    @Override
    public void validate(final ApplicationArguments args) throws NotValidArgumentException {
        ArgumentGetter.getArgument(args, ARG_START_DATE).filter(this::isDateParsable)
                .orElseThrow(() -> new NotValidArgumentException(
                        ARG_START_DATE + " argument has to be provided in the format: " + DATE_TIME_FORMAT));
    }

    private boolean isDateParsable(final String startDateStr) {
        try {
            LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        } catch (DateTimeParseException dtp) {
            log.error(dtp.getMessage());
            return false;
        }
        return true;
    }
}
