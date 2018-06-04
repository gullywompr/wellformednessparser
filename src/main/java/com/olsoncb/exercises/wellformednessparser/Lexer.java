package com.olsoncb.exercises.wellformednessparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private XmlParser parser;
    private int lineNumber;
    private int position;

    public Lexer(XmlParser parser) {
        this.parser = parser;
    }

    public void analyze(String s) {
        lineNumber = 1;
        String lines[] = s.split("\n");
        for (String line : lines) {
            analyzeLine(line);
            lineNumber++;
        }
        if(!parser.allOpenAndCloseTagsMatch())
            parser.error("Unmatched open and closed tags in xml");
    }

    private void analyzeLine(String line) {
        for (position = 0; position < line.length(); )
            analyzeToken(line);
    }

    private void analyzeToken(String line) {
        if (!findToken(line)) {
            parser.error("No token found", lineNumber, position + 1);
            position += 1;
        }
    }

    private boolean findToken(String line) {
        return
                findWhiteSpace(line) ||
                        findSingleCharacterToken(line) ||
                        findName(line);
    }

    private static Pattern whitePattern = Pattern.compile("^\\s+");

    private boolean findWhiteSpace(String line) {
            Matcher matcher = whitePattern.matcher(line.substring(position));
            if (matcher.find()) {
                position += matcher.end();
                return true;
            }
        return false;
    }

    private boolean findSingleCharacterToken(String line) {
        String c = line.substring(position, position + 1);
        switch (c) {
            case "<":
                parser.openAngle(lineNumber, position);
                break;
            case ">":
                parser.closedAngle(lineNumber, position);
                break;
            case "/":
                parser.slash(lineNumber, position);
                break;
            default:
                return false;
        }
        position++;
        return true;
    }

    private static Pattern namePattern = Pattern.compile("^\\w+");

    private boolean findName(String line) {
        Matcher nameMatcher = namePattern.matcher(line.substring(position));
        if (nameMatcher.find()) {
            parser.text(nameMatcher.group(0), lineNumber, position);
            position += nameMatcher.end();
            return true;
        }
        return false;
    }
}