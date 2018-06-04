package com.olsoncb.exercises.wellformednessparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LexerTests {

  private XmlParser parser = new XmlParser();

  private final Lexer lexer = new Lexer(parser);

  @Before
  public void setup() {
    parser = new XmlParser();
  }

  @Test
  public void should_report_well_formed_when_no_tokens() {

    String xml = "";

    lexer.analyze(xml);

      Assert.assertTrue(parser.allOpenAndCloseTagsMatch());
    Assert.assertEquals(0, parser.getErrors().size());
  }

  @Test
  public void should_report_badly_formed_when_unmatched_open_tag() {

    String xml = "<root>";

    lexer.analyze(xml);

    assertThat(lexer.wellFormed(), is(false));
    assertThat(lexer.getErrors(), is("[Unmatched open and closed tags in xml]"));
  }

  @Test
  public void should_report_well_formed_for_autoclose_tag() {

    String xml = "<alpha/>";

    lexer.analyze(xml);

    assertThat(lexer.wellFormed(), is(true));
    assertThat(lexer.getErrors(), is("[]"));
  }

  @Test
  public void should_report_well_formed_when_matched_tags() {

    String xml = "<alpha></alpha>";

    lexer.analyze(xml);

    assertThat(lexer.wellFormed(), is(true));
    assertThat(lexer.getErrors(), is("[]"));
  }

  @Test
  public void should_report_well_formed_for_sample_xml() {

    String xml =
        "<BackgroundCheck>\n"
            + "<CriminalHistory>\n"
            + "<HistoryCode>x</HistoryCode>\n"
            + "< HistoryCode>y</HistoryCode>\n"
            + "< HistoryCode>z</HistoryCode>\n"
            + "</CriminalHistory>\n"
            + "</BackgroundCheck>";

    lexer.analyze(xml);

    assertThat(lexer.wellFormed(), is(true));
    assertThat(lexer.getErrors(), is("[]"));
  }
}
