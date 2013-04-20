package com.autoupdater.system.process.killers;

import java.io.IOException;

/**
 * Implementation of ProcessKillerInterface used for killing process in Mac OS
 * family systems.
 * 
 * @see com.autoupdater.system.process.killers.IProcessKiller
 */
public class MacOSProcessKiller implements IProcessKiller {
    /**
     * Not yet implemented!
     * 
     * @TODO write actual procedure
     */
    @Override
    public void killProcess(String programName) throws IOException, InterruptedException {
        throw new RuntimeException("MacOSProcessKiller is not yet implemented!");
    }
}
