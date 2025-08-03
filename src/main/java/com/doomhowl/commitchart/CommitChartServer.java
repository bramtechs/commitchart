package com.doomhowl.commitchart;

import java.time.LocalDate;
import java.time.Month;

public class CommitChartServer {
    public static void main(String[] args) {
        Repository repo = new Repository("C:\\dev\\spacetyper");
        repo.open();

        System.out.println("Repo has " + repo.getCommitCount(LocalDate.of(2025, Month.AUGUST, 1)) + " commits");
    }
}
