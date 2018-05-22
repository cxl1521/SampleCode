package com.unit6_marriagesuggestion;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarriageSuggestionTest {

    @Test
    public void getSuggestion() {
        MarriageSuggestion marriageSuggestion = new MarriageSuggestion();

        String s = marriageSuggestion.getSuggestion("男", 25);
        assertEquals(s, "還不急。");

        s = marriageSuggestion.getSuggestion("男", 30);
        assertEquals(s, "開始找對象。");

        s = marriageSuggestion.getSuggestion("男", 34);
        assertEquals(s, "趕快結婚！");

        s = marriageSuggestion.getSuggestion("女", 24);
        assertEquals(s, "還不急。");

        s = marriageSuggestion.getSuggestion("女", 29);
        assertEquals(s, "開始找對象。");

        s = marriageSuggestion.getSuggestion("女", 30);
        assertEquals(s, "趕快結婚！");
    }
}
