package com.aktasci.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.boot.ApplicationArguments;

public class ArgumentGetter {

    private ArgumentGetter() {
    }

    public static Optional<String> getArgument(final ApplicationArguments args, final String argumentName) {
        Set<String> optionNames = args.getOptionNames();
        if (!optionNames.contains(argumentName)) {
            return Optional.empty();
        }

        Optional<List<String>> optionValues = Optional.ofNullable(args.getOptionValues(argumentName));

        return optionValues.filter(list -> !list.isEmpty()).map(values -> values.get(0))
                .filter(value -> !value.equals(""));
    }
}
