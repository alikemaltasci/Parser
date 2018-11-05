package com.ef.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private final List<String> errors;

    public ValidationResult() {
        errors = new ArrayList<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
