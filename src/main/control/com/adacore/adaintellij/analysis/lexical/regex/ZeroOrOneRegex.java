package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Regex matching zero or one occurrence of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrOneRegex extends LexerRegex {

	/**
	 * The subregex to be matched zero or one times.
	 */
	final LexerRegex REGEX;

	/**
	 * Constructs a new zero or one regex given a subregex.
	 *
	 * @param regex The subregex for the zero or one regex.
	 */
	public ZeroOrOneRegex(@NotNull LexerRegex regex) { this(regex, 0); }

	/**
	 * Constructs a new zero or one regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the zero or one regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ZeroOrOneRegex(@NotNull LexerRegex regex, int priority) {
		super(priority);
		REGEX = regex;
	}

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return true; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return -1; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		return REGEX.advanced(character);
	}

}
