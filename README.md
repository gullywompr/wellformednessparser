# wellformednessparser
Exercise in writing a simple parser to check XML for well-formedness

Solution will employ a lexer and parser to match xml tags.  Text between xml tags will be ignored, as it is not relevant to well-formedness.  As the lexer gets a token, a parser will process the token stream, using a finite state machine to identify tags and validate their well-formedness.
 
The implementation is simple, the finite state machine uses small lists of enumerated states and events to identify and validate tags as the lexer proceses tokens one-by-one.  To validate whether open and close tags match, open tags are pushed onto a stack.  When encountering a closing tag, the top tag on the stack will be popped, and the name compared to the closing tag.  If no match, the XML is not well-formed.