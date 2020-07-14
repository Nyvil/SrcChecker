package me.nyvil;

/*
 * Created by Nyvil on 7/13/2020, 7:34 PM
 * in me.nyvil
 */

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static String[] stringsToCheck = {"public static void main(String[] args)", "Runtime", "ProcessBuilder", "HttpURLConnection", "URLConnection", "Process", "isProcessRunning",
            "isProcessRunningTitle"
    };


    //TODO: If bored make a check for reflections (I'll prolly never do it)
    public static void main(String[] args) {
        final List<CheckedLine> checkedLines = listFiles();
        writeCheckedLines(checkedLines);
    }

    /**
     * Simple method to get the Path of
     * the jar
     *
     * @return Path of the jar
     */
    public static Path getFileLocation() {
        try {
            return Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks the files and searches for specific code
     * and if it is found it is added to CheckedLines to write it later
     * to the file
     *
     * @return checkedLines
     */
    public static List<CheckedLine> listFiles() {
        final List<CheckedLine> checkedLines = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(getFileLocation())) {
            paths.filter(Files::isReadable).filter(f -> f.toString().endsWith(".java")).forEach(file -> {
                try {
                    final List<String> lines = Files.readAllLines(file, Charset.defaultCharset());

                    for (int line = 0; line < lines.size(); line++) {
                        for (String s : stringsToCheck) {
                            final String content = lines.get(line);
                            if (content.contains(s)) {
                                final CheckedLine checkedLine = new CheckedLine(file.getFileName().toString(), line, content);
                                checkedLines.add(checkedLine);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checkedLines;
    }

    /**
     * Writes the lines found
     * to the lines.txt file
     *
     * @param checkedLines
     */
    public static void writeCheckedLines(List<CheckedLine> checkedLines) {
        try (BufferedWriter bw = Files.newBufferedWriter(getFileLocation().resolve("lines.txt"))) {
            for (CheckedLine checkedLine : checkedLines) {
                bw.write("File:" + checkedLine.getFileName() + " Line: " + checkedLine.getLineNumber() + " code: " + checkedLine.getLineContent() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
