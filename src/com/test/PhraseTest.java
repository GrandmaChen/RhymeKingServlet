package com.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.domain.Phrase;

public class PhraseTest {
	/*
	 * @Test public void test() { String str1 = "厉害"; String[] initials1 = {
	 * "h", "l" }; String[] finals1 = { "ai", "i" }; int[] tones1 = { 4, 4 };
	 * 
	 * Phrase phrase1 = new Phrase("gxasdfasdfasdf", str1, initials1, finals1,
	 * tones1);
	 * 
	 * String str2 = "迫害"; String[] initials2 = { "h", "p" }; String[] finals2 =
	 * { "ai", "o" }; int[] tones2 = { 4, 4 };
	 * 
	 * Phrase phrase2 = new Phrase("asdfasdfasdf", str2, initials2, finals2,
	 * tones2);
	 * 
	 * System.out.println(phrase1.rhymes(phrase2, false, true, false, false));
	 * 
	 * }
	 */
	@Test
	public void test2() {

		Phrase phrase1 = new Phrase("gd7e1d366ea972a3a395d50f00832ab95|光虫|ch g|ong uang|21");
		Phrase phrase2 = new Phrase(
				"gd7e1d366ea972a3a395d50f00832ab95|一之太刀鱼|y d t zh y|u ao ai -i(zh/ch/sh/r) i|21411");

		System.out.println(phrase1.toString());
		System.out.println(phrase2.toString());

		System.out.println(phrase1.rhymes(phrase2, false, true, false, false));

	}

}
