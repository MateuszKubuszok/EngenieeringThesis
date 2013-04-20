package com.autoupdater.system;

import com.autoupdater.system.process.executors.AbstractProcessExecutor;
import com.autoupdater.system.process.executors.IProcessExecutor;
import com.autoupdater.system.process.executors.LinuxProcessExecutor;
import com.autoupdater.system.process.executors.MacOSProcessExecutor;
import com.autoupdater.system.process.executors.WindowsProcessExecutor;
import com.autoupdater.system.process.killers.IProcessKiller;
import com.autoupdater.system.process.killers.LinuxProcessKiller;
import com.autoupdater.system.process.killers.MacOSProcessKiller;
import com.autoupdater.system.process.killers.WindowsProcessKiller;

/**
 * Defines currently used operating system, as well as classes performing some
 * system-dependent operations.
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 * @see com.autoupdater.system.process.killers.IProcessKiller
 */
public enum EOperatingSystem {
    /**
     * Defines system dependent instances used on Windows family systems.
     * 
     * @see com.autoupdater.system.OperatingSystemConfiguration#WINDOWS_LOCAL_APP_DATA
     * @see com.autoupdater.system.process.executors.WindowsProcessExecutor
     * @see com.autoupdater.system.process.killers.WindowsProcessKiller
     */
    WINDOWS("Windows", OperatingSystemConfiguration.WINDOWS_LOCAL_APP_DATA,
            new WindowsProcessExecutor(), new WindowsProcessKiller(), "tasklist"),

    /**
     * Defines system dependent instances used on Linux family systems.
     * 
     * @see com.autoupdater.system.OperatingSystemConfiguration#LINUX_LOCAL_APP_DATA
     * @see com.autoupdater.system.process.executors.LinuxProcessExecutor
     * @see com.autoupdater.system.process.killers.LinuxProcessKiller
     */
    LINUX("Linux", OperatingSystemConfiguration.LINUX_LOCAL_APP_DATA, new LinuxProcessExecutor(),
            new LinuxProcessKiller(), "echo \"content\""),

    /**
     * Defines system dependent instances used on MacOS family systems.
     * 
     * @see com.autoupdater.system.OperatingSystemConfiguration#MAC_OS_LOCAL_APP_DATA
     * @see com.autoupdater.system.process.executors.MacOSProcessExecutor
     * @see com.autoupdater.system.process.killers.MacOSProcessKiller
     */
    MAC_OS("MacOS", OperatingSystemConfiguration.MAC_OS_LOCAL_APP_DATA, new MacOSProcessExecutor(),
            new MacOSProcessKiller(), null);

    private final String familyName;
    private final String localAppData;
    private final IProcessExecutor processExecutor;
    private final IProcessKiller processKiller;
    private final String testCommand;

    private static final EOperatingSystem current;
    static {
        EOperatingSystem eos = null;
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN"))
            eos = EOperatingSystem.WINDOWS;
        else if (OS.contains("MAC"))
            eos = EOperatingSystem.MAC_OS;
        else if (OS.contains("NUX"))
            eos = EOperatingSystem.LINUX;
        current = eos;
    }

    /**
     * Creates new EOperatingSystem instance.
     * 
     * @param familyName
     *            name of operating system family
     * @param localAppData
     *            location of "local application data" catalog
     * @param processExecutor
     *            instance of ProcessExecutor
     * @param processKiller
     *            instance of ProcessKiller
     * @param testCommand
     *            command used to tests on given system
     */
    private EOperatingSystem(String familyName, String localAppData,
            AbstractProcessExecutor processExecutor, IProcessKiller processKiller,
            String testCommand) {
        this.familyName = familyName;
        this.localAppData = localAppData;
        this.processExecutor = processExecutor;
        this.processKiller = processKiller;
        this.testCommand = testCommand;
    }

    /**
     * Returns family name for current operating system.
     * 
     * @return family name
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Returns path to local application data.
     * 
     * @return path to local application data
     */
    public String getLocalAppData() {
        return localAppData;
    }

    /**
     * Returns instance of ProcessExecutor suitable for current system.
     * 
     * @see com.autoupdater.system.process.executors.IProcessExecutor
     * 
     * @return ProcessExecutor
     */
    public IProcessExecutor getProcessExecutor() {
        return processExecutor;
    }

    /**
     * Returns instance of ProcessKiller suitable for current system.
     * 
     * @see com.autoupdater.system.process.killers.IProcessKiller
     * 
     * @return ProcessKiller
     */
    public IProcessKiller getProcessKiller() {
        return processKiller;
    }

    /**
     * Command that tests whether or not Executor works.
     * 
     * <p>
     * Since available commands are system-dependent it has to be defined here.
     * </p>
     * 
     * @return commands that tests executor under given system
     */
    public String getTestCommand() {
        return testCommand;
    }

    @Override
    public String toString() {
        return familyName;
    }

    /**
     * Returns EOperatingSustem for current operating system.
     * 
     * @return current operating system
     */
    public static EOperatingSystem current() {
        return current;
    }
}
