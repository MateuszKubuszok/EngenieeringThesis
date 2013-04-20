package com.autoupdater.client.installation.command.generation;

import com.autoupdater.client.environment.settings.ProgramSettings;
import com.autoupdater.client.models.Update;
import com.autoupdater.system.process.executors.InvalidCommandException;

public interface ICommandGenerator {
    public String[] generateCommand(Update update, String pathToInstaller,
            ProgramSettings programSettings) throws InvalidCommandException;
}
