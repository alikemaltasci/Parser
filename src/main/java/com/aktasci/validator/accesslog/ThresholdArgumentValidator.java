package com.aktasci.validator.accesslog;

import com.aktasci.util.ArgumentGetter;
import com.aktasci.validator.ArgumentValidator;
import com.aktasci.validator.ValidationResult;
import java.util.Optional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ThresholdArgumentValidator implements ArgumentValidator {

    private static final String ARG_THRESHOLD = "threshold";

    @Override
    public ValidationResult validate(final ApplicationArguments args) {
        Optional<String> argument = ArgumentGetter.getArgument(args, ARG_THRESHOLD);
        ValidationResult validationResult = new ValidationResult();
        if (!argument.isPresent()) {
            validationResult.addError(ARG_THRESHOLD + " argument has to be provided.");
        }
        return validationResult;
    }
}
