package com.doomhowl.commitchart.domain;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public interface GitStats {
    Stream<Commit> getCommits();

    Stream<Commit> getCommitsOfDate(LocalDate date);

    Stream<Commit> getCommitsBetween(LocalDate after, LocalDate before);

    default Stream<Commit> getCommitsOfYear(int year) {
        return getCommitsBetween(LocalDate.of(year, Month.JANUARY, 1), LocalDate.of(year, Month.DECEMBER, 31));
    }
}
