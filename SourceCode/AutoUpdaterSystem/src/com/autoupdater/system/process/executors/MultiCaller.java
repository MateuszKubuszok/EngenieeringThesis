package com.autoupdater.system.process.executors;

import static com.autoupdater.system.process.executors.Commands.convertConsoleCommands;
import static com.autoupdater.system.process.executors.Commands.joinArguments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to replace multiple commands calls with one.
 * 
 * <p>
 * Used by some ProcessExecutors to execute multiple commands with the privilege
 * elevation.
 * </p>
 */
public class MultiCaller {
    private static String path;
    private static String classPath;

    /**
     * Generates command that will allow to run multiple commands through
     * MultiCaller front end.
     * 
     * <p>
     * Used by some ProcessExecutors to run multiple commands with one request
     * for the privilege elevation.
     * </p>
     * 
     * @param commands
     * @return
     */
    static List<String> prepareCommand(List<String[]> commands) {
        List<String> command = new ArrayList<String>();
        command.add("java");
        command.add("-cp");
        command.add(getClassPath());
        command.add(MultiCaller.class.getName());
        for (String[] subCommand : commands)
            command.add(joinArguments(subCommand));
        return command;
    }

    /**
     * Runs each argument as a command, treating it as if it were a console
     * line.
     * 
     * <p>
     * Results are redirected to output - both standard output and error output.
     * </p>
     * 
     * @param args
     *            commands to run
     */
    public static void main(String[] args) {
        try {
            for (String[] command : convertConsoleCommands(args)) {
                try {
                    Process process = new ProcessBuilder(command).start();

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));
                    BufferedReader err = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()));

                    String line;

                    while ((line = in.readLine()) != null)
                        System.out.println(line);
                    while ((line = err.readLine()) != null)
                        System.err.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtains path to MultiCaller class in ClassPath.
     * 
     * @return path to MultiCaller
     */
    private static String getPath() {
        return (path != null) ? path : (path = MultiCaller.class.getResource(
                MultiCaller.class.getSimpleName() + ".class").toString());
    }

    /**
     * Finds out whether MultiCaller should be run as JAR or from byte code.
     * 
     * @return true if class is run from JAR, false otherwise
     */
    private static boolean runAsJar() {
        return getPath().startsWith("jar:");
    }

    /**
     * Returns ClassPath that will be needed to run main(String[]) method.
     * 
     * <p>
     * Automatically resolves whether MultiCaller should be run from JAR or
     * directly from byte code.
     * </p>
     * 
     * @return ClassPath
     */
    private static String getClassPath() {
        if (classPath == null) {
            if (runAsJar()) {
                Matcher matcher = Pattern.compile("jar:([^!]+)!.+").matcher(getPath());
                if (!matcher.find())
                    throw new RuntimeException("Invalid class path");
                classPath = matcher.group(1);
            } else
                classPath = System.getProperty("java.class.path", null);
        }
        return classPath;
    }
}
