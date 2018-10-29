package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import com.ef.util.ArgumentGetter;
import com.ef.validator.ArgumentValidator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class DurationArgumentValidator implements ArgumentValidator {

    private static final String ARG_DURATION = "duration";
    private static final String DUR_DAILY = "daily";
    private static final String DUR_HOURLY = "hourly";

    @Override
    public void validate(final ApplicationArguments args) throws NotValidArgumentException {

        ArgumentGetter.getArgument(args, ARG_DURATION)
                .filter(element -> (element.equals(DUR_HOURLY) || element.equals(DUR_DAILY))).orElseThrow(
                () -> new NotValidArgumentException(
                        ARG_DURATION + " argument has to be provided as " + DUR_DAILY + " or " + DUR_HOURLY));
    }
}
