package com.autoupdater.system.process.executors;

import java.util.List;

/**
 * Implementation of AbstractProcessExecutor for Mac OS family systems.
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 */
public class MacOSProcessExecutor extends AbstractProcessExecutor {
    @Override
    protected List<String[]> rootCommand(String uacHandler, List<String[]> commands) {
        throw new RuntimeException("MacOSProcessExecutor is not yet implemented!");
    }
}
