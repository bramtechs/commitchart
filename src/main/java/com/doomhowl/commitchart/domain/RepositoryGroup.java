package com.doomhowl.commitchart.domain;

import java.io.File;

public class RepositoryGroup extends GitStatsGroup {
    private final File mFolder;

    public RepositoryGroup(String folder) {
        this(new File(folder));
    }

    public RepositoryGroup(File folder) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Git repos parent dir must be a directory!");
        }
        mFolder = folder;
    }

    public void open() {
        File[] children = mFolder.listFiles(File::isDirectory);
        if (children != null) {
            for (File child : children) {
                // TODO: thread
                Repository repo = new Repository(child);
                try {
                    repo.open();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("Error occurred while reading repo " + repo.getFolder() + ". It will not be used.");
                }
                add(repo);
            }
        }
    }
}
