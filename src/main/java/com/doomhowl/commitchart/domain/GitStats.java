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

    default long countTopDailyCommitsOfYear(int year) {
        int daysInYear = LocalDate.ofYearDay(year, 1).isLeapYear() ? 366 : 365;
        long most = 0;
        for (int d = 1; d <= daysInYear; d++) {
            long count = getCommitsOfDate(LocalDate.ofYearDay(year, d)).count();
            if (most < count) {
                most = count;
            }
        }
        return most;
    }
}
