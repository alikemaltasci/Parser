package com.ef.validator.accesslog;

import com.ef.validator.ArgumentValidator;
import com.ef.validator.ValidationResult;
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
    public ValidationResult validate(final ApplicationArguments args) {

        ValidationResult validationResult = new ValidationResult();
        ArgumentValidator[] validators = getValidators();
        for (ArgumentValidator validator : validators) {
            validationResult.getErrors().addAll(validator.validate(args).getErrors());
        }

        return validationResult;
    }

    private ArgumentValidator[] getValidators() {
        return new ArgumentValidator[]{
                accessLogFileArgumentValidator, startDateArgumentValidator,
                durationArgumentValidator, thresholdArgumentValidator};
    }
}
