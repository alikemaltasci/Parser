package com.ef.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

public class ArgumentGetterTest {

    @Test
    public void getExistingArgument() {
        Optional<String> result = ArgumentGetter.getArgument(getArgumentList(), "arg2");
        assertTrue(result.isPresent());
        assertEquals("argVal2", result.get());
    }

    @Test
    public void getNonExistingArgument() {
        Optional<String> result = ArgumentGetter.getArgument(getArgumentList(), "arg5");
        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void getArgumentWithNoValue() {
        Optional<String> result = ArgumentGetter
                .getArgument(new DefaultApplicationArguments(new String[]{"--arg1"}), "arg1");
        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
    }

    private ApplicationArguments getArgumentList() {
        return new DefaultApplicationArguments(new String[]{"--arg1=argVal1", "--arg2=argVal2", "--arg3=argVal3"});
    }
}
