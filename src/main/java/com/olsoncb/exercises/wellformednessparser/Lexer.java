package com.olsoncb.exercises.wellformednessparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer {

  private static final Pattern whitePattern = Pattern.compile("^\\s+");
  private static Pattern namePattern = Pattern.compile("^\\w+");
  private XmlParser parser;
  private int lineNumber;
  private int position;

  Lexer(XmlParser parser) {
    this.parser = parser;
  }

  void analyze(String s) {
    lineNumber = 1;
    String lines[] = s.split("\n");
    for (String line : lines) {
      analyzeLine(line);
      lineNumber++;
    }
    if (!wellFormed()) parser.error("Unmatched open and closed tags in xml");
  }

  private void analyzeLine(String line) {
      position = 0;
      while (position < line.length()) {
          analyzeToken(line);
      }
  }

  private void analyzeToken(String line) {
    if (!findToken(line)) {
      parser.error("No token found", lineNumber, position + 1);
      position += 1;
    }
  }

  private boolean findToken(String line) {
    return findWhiteSpace(line) || findSingleCharacterToken(line) || findName(line);
  }

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

  private boolean findName(String line) {
    Matcher nameMatcher = namePattern.matcher(line.substring(position));
    if (nameMatcher.find()) {
      parser.text(nameMatcher.group(0), lineNumber, position);
      position += nameMatcher.end();
      return true;
    }
    return false;
  }

  boolean wellFormed() {
    return parser.allOpenAndCloseTagsMatch();
  }

  String getErrors() {
    return String.valueOf(parser.getErrors());
  }
}
