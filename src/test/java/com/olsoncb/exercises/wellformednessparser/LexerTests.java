package com.olsoncb.exercises.wellformednessparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LexerTests {

    private XmlParser parser = new XmlParser();
    Lexer lexer = new Lexer(parser);

    @Before
    public void setup(){
        parser = new XmlParser();
    }

    @Test
    public void should_find_no_tokens(){

        String xml = "";

        lexer.analyze(xml);

        Assert.assertEquals(true, parser.allOpenAndCloseTagsMatch());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void should_find_root_token(){

        String xml = "<root>";

        lexer.analyze(xml);

        Assert.assertEquals(false, parser.allOpenAndCloseTagsMatch());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void should_find_single_non_root_tokens(){

        String xml = "<alpha/>";

        lexer.analyze(xml);

        Assert.assertEquals(true, parser.allOpenAndCloseTagsMatch());
        Assert.assertEquals(0, parser.getErrors().size());
    }


    @Test
    public void should_find_two_non_root_tokens(){

        String xml = "<alpha></alpha>";

        lexer.analyze(xml);

        Assert.assertEquals(true, parser.allOpenAndCloseTagsMatch());
        Assert.assertEquals(0, parser.getErrors().size());
    }
}
