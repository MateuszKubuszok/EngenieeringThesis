package com.autoupdater.installer.installation.strategies;

import java.io.File;
import java.io.IOException;

import com.autoupdater.system.EOperatingSystem;
import com.autoupdater.system.process.executors.Commands;
import com.autoupdater.system.process.executors.InvalidCommandException;

/**
 * Executes command passed into installer.
 */
public class ExecuteInstallationStrategy implements IInstallationStrategy {
    @Override
    public void process(File ignoredFile, String executedCommand) throws IOException,
            InvalidCommandException {
        EOperatingSystem.current().getProcessExecutor()
                .execute(Commands.convertConsoleCommands(executedCommand)).rewind();
    }
}
