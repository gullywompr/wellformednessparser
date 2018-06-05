# wellformednessparser
Exercise in writing a simple parser to check XML for well-formedness

Solution will employ a lexer and parser to match xml tags.  Text between xml tags will be ignored, as it is not relevant to well-formedness.  As the lexer gets a token, a parser will process the token stream, using a finite state machine to identify tags and validate their well-formedness.

The implementation is simple, the finite state machine uses small lists of enumerated states and events to identify and validate tags as the lexer processes tokens one-by-one.  To validate whether open and close tags match, open tags are pushed onto a stack.  When encountering a closing tag, the top tag on the stack will be popped, and the name compared to the closing tag.  If no match, the XML is not well-formed.

This is a simple approach, and only accounts for a minimal set of markup.  Thus nested switch statements are used, because of their speed and simplicity.  However, as more markup and more complex documents are accounted for, the events and states would grow quite a great deal, and the nested switch statement approach would be come unwieldy. At that point I would probably migrate the code to use the GoF state pattern to encapsulate Parser state, and maybe a class for transitions to encapsulate events, old and new states, and actions. Perhaps something like the visitor pattern could be used to process the xml as well.
