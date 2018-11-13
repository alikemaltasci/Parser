package com.aktasci.validator;

import org.springframework.boot.ApplicationArguments;

public interface ArgumentValidator {

    ValidationResult validate(final ApplicationArguments args);
}
