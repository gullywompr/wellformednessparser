# wellformednessparser
Exercise in writing a simple parser to check XML for well-formedness

Solution will employ a lexer to identify tags.  Text between the tags will be discarded.  Once the lexer has tokenized all tags, a parser will process the tags, validating their internal content for well-formedness (attributes), and push opening tags on a stack.  When encountering a closing tag, to top tag on the stack will be popped, and the name compared to the closing tag.  If no match, the XML is not well-formed.