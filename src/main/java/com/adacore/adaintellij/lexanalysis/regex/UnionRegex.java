package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Regex matching the union of two subregexes.
 */
public final class UnionRegex implements OORegex {
	
	/**
	 * The union subregexes.
	 */
	private OORegex firstRegex;
	private OORegex secondRegex;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new union regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public UnionRegex(OORegex firstRegex, OORegex secondRegex) {
		this(firstRegex, secondRegex, 0);
	}
	
	/**
	 * Constructs a new union regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public UnionRegex(OORegex firstRegex, OORegex secondRegex, int priority) {
		this.firstRegex  = firstRegex;
		this.secondRegex = secondRegex;
		this.PRIORITY    = priority;
	}
	
	/**
	 * Returns a new hierarchy of union regexes representing the union
	 * of a list of regexes:
	 *
	 *             union_regex
	 *              /      \
	 *          regex_1   union_regex
	 *                     /      \
	 *                 regex_2     .
	 *                              .
	 *                               .
	 *                              union_regex
	 *                               /      \
	 *                         regex_n-2   union_regex
	 *                                      /      \
	 *                                regex_n-1  regex_n
	 *
	 * The priority of the returned root regex is set to that of the
	 * regex in the given list with the highest priority.
	 *
	 * @param regexes The list of regexes to unite.
	 * @return A hierarchy of union regexes.
	 */
	public static OORegex fromList(@NotNull final List<OORegex> regexes) {
		
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
			
			regex = new UnionRegex(nextRegex, regex, maxPriority);
			
		}
		
		return regex;
		
	}
	
	/**
	 * Returns a new hierarchy of union regexes from an arbitrary
	 * number of regexes (Java varargs) using fromList(List<OORegex>).
	 *
	 * @param regexes The regexes to unite.
	 * @return A hierarchy of union regexes.
	 */
	public static OORegex fromRegexes(@NotNull OORegex... regexes) {
		return fromList(Arrays.asList(regexes));
	}
	
	/**
	 * Returns a new hierarchy of union regexes from a pair of character
	 * bounds, using fromRange(char, char, int) with the priority is set
	 * to 0.
	 *
	 * @param fromChar The lower bound character of the range.
	 * @param toChar The upper bound character of the range.
	 * @return A hierarchy of union regexes matching a range of characters.
	 */
	public static OORegex fromRange(char fromChar, char toChar) {
		return fromRange(fromChar, toChar, 0);
	}
	
	/**
	 * Returns a new hierarchy of union regexes, in the same format
	 * returned by fromList, representing the union of unit regexes each
	 * matching a character from a range specified by the given character
	 * bounds. The priority of all regexes in the hierarchy is set to the
	 * given priority.
	 *
	 * @param fromChar The lower bound character of the range.
	 * @param toChar The upper bound character of the range.
	 * @param priority The priority to assign to the regexes in the hierarchy.
	 * @return A hierarchy of union regexes matching a range of characters.
	 */
	public static OORegex fromRange(char fromChar, char toChar, int priority) {
		
		if (fromChar > toChar) {
			throw new IllegalArgumentException("Invalid bounds: fromChar must be smaller or equal to toChar");
		} else if (fromChar == toChar) {
			return new UnitRegex(Character.toString(fromChar), priority);
		}
		
		char character = toChar;
		OORegex regex  = new UnitRegex(Character.toString(toChar), priority);
		
		while (character != fromChar) {
			regex = new UnionRegex(
				new UnitRegex(Character.toString(--character), priority),
				regex,
				priority
			);
		}
		
		return regex;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return firstRegex.nullable() || secondRegex.nullable(); }
	
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
		OORegex secondRegexAdvanced = secondRegex.advanced(character);
		
		if (firstRegexAdvanced == null && secondRegexAdvanced == null) {
			
			return null;
			
		} else if (firstRegexAdvanced == null) {
			
			return secondRegexAdvanced;
			
		} else if (secondRegexAdvanced == null) {
			
			return firstRegexAdvanced;
			
		} else {
			
			return new UnionRegex(firstRegexAdvanced, secondRegexAdvanced, PRIORITY);
			
		}
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() {
		return new UnionRegex(firstRegex.clone(), secondRegex.clone(), PRIORITY);
	}
	
}
