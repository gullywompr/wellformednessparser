package com.olsoncb.exercises.wellformednessparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.olsoncb.exercises.wellformednessparser.ParserEvent.*;

class XmlParser {

  private ParserState state = ParserState.CLOSE_TAG_TERMINATED;
  private String openElementName;
  private String text;
  private final Stack<String> stack = new Stack<>();
  private List<String> errors = new ArrayList<>();

  void error(String message, int lineNumber, int position) {
    errors.add(message + " at line " + lineNumber + ", position " + position);
  }

  void error(String message) {
    errors.add(message);
  }

  void openAngle(int lineNumber, int position) {
    handleEvent(OPEN_ANGLE, lineNumber, position);
  }

  void closedAngle(int lineNumber, int position) {
    handleEvent(CLOSE_ANGLE, lineNumber, position);
  }

  void slash(int lineNumber, int position) {
    handleEvent(SLASH, lineNumber, position);
  }

  void text(String text, int lineNumber, int position) {
    this.text = text;
    handleEvent(TEXT, lineNumber, position);
  }

  private void handleEvent(ParserEvent event, int lineNumber, int position) {

    switch (state) {

      case OPEN_TAG_INITIATED:
        switch (event) {
          case OPEN_ANGLE:
            error("Too many open angle brackets", lineNumber, position);
            break;
          case CLOSE_ANGLE:
            error("Element open tag has no name", lineNumber, position);
            this.state = ParserState.OPEN_TAG_TERMINATED;
            break;
          case SLASH:
            this.state = ParserState.CLOSE_TAG_INITIATED;
            break;
          case TEXT:
            openElementName = text;
            this.state = ParserState.OPEN_TAG_NAMED;
            break;
        }
        break;

      case OPEN_TAG_NAMED:
        switch (event) {
          case OPEN_ANGLE:
            error("Too many open angle brackets", lineNumber, position);
            break;
          case CLOSE_ANGLE:
            stack.push(openElementName);
            this.state = ParserState.OPEN_TAG_TERMINATED;
            break;
          case SLASH:
            this.state = ParserState.OPEN_TAG_AUTOCLOSE_INITIATED;
            break;
          case TEXT:
            error("Element name can only be one word", lineNumber, position);
            break;
        }
        break;

      case OPEN_TAG_AUTOCLOSE_INITIATED:
        switch (event) {
          case OPEN_ANGLE:
            error("Too many open angle brackets", lineNumber, position);
            break;
          case CLOSE_ANGLE: // no reason to push autoclosed element onto stack
            this.state = ParserState.OPEN_TAG_TERMINATED;
            break;
          case SLASH:
            error("Too many slashes", lineNumber, position);
            break;
          case TEXT:
            error("Invalid text after slash in autoclose element tag", lineNumber, position);
            break;
        }
        break;

      case OPEN_TAG_TERMINATED:
        switch (event) {
          case OPEN_ANGLE:
            this.state = ParserState.OPEN_TAG_INITIATED;
            break;
          case CLOSE_ANGLE:
            error("Too many close angle brackets", lineNumber, position);
            break;
          case SLASH:
            error("Slash found without preceding open angle bracket", lineNumber, position);
            break;
          case TEXT:
            // Normal element text, not relevant to well-formedness, do nothing
            break;
        }
        break;

      case CLOSE_TAG_INITIATED:
        switch (event) {
          case OPEN_ANGLE:
            error("Too many open angle brackets", lineNumber, position);
            break;
          case CLOSE_ANGLE:
            this.state = ParserState.CLOSE_TAG_TERMINATED;
            String currentElementName = stack.pop();
            if (!currentElementName.equals(openElementName)) {
              error("Mismatched element tag names", lineNumber, position);
              this.stack.push(currentElementName);
            }
          case SLASH: // this is what we want to see, do nothing
            break;
          case TEXT:
            openElementName = text;
            break;
        }
        break;

      case CLOSE_TAG_TERMINATED:
        switch (event) {
          case OPEN_ANGLE:
            openElementName = "";
            state = ParserState.OPEN_TAG_INITIATED;
            break;
          case CLOSE_ANGLE:
            error("Too many close angle brackets", lineNumber, position);
            break;
          case SLASH:
            error("Slash found without preceding open angle bracket", lineNumber, position);
            break;
          case TEXT:
            // text outside of an element, undefined, do nothing
            break;
        }

        break;
    }
  }

  boolean allOpenAndCloseTagsMatch() {
    return stack.empty();
  }

  List<String> getErrors() {
    return errors;
  }
}
