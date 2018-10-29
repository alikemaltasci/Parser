package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import com.ef.util.ArgumentGetter;
import com.ef.validator.ArgumentValidator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ThresholdArgumentValidator implements ArgumentValidator {

    private static final String ARG_THRESHOLD = "threshold";

    @Override
    public void validate(final ApplicationArguments args) throws NotValidArgumentException {
        ArgumentGetter.getArgument(args, ARG_THRESHOLD)
                .orElseThrow(() -> new NotValidArgumentException(ARG_THRESHOLD + " argument has to be provided."));
    }
}
