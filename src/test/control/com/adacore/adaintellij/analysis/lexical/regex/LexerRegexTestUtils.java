package com.adacore.adaintellij.analysis.lexical.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Helper testing methods for regex classes.
 */
final class LexerRegexTestUtils {

	/**
	 * Returns the advance state of the given regex with the given of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 * @return The advance state.
	 */
	private static AdvanceState regexAdvanceStateOnSequence(LexerRegex regex, String sequence) {

		LexerRegex advancedRegex = regex;

		for (char character : sequence.toCharArray()) {
			advancedRegex = advancedRegex.advanced(character);
			if (advancedRegex == null) { return AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE; }
		}

		return advancedRegex.nullable() ?
			AdvanceState.MATCHES_SEQUENCE : AdvanceState.ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE;

	}

	/**
	 * Asserts that the given regex successfully advances on
	 * the given sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexAdvances(LexerRegex regex, String sequence) {
		assertNotEquals(
			AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}

	/**
	 * Asserts that the given regex does not successfully advance
	 * on the given sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexDoesNotAdvance(LexerRegex regex, String sequence) {
		assertEquals(
			AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}

	/**
	 * Asserts that the given regex successfully advances on
	 * the given sequence of characters and matches it.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexMatches(LexerRegex regex, String sequence) {
		assertEquals(
			AdvanceState.MATCHES_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}

	/**
	 * Asserts that the given regex does not match the given
	 * sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexDoesNotMatch(LexerRegex regex, String sequence) {
		assertNotEquals(
			AdvanceState.MATCHES_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}

	/**
	 * Represents the result of applying a regex to a sequence of characters.
	 * Possible values are:
	 * <p>
	 * DOES_NOT_ADVANCE_ON_SEQUENCE         => The regex does not successfully advance
	 *                                         on the sequence of characters.
	 * <p>
	 * ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE => The regex successfully advances but the
	 *                                         advanced regex is not nullable and therefore
	 *                                         does not match the sequence of characters.
	 * <p>
	 * MATCHES_SEQUENCE                     => The regex successfully advances and the
	 *                                         advanced regex is nullable and therefore
	 *                                         matches the sequence of characters.
	 */
	private enum AdvanceState {
		DOES_NOT_ADVANCE_ON_SEQUENCE, ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE, MATCHES_SEQUENCE
	}

}
