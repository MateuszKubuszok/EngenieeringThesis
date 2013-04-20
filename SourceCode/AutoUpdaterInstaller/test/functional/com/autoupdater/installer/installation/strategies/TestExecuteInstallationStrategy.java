package com.autoupdater.installer.installation.strategies;

import static org.fest.assertions.api.Assertions.fail;

import java.io.IOException;

import org.junit.Test;

import com.autoupdater.system.EOperatingSystem;
import com.autoupdater.system.process.executors.InvalidCommandException;

public class TestExecuteInstallationStrategy {
    @Test
    public void testProcess() {
        try {
            // given
            String testCommand = EOperatingSystem.current().getTestCommand();

            // when
            new ExecuteInstallationStrategy().process(null, testCommand);
        } catch (IOException | InvalidCommandException e) {
            fail("process(File,File) should not throw exception while working on accessible files");
        }
    }
}
