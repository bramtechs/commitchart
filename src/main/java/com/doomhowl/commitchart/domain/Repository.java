package com.doomhowl.commitchart.domain;

import com.doomhowl.commitchart.utils.ShellProcess;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class Repository implements GitStats {
    private final File mFolder;
    private final List<Commit> mCommits;

    public Repository(String folder) {
        this(new File(folder));
    }

    public Repository(File folder) {
        mFolder = folder;
        Path gitDir = mFolder.toPath().resolve(".git");
        Path branchesDir = mFolder.toPath().resolve("branches"); // bare repo
        if (!mFolder.isDirectory() || !mFolder.exists() || (!gitDir.toFile().isDirectory() && !branchesDir.toFile().isDirectory())) {
            throw new IllegalArgumentException(mFolder.getAbsolutePath() + " is not a valid git repo folder");
        }
        mCommits = new ArrayList<>();
    }

    public void open() {
        ShellProcess git = new ShellProcess(mFolder.getAbsolutePath());
        String log = git.runCommand("git", "log", "--pretty=format:%h %ad %an", "--date=iso");
        importFromLogOutput(log);
    }

    public File getFolder() {
        return mFolder;
    }

    @Override
    public Stream<Commit> getCommits() {
        return mCommits.stream();
    }

    @Override
    public Stream<Commit> getCommitsOfDate(LocalDate date) {
        return mCommits.stream().filter(c -> c.date.equals(date));
    }

    @Override
    public Stream<Commit> getCommitsBetween(LocalDate after, LocalDate before) {
        return mCommits.stream().filter(c -> c.date.isAfter(after) && c.date.isBefore(before));
    }

    private void importFromLogOutput(String log) {
        var parsedCommits =
                Arrays.stream(log.split("(\\r\\n|\\r|\\n)")).map(Commit::fromLogLine).filter(Optional::isPresent).map(Optional::get);
        mCommits.addAll(parsedCommits.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Repository that)) return false;
        return Objects.equals(mFolder, that.mFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mFolder);
    }
}
