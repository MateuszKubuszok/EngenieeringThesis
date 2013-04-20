package com.autoupdater.system.process.executors;

import com.autoupdater.system.SystemOperationException;

/**
 * Thrown when Command passed into AbstractProcessExecutor/Commands is invalid.
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 * @see com.autoupdater.system.process.executors.Commands
 */
public class InvalidCommandException extends SystemOperationException {
    /**
     * Creates instance of InvalidCommandException.
     * 
     * @param message
     *            message to be passed
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
