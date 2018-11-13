package com.aktasci.validator.accesslog;

import com.aktasci.util.ArgumentGetter;
import com.aktasci.validator.ArgumentValidator;
import com.aktasci.validator.ValidationResult;
import java.util.Optional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class DurationArgumentValidator implements ArgumentValidator {

    private static final String ARG_DURATION = "duration";
    private static final String DUR_DAILY = "daily";
    private static final String DUR_HOURLY = "hourly";

    @Override
    public ValidationResult validate(final ApplicationArguments args) {

        Optional<String> argument = ArgumentGetter.getArgument(args, ARG_DURATION)
                .filter(element -> (element.equals(DUR_HOURLY) || element.equals(DUR_DAILY)));

        ValidationResult validationResult = new ValidationResult();
        if (!argument.isPresent()) {
            validationResult
                    .addError(ARG_DURATION + " argument has to be provided as " + DUR_DAILY + " or " + DUR_HOURLY);
        }
        return validationResult;
    }
}
