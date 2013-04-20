package com.autoupdater.system.process.executors;

import static com.autoupdater.system.process.executors.Commands.convertSingleCommand;
import static com.autoupdater.system.process.executors.MultiCaller.prepareCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AbstractProcessExecutor for Linux family systems.
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 */
public class LinuxProcessExecutor extends AbstractProcessExecutor {
    @Override
    protected List<String[]> rootCommand(String uacHandler, List<String[]> commands) {
        List<String> command = new ArrayList<String>();
        command.add("pkexec");
        command.addAll(prepareCommand(commands));
        return convertSingleCommand(command);
    }
}
