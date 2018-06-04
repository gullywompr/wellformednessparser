package com.olsoncb.exercises.wellformednessparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.olsoncb.exercises.wellformednessparser.ParserEvent.*;


public class XmlParser {

    private ParserState state = ParserState.CLOSE_TAG_TERMINATED;
    private Element openElement;
    private String text;
    private Stack<Element> stack = new Stack<>();
    private List<String> errors = new ArrayList<>();

    public void error(String message, int lineNumber, int position) {
        errors.add(message + " at line "  + lineNumber + ", position " + position);
    }

    public void error(String message) {
        errors.add(message);
    }

    public void openAngle(int lineNumber, int position) {
            handleEvent(OPEN_ANGLE, lineNumber, position);
    }

    public void closedAngle(int lineNumber, int position) {
        handleEvent(CLOSE_ANGLE, lineNumber, position);
    }

    public void slash(int lineNumber, int position) {
        handleEvent(SLASH, lineNumber, position);
    }

    public void text(String text, int lineNumber, int position) {
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
                        this.stack.push(openElement);
                        this.state = ParserState.OPEN_TAG_TERMINATED;
                        break;
                    case SLASH:
                        this.state = ParserState.CLOSE_TAG_INITIATED;
                        break;
                    case TEXT:
                        openElement.setName(this.text);
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
                        Element currentElement = this.stack.pop();
                        if(!currentElement.getName().equals(this.openElement.getName())){
                            error("Mismatched element tag names", lineNumber, position);
                            this.stack.push(currentElement);
                        }
                    case SLASH:
                        error("Too many slashes", lineNumber, position);
                        break;
                    case TEXT:
                        openElement.setName(this.text);
                        break;
                }
                break;
            case CLOSE_TAG_TERMINATED:
                switch (event) {
                    case OPEN_ANGLE:
                        openElement = new Element();
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

    public boolean allOpenAndCloseTagsMatch() {
        return stack.empty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
