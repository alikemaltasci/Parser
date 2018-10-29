package com.ef.validator.accesslog;

import com.ef.exception.NotValidArgumentException;
import com.ef.validator.ArgumentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class AccessLogArgumentValidator implements ArgumentValidator {

    @Autowired
    private AccessLogFileArgumentValidator accessLogFileArgumentValidator;

    @Autowired
    private StartDateArgumentValidator startDateArgumentValidator;

    @Autowired
    private DurationArgumentValidator durationArgumentValidator;

    @Autowired
    private ThresholdArgumentValidator thresholdArgumentValidator;

    @Override
    public void validate(final ApplicationArguments args) throws NotValidArgumentException {

        ArgumentValidator[] validators = getValidators();
        for (ArgumentValidator validator : validators) {
            validator.validate(args);
        }
    }

    private ArgumentValidator[] getValidators() {
        return new ArgumentValidator[]{
                accessLogFileArgumentValidator, startDateArgumentValidator,
                durationArgumentValidator, thresholdArgumentValidator};
    }
}
