package com.doomhowl.commitchart.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class ChartImageServer {
    public static void main(String[] args) throws IOException {
        int width = 1280;
        int height = 720;
        String caption = "git.doomhowl-interactive.com";
        String repoDir = "C:\\dev\\_dump";

        if (args.length >= 1) {
            try {
                width = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        if (args.length >= 2) {
            try {
                height = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
        }

        if (args.length >= 3) {
            caption = args[2];
        }

        if (args.length >= 4) {
            repoDir = args[3];
        }

        ChartImageFactory factory = new ChartImageFactory();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        int finalWidth = width;
        int finalHeight = height;
        String finalCaption = caption;
        String finalRepoDir = repoDir;

        HttpHandler handler = exchange -> {
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.getPath();

            if (!path.startsWith("/commitchart/") || !path.endsWith(".png")) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            String filePart = path.substring("/commitchart/".length(), path.length() - ".png".length());

            if (!filePart.matches("\\d{4}")) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            int year;
            try {
                year = Integer.parseInt(filePart);
            } catch (NumberFormatException ex) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            if (year < 2020 || year > LocalDate.now().getYear()) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            boolean darkMode = false;
            String query = requestURI.getQuery();
            if (query != null && query.contains("darkMode=true")) {
                darkMode = true;
            }

            ChartImageSettings settings = new ChartImageSettings(
                    finalWidth,
                    finalHeight,
                    finalCaption,
                    finalRepoDir,
                    new Color(0, 0, 0, 0),
                    darkMode,
                    year,
                    LocalDate.now().getMonthValue(),
                    LocalDate.now().getDayOfMonth()
            );

            System.out.println("Asking for image with settings: " + settings);
            Path imgFile = factory.createOrRender(settings).toPath();

            exchange.getResponseHeaders().add("Content-Type", "image/png");
            exchange.sendResponseHeaders(200, Files.size(imgFile));
            Files.copy(imgFile, exchange.getResponseBody());
            exchange.close();
        };

        server.createContext("/commitchart/", handler);
        server.start();
        System.out.println("Running file server on port 8080.");
        System.out.println("Example GET fetch: http://localhost:8080/commitchart/" + LocalDate.now().getYear() + ".png?darkMode=true");
    }
}
