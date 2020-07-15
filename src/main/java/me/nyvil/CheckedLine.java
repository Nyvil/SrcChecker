package me.nyvil;

/*
 * Created by Nyvil on 7/14/2020, 3:52 PM
 * in me.nyvil
 */

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CheckedLine {

    private final String filePath;
    private final String fileName;
    private final int lineNumber;
    private final String lineContent;

    public CheckedLine(String filePath, String fileName, int lineNumber, String lineContent) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

}
