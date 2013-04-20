package com.autoupdater.system.process.executors;

import java.io.IOException;
import java.util.List;

/**
 * Common interface for classes responsible for executing processes in their
 * respective operating systems with the possible privilege elevation.
 * 
 * <p>
 * It can (and should) be obtained by
 * EOperatingSystem.current().getProcessExecutor().
 * </p>
 * 
 * @see com.autoupdater.system.process.killers.LinuxProcessKiller
 * @see com.autoupdater.system.process.killers.MacOSProcessKiller
 * @see com.autoupdater.system.process.killers.WindowsProcessKiller
 * 
 * @see com.autoupdater.system.EOperatingSystem#getProcessKiller()
 */
public interface IProcessExecutor {
    /**
     * Executes commands as a common user (namely the one that run Java VM).
     * 
     * @param commands
     *            commands to be executed
     * @return reader, which allows to read result of processing
     * @throws IOException
     *             thrown when error occurs in system dependent process
     */
    public ExecutionQueueReader execute(List<String[]> commands) throws IOException;

    /**
     * Executes commands as root.
     * 
     * <p>
     * It is required that UACHandler (if it is needed) was compatible to the
     * one distributed with library: handler should handle any number of
     * parameters passed, each of them should be able to run as a command on
     * terminal.
     * 
     * If there is need to pass a command with argument(s) using quotation mark,
     * it should be escaped, e.g.:
     * </p>
     * 
     * <pre>
     * &quot;program \&quot;parameter 1\&quot; parameter2&quot;
     * </pre>
     * 
     * @param uacHandler
     *            path to UACHandler used by some implementations
     * @param commands
     *            commands to be executed
     * @return reader, which allows to read result of processing
     * @throws IOException
     *             thrown when error occurs in system dependent process
     */
    public ExecutionQueueReader execute(String uacHandler, List<String[]> commands)
            throws IOException;

    /**
     * Executes commands as a common user or root, depending on parameter.
     * 
     * <p>
     * It is required that UACHandler (if it is needed) was compatible to the
     * one distributed with library: handler should handle any number of
     * parameters passed, each of them should be able to run as a command on
     * terminal.
     * 
     * If there is need to pass a command with argument(s) using quotation mark,
     * it should be escaped, e.g.:
     * </p>
     * 
     * <pre>
     * &quot;program \&quot;parameter 1\&quot; parameter2&quot;
     * </pre>
     * 
     * @param uacHandler
     *            path to UACHandler used by some implementations
     * @param commands
     *            commands to be executed
     * @param asRoot
     *            whether or not run commands as root
     * @return reader, which allows to read result of processing
     * @throws IOException
     *             thrown when error occurs in system dependent process
     */
    public ExecutionQueueReader execute(String uacHandler, List<String[]> commands, boolean asRoot)
            throws IOException;
}
