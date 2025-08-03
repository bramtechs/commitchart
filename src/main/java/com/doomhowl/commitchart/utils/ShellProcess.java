package com.doomhowl.commitchart.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellProcess {
    private final File workingDirectory;

    public ShellProcess(String workingDir) {
        workingDirectory = new File(workingDir);
        if (!workingDirectory.isDirectory() || !workingDirectory.exists()) {
            throw new IllegalArgumentException("Working directory is not valid");
        }
    }

    public String runCommand(String... command) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDirectory.getAbsoluteFile());
        builder.redirectErrorStream(true);
        System.out.println("<<< " + String.join(" ", command));
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new ProcessException(e);
        }

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new ProcessException(e);
        }

        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new ProcessException(e);
        }

        if (exitCode != 0) {
            System.err.println(output);
            throw new ProcessException("Command returned non null exit code!");
        }
        return output.toString();
    }

}
