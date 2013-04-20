package com.autoupdater.system.process.executors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass of ProcessExecutors - handles execution of commands and obtaining
 * results through BufferedReader.
 * 
 * <p>
 * It can (and should) be obtained by
 * EOperatingSystem.current().getProcessExecutor().
 * </p>
 * 
 * <p>
 * Commands could be obtained from Commands methods.
 * </p>
 * 
 * @see com.autoupdater.system.process.executors.Commands
 * @see com.autoupdater.system.process.executors.LinuxProcessExecutor
 * @see com.autoupdater.system.process.executors.MacOSProcessExecutor
 * @see com.autoupdater.system.process.executors.WindowsProcessExecutor
 * 
 * @see com.autoupdater.system.EOperatingSystem#getProcessExecutor()
 */
public abstract class AbstractProcessExecutor implements IProcessExecutor {
    @Override
    public ExecutionQueueReader execute(List<String[]> commands) throws IOException {
        return executeCommands(Commands.wrapCommands(commands));
    }

    @Override
    public ExecutionQueueReader execute(String uacHandler, List<String[]> commands)
            throws IOException {
        return executeCommands(rootCommand(uacHandler, Commands.wrapCommands(commands)));
    }

    @Override
    public ExecutionQueueReader execute(String uacHandler, List<String[]> commands, boolean asRoot)
            throws IOException {
        if (asRoot)
            return execute(uacHandler, commands);
        return execute(commands);
    }

    /**
     * Actual execution of commands.
     * 
     * @param commands
     *            commands that should be executed
     * @return reader, which allows to read result of processing
     * @throws IOException
     *             thrown when error occurs in system dependent process
     */
    private ExecutionQueueReader executeCommands(List<String[]> commands) throws IOException {
        List<ProcessBuilder> processBuilders = new ArrayList<ProcessBuilder>();

        for (String[] command : commands)
            processBuilders.add(new ProcessBuilder(command));

        return new ExecutionQueueReader(new ProcessQueue(processBuilders));
    }

    /**
     * Generates command(s) executing all commands passed into ProcessExecutor
     * as root (or any other user with administrative privileges).
     * 
     * @param uacHandler
     *            path to UACHandler used by some implementations
     * @param commands
     *            commands passed into ProcessExecutor
     * @return single command
     */
    protected abstract List<String[]> rootCommand(String uacHandler, List<String[]> commands);
}
