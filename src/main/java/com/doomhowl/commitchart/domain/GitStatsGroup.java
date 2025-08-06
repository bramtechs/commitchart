package com.doomhowl.commitchart.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class GitStatsGroup implements GitStats {
    private final List<GitStats> mStatHolders;

    public GitStatsGroup() {
        mStatHolders = new ArrayList<>();
    }

    public void add(Repository repo) {
        mStatHolders.add(repo);
    }

    @Override
    public Stream<Commit> getCommits() {
        return mStatHolders.stream().flatMap(GitStats::getCommits);
    }

    @Override
    public Stream<Commit> getCommitsOfDate(LocalDate date) {
        return mStatHolders.stream().flatMap(h -> h.getCommitsOfDate(date));
    }

    @Override
    public Stream<Commit> getCommitsBetween(LocalDate after, LocalDate before) {
        return mStatHolders.stream().flatMap(h -> h.getCommitsBetween(after, before));
    }

    @Override
    public Stream<Commit> getCommitsOfYear(int year) {
        return mStatHolders.stream().flatMap(h -> h.getCommitsOfYear(year));
    }

    public Optional<GitStats> getTopGitStatsOfDay(LocalDate date) {
        return mStatHolders.stream().max((a, b) -> Long.compareUnsigned(a.getCommitsOfDate(date).count(), b.getCommitsOfDate(date).count()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GitStatsGroup that)) return false;
        return Objects.equals(mStatHolders, that.mStatHolders);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mStatHolders);
    }
}
