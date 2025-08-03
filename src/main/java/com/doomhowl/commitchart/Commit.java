package com.doomhowl.commitchart;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Commit {
    public final String hash;
    public final LocalDate date;
    public final String author;

    public Commit(String hash, LocalDate date, String author) {
        this.hash = hash;
        this.date = date;
        this.author = author;
    }

    public static Optional<Commit> fromLogLine(String line) {
        try {
            String[] segments = line.split(" ");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.parse(segments[1], formatter);
            return Optional.of(new Commit(segments[0], date, segments[2]));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to parse '" + line + "' as commit.");
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Commit commit)) return false;
        return Objects.equals(hash, commit.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }
}
