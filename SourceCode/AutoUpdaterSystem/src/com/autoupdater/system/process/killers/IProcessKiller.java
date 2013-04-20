package com.autoupdater.system.process.killers;

import java.io.IOException;

/**
 * Common interface for classes responsible for killing processes in their
 * respective operating systems.
 * 
 * <p>
 * It can (and should) be obtained by
 * EOperatingSystem.current().getProcessKiller().
 * </p>
 * 
 * @see com.autoupdater.system.process.killers.LinuxProcessKiller
 * @see com.autoupdater.system.process.killers.MacOSProcessKiller
 * @see com.autoupdater.system.process.killers.WindowsProcessKiller
 * 
 * @see com.autoupdater.system.EOperatingSystem#getProcessKiller()
 */
public interface IProcessKiller {
    /**
     * Attempt to shutdown process with given program name.
     * 
     * @param programName
     *            name of program that should be killed
     * @throws IOException
     *             thrown when error occurs in system dependent process
     * @throws InterruptedException
     *             thrown when thread is interrupted during waiting for system
     *             dependent process to finish
     * @throws ProcessKillerException
     *             thrown if killer is unable to shutdown the process
     */
    public void killProcess(String programName) throws IOException, InterruptedException,
            ProcessKillerException;
}
