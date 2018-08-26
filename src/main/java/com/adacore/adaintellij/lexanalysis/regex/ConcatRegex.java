package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Regex matching the concatenation of two subregexes.
 */
public final class ConcatRegex implements OORegex {
	
	/**
	 * The concatenation subregexes.
	 */
	private OORegex firstRegex;
	private OORegex secondRegex;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new concatenation regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public ConcatRegex(OORegex firstRegex, OORegex secondRegex) {
		this(firstRegex, secondRegex, 0);
	}
	
	/**
	 * Constructs a new concatenation regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ConcatRegex(OORegex firstRegex, OORegex secondRegex, int priority) {
		this.firstRegex  = firstRegex;
		this.secondRegex = secondRegex;
		this.PRIORITY    = priority;
	}
	
	/**
	 * Returns a new hierarchy of concatenation regexes representing
	 * the concatenation of a list of regexes, in the same order as they
	 * appear in the list:
	 *
	 *            concat_regex
	 *              /      \
	 *          regex_1  concat_regex
	 *                     /      \
	 *                 regex_2     .
	 *                              .
	 *                               .
	 *                             concat_regex
	 *                               /      \
	 *                         regex_n-2  concat_regex
	 *                                      /      \
	 *                                regex_n-1  regex_n
	 *
	 * The priority of the returned root regex is set to that of the
	 * regex in the given list with the highest priority.
	 *
	 * @param regexes The list of regexes to concatenate.
	 * @return A hierarchy of concatenation regexes.
	 */
	public static OORegex fromList(@NotNull List<OORegex> regexes) {
		
		int regexesSize = regexes.size();
		
		if (regexesSize == 0) { return null; }
		
		ListIterator<OORegex> regexIterator = regexes.listIterator(regexesSize);
		
		OORegex regex = regexIterator.previous();
		
		int maxPriority = regex.getPriority();
		
		while (regexIterator.hasPrevious()) {
			
			OORegex nextRegex = regexIterator.previous();
			
			int nextRegexPriority = nextRegex.getPriority();
			
			if (nextRegexPriority > maxPriority) {
				maxPriority = nextRegexPriority;
			}
			
			regex = new ConcatRegex(nextRegex, regex, maxPriority);
			
		}
		
		return regex;
		
	}
	
	/**
	 * Returns a new hierarchy of concatenation regexes from an arbitrary
	 * number of regexes (Java varargs) using fromList(List<OORegex>).
	 *
	 * @param regexes The regexes to concatenate.
	 * @return A hierarchy of concatenation regexes.
	 */
	public static OORegex fromRegexes(@NotNull OORegex... regexes) {
		return fromList(Arrays.asList(regexes));
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return firstRegex.nullable() && secondRegex.nullable(); }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#advanced(char)
	 */
	@Override
	public OORegex advanced(char character) {
		
		OORegex firstRegexAdvanced  = firstRegex.advanced(character);
		
		if (firstRegex.nullable()) {
			
			OORegex secondRegexAdvanced = secondRegex.advanced(character);
			
			if (firstRegexAdvanced == null && secondRegexAdvanced == null) {
				
				return null;
				
			} else if (firstRegexAdvanced == null) {
			
				return secondRegexAdvanced;
			
			} else if (secondRegexAdvanced == null) {
			
				return new ConcatRegex(firstRegexAdvanced, secondRegex.clone(), PRIORITY);
			
			} else {
			
				return new UnionRegex(
					new ConcatRegex(firstRegexAdvanced, secondRegex.clone(), PRIORITY),
					secondRegexAdvanced,
					PRIORITY
				);
			
			}
			
		} else {
			
			if (firstRegexAdvanced == null) {
				
				return null;
				
			} else {
				
				return new ConcatRegex(firstRegexAdvanced, secondRegex.clone(), PRIORITY);
				
			}
		}
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() {
		return new ConcatRegex(firstRegex.clone(), secondRegex.clone(), PRIORITY);
	}
	
}
