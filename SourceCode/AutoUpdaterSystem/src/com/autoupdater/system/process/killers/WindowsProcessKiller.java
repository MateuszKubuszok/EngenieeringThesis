package com.autoupdater.system.process.killers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementation of ProcessKillerInterface used for killing process in Windows
 * family systems.
 * 
 * @see com.autoupdater.system.process.killers.IProcessKiller
 */
public class WindowsProcessKiller implements IProcessKiller {
    @Override
    public void killProcess(String programName) throws IOException, InterruptedException,
            ProcessKillerException {
        int attempts = 0;

        if (!isProgramRunning(programName))
            return;

        for (attempts = 0; attempts < ProcessKillerConfiguration.HOW_MANY_ATTEMPTS_BEFORE_FAIL; attempts++) {
            if (!askToDieGracefully(programName)) {
                killAllResistants(programName);
                return;
            }

            if (!isProgramRunning(programName))
                return;

            Thread.sleep(ProcessKillerConfiguration.HOW_MANY_SECONDS_BETWEEN_ATTEMPTS * 1000);
        }

        throw new ProcessKillerException("Couldn't kill process - "
                + ProcessKillerConfiguration.HOW_MANY_ATTEMPTS_BEFORE_FAIL + " attempts failed");
    }

    /**
     * Attempts to kill process "gracefully" - by sending TERM signal.
     * 
     * <p>
     * Should make program pop
     * "Do you want to save before exit?"/"Are you sure you want to quit?"
     * dialog and then finish. Otherwise program should just die.
     * </p>
     * 
     * @param programName
     *            program that should be killed
     * @return true if succeed to kill process
     * @throws IOException
     *             thrown when error occurs in system dependent process
     * @throws InterruptedException
     *             thrown when thread is interrupted, while waiting for system
     *             dependent process
     */
    private boolean askToDieGracefully(String programName) throws IOException, InterruptedException {
        return new ProcessBuilder("taskkill", "/IM", programName).start().waitFor() == 0;
    }

    /**
     * Kills process forcefully, if attempt to kill it gracefully failed.
     * 
     * @param programName
     *            program that should be killed
     * @throws IOException
     *             thrown when error occurs in system dependent process
     * @throws InterruptedException
     *             thrown when thread is interrupted, while waiting for system
     *             dependent process
     * @throws ProcessKillerException
     */
    private void killAllResistants(String programName) throws IOException, InterruptedException,
            ProcessKillerException {
        Process process = new ProcessBuilder("taskkill", "/F", "/IM", programName).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        int errorCode = process.waitFor();

        if (errorCode != 0) {
            String message = reader.readLine();
            throw new ProcessKillerException(
                    message != null && message.length() > 7 ? message.substring(7, message.length())
                            : "Couldn't kill process \"" + programName + "\"");
        }
    }

    /**
     * Checks whether program with given name is currently executed.
     * 
     * @param programName
     *            program that should be checked
     * @return true if program is running
     * @throws IOException
     *             thrown when error occurs in system dependent process
     * @throws InterruptedException
     *             thrown when thread is interrupted, while waiting for system
     *             dependent process
     */
    private boolean isProgramRunning(String programName) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("tasklist", "/FO", "csv").start();

        BufferedReader outputReader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        String outputMessage;
        while ((outputMessage = outputReader.readLine()) != null) {
            if (outputMessage.startsWith("\"" + programName + "\""))
                return true;
        }

        return false;
    }
}
