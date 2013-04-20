package com.autoupdater.system.process.killers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.autoupdater.system.EOperatingSystem;

public class ProcessKillerMain {
    final static EOperatingSystem operatingSystem = EOperatingSystem.current();
    final static IProcessKiller processKiller = operatingSystem.getProcessKiller();

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("For testing ProcessKiller type:");
        System.out.println("[process name] - to kill process with given name");
        System.out.println("exit - to quit tester");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println();
            String processName = reader.readLine();

            if ("exit".equals(processName))
                return;

            System.out.println("Send kill command to " + processName);
            try {
                processKiller.killProcess(processName);
            } catch (ProcessKillerException e) {
                System.out.println(e);
            }
        }
    }
}
