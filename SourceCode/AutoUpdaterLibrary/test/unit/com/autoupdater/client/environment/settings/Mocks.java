package com.autoupdater.client.environment.settings;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import com.autoupdater.client.Paths;
import com.autoupdater.client.Values;

public class Mocks {
    private static ClientSettings clientSettings;
    private static ProgramSettings programSettings;
    private static ProgramSettings programSettings2;
    private static SortedSet<ProgramSettings> programsSettings;

    public static ClientSettings clientSettings() {
        return (clientSettings != null) ? clientSettings : (clientSettings = ClientSettingsBuilder
                .builder().setClientName(Values.ClientSettings.clientName)
                .setClientExecutableName(Values.ClientSettings.clientExecutableName)
                .setPathToClient(Paths.Library.clientPath)
                .setPathToClientDirectory(Paths.Library.clientDir)
                .setPathToInstaller(Paths.Library.installerPath)
                .setPathToUACHandler(Paths.Library.uacHandlerPath)
                .setProxyAddress(Values.ClientSettings.proxyAddress)
                .setProxyPort(Values.ClientSettings.proxyPort).build());
    }

    public static ProgramSettings programSettings() {
        return (programSettings != null) ? programSettings
                : (programSettings = ProgramSettingsBuilder.builder()
                        .setProgramName(Values.ProgramSettings.programName)
                        .setProgramExecutableName(Values.ProgramSettings.programExecutableName)
                        .setPathToProgramDirectory(Paths.Installations.Program.programDir)
                        .setPathToProgram(Paths.Installations.Program.programPath)
                        .setServerAddress(Values.ProgramSettings.serverAddress)
                        .setDevelopmentVersion(Values.ProgramSettings.developmentVersion).build());
    }

    public static ProgramSettings programSettings2() {
        return (programSettings2 != null) ? programSettings2
                : (programSettings2 = ProgramSettingsBuilder.builder()
                        .setProgramName(Values.ProgramSettings2.programName)
                        .setProgramExecutableName(Values.ProgramSettings2.programExecutableName)
                        .setPathToProgramDirectory(Paths.Installations.Program2.programDir)
                        .setPathToProgram(Paths.Installations.Program2.programPath)
                        .setServerAddress(Values.ProgramSettings2.serverAddress)
                        .setDevelopmentVersion(Values.ProgramSettings2.developmentVersion).build());
    }

    public static SortedSet<ProgramSettings> programsSettings() {
        return (programsSettings != null) ? programsSettings
                : (programsSettings = new TreeSet<ProgramSettings>(Arrays.asList(programSettings(),
                        programSettings2())));
    }
}
