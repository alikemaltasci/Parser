package com.ef.validator;

import com.ef.exception.NotValidArgumentException;
import org.springframework.boot.ApplicationArguments;

public interface ArgumentValidator {

    void validate(final ApplicationArguments args) throws NotValidArgumentException;
}
