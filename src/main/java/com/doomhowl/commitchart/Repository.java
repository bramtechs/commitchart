package com.doomhowl.commitchart;

import com.doomhowl.commitchart.utils.ShellProcess;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Repository {
    private final File folder;
    private final List<Commit> commits;

    public Repository(String folder) {
        this.folder = new File(folder);
        Path gitDir = this.folder.toPath().resolve(".git");
        if (!this.folder.isDirectory() || !this.folder.exists() || !gitDir.toFile().isDirectory()) {
            throw new IllegalArgumentException("Not a valid git repo folder");
        }
        this.commits = new ArrayList<>();
    }

    public void open() {
        ShellProcess git = new ShellProcess(folder.getAbsolutePath());
        String log = git.runCommand("git", "log", "--pretty=format:%h %ad %an", "--date=iso");
        importFromLogOutput(log);
    }

    public long getCommitCount() {
        return commits.size();
    }

    public long getCommitCount(LocalDate date)
    {
        return commits.stream().filter(c -> c.date.equals(date)).count();
    }

    private void importFromLogOutput(String log) {
        var parsedCommits =
                Arrays.stream(log.split("(\\r\\n|\\r|\\n)")).map(Commit::fromLogLine).filter(Optional::isPresent).map(Optional::get);
        commits.addAll(parsedCommits.toList());
    }
}
