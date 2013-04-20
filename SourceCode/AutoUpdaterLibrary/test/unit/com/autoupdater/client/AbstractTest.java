package com.autoupdater.client;

import java.io.File;
import java.util.SortedSet;

import com.autoupdater.client.Paths.Library;
import com.autoupdater.client.environment.EnvironmentData;
import com.autoupdater.client.environment.settings.ClientSettings;
import com.autoupdater.client.environment.settings.ProgramSettings;
import com.autoupdater.system.process.executors.ExecutionQueueReader;
import com.autoupdater.system.process.executors.ProcessQueue;

public abstract class AbstractTest {
    public AbstractTest() {
        new File(Library.testDir).mkdirs();
    }

    protected ClientSettings clientSettings() {
        return com.autoupdater.client.environment.settings.Mocks.clientSettings();
    }

    protected ProgramSettings programSettings() {
        return com.autoupdater.client.environment.settings.Mocks.programSettings();
    }

    protected ProgramSettings programSettings2() {
        return com.autoupdater.client.environment.settings.Mocks.programSettings2();
    }

    protected SortedSet<ProgramSettings> programsSettings() {
        return com.autoupdater.client.environment.settings.Mocks.programsSettings();
    }

    protected EnvironmentData environmentData() {
        return com.autoupdater.client.environment.Mocks.environmentData();
    }

    protected ProcessQueue processQueue(String... resultsToReturn) {
        return com.autoupdater.system.process.executors.Mocks.processQueue(resultsToReturn);
    }

    protected ExecutionQueueReader executionQueueReader(String... resultsToReturn) {
        return com.autoupdater.system.process.executors.Mocks.executionQueueReader(resultsToReturn);
    }
}
