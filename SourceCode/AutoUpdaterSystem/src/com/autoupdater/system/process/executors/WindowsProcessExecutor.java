package com.autoupdater.system.process.executors;

import static com.autoupdater.system.process.executors.Commands.convertSingleCommand;
import static com.autoupdater.system.process.executors.Commands.joinArguments;
import static com.autoupdater.system.process.executors.Commands.wrapArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AbstractProcessExecutor for Windows family systems.
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 */
public class WindowsProcessExecutor extends AbstractProcessExecutor {
    @Override
    protected List<String[]> rootCommand(String uacHandler, List<String[]> commands) {
        List<String> command = new ArrayList<String>();
        command.add(wrapArgument(uacHandler));
        for (String[] subCommand : commands)
            command.add(wrapArgument(joinArguments(subCommand)));
        return convertSingleCommand(command);
    }
}
