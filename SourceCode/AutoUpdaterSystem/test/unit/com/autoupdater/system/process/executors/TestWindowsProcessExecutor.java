package com.autoupdater.system.process.executors;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class TestWindowsProcessExecutor {
    @Test
    public void testRootCommand() {
        try {
            // given
            List<String[]> command = Commands.convertSingleCommand("java", "-jar",
                    "Some Installer.jar");
            Method rootCommand = WindowsProcessExecutor.class.getDeclaredMethod("rootCommand",
                    String.class, List.class);
            rootCommand.setAccessible(true);

            // when
            @SuppressWarnings("unchecked")
            List<String[]> result = (List<String[]>) rootCommand.invoke(
                    new WindowsProcessExecutor(), "uacHandler", command);

            // then
            assertThat(result).as("").isNotNull().hasSize(1);
            assertThat(result.get(0))
                    .as("")
                    .isNotNull()
                    .isEqualTo(
                            new String[] { "uacHandler",
                                    "\"\\\"java\\\" \\\"-jar\\\" \\\"Some Installer.jar\\\"\"" });
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            fail("No exception should be thrown");
        }
    }
}
