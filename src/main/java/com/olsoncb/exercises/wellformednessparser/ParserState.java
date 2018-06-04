package com.olsoncb.exercises.wellformednessparser;

public enum ParserState {
  OPEN_TAG_INITIATED,
  OPEN_TAG_TERMINATED,
  CLOSE_TAG_INITIATED,
  OPEN_TAG_AUTOCLOSE_INITIATED,
  OPEN_TAG_NAMED,
  CLOSE_TAG_TERMINATED
}
