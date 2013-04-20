package com.autoupdater.system.process.executors;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProcessBuilder.class })
public class Mocks {
    private static AbstractProcessExecutor abstractProcessExecutor;
    private static List<String[]> mockCommands;
    private static List<String[]> mockRootCommands;

    public static AbstractProcessExecutor abstractProcessExecutor() {
        return (abstractProcessExecutor != null) ? abstractProcessExecutor
                : (new AbstractProcessExecutor() {
                    @Override
                    protected List<String[]> rootCommand(String uacHandler, List<String[]> commands) {
                        return mockRootCommands();
                    }
                });
    }

    public static List<String[]> mockCommands() {
        return (mockCommands != null) ? mockCommands : Commands.convertSingleCommand("java",
                "-jar", "Installer.jar");
    }

    public static List<String[]> mockRootCommands() {
        return (mockRootCommands != null) ? mockRootCommands : Commands.convertSingleCommand(
                "sudo", "java", "-jar", "Installer.jar");
    }

    public static ProcessQueue processQueue(String... inputs) {
        List<String> queue = new ArrayList<String>(Arrays.asList(inputs));
        final List<Process> processList = new ArrayList<Process>();

        while (!queue.isEmpty()) {
            Process process = PowerMockito.mock(Process.class);
            PowerMockito.doReturn(new ByteArrayInputStream(queue.get(0).getBytes())).when(process)
                    .getInputStream();
            queue.remove(0);

            if (!queue.isEmpty()) {
                PowerMockito.doReturn(new ByteArrayInputStream(queue.get(0).getBytes()))
                        .when(process).getErrorStream();
                queue.remove(0);
            } else
                PowerMockito.doReturn(new ByteArrayInputStream(new byte[0])).when(process)
                        .getErrorStream();

            processList.add(process);
        }

        return new ProcessQueue() {
            @Override
            public Process getNextProcess() {
                if (processList.isEmpty())
                    return null;
                Process process = processList.get(0);
                processList.remove(0);
                return process;
            }

            @Override
            public boolean isEmpty() {
                return processList.isEmpty();
            }
        };
    }

    public static ExecutionQueueReader executionQueueReader(String... inputs) {
        return new ExecutionQueueReader(processQueue(inputs));
    }
}
