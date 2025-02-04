package com.adacore.adaintellij.analysis.lexical;

import com.adacore.adaintellij.AdaTestUtils;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Parser for token list files used for testing.
 * A token list file specifies the list of tokens that constitute an
 * Ada source file, in the order they must be returned by an Ada lexer,
 * including the start and end offset of each token in the source file.
 * The general format of token files is:
 * <p>
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 *                             .
 *                             .
 *                             .
 * <p>
 * For possible token debug names:
 * @see com.adacore.adaintellij.analysis.lexical.AdaTokenTypes
 *
 * A token list files can also contain comments starting with "--",
 * either on a separate line or at the end of a standard line.
 * More formally, a token list file has the following grammar:
 * <p>
 * token_list_file ::= {token_list_line | line_feed_character}
 * <p>
 * token_list_line ::= [no_whitespace_string integer integer] [comment]
 * <p>
 * integer ::= [digit] {digit}
 * <p>
 * digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
 * <p>
 * comment ::= --{no_whitespace_string}
 * <p>
 * Notes:
 * - line_feed_character is the character with code point \u000a
 * - no_whitespace_string can be any string of characters not separated
 *   by spaces or line feeds
 */
final class AdaTokenListParser {

	/**
	 * Parses the token list file at the given URI and returns
	 * the list of tokens described by the list file as an iterator
	 * over token data objects.
	 *
	 * @param fileURI The URI of the token list file.
	 * @return An iterator over the tokens.
	 * @throws Exception If a problem occurs while reading the file
	 *                   or if its syntax is invalid.
	 */
	static Iterator<AdaLexer.Token> parseTokenListFile(URI fileURI) throws Exception {

		String tokenListText = AdaTestUtils.getFileText(fileURI);

		String[] lines = tokenListText.split("\n");

		List<AdaLexer.Token> expectedTokens = new LinkedList<>();

		for (int i = 0 ; i < lines.length ; i++) {

			String line = lines[i];

			String[] lineComponents = line.split(" +");
			int lineComponentsSize = 0;

			for (String component : lineComponents) {
				if (component.startsWith("--")) { break; }
				lineComponentsSize++;
			}

			if (line.length() == 0 || lineComponentsSize == 0) { continue; }
			else if (lineComponentsSize != 3) {
				throw new Exception("Invalid token list file: line " + (i + 1) +
					" does not have exactly 3 components." + line + lineComponentsSize);
			}

			String tokenName = lineComponents[0];
			int tokenStart, tokenEnd;

			try {

				tokenStart = Integer.parseInt(lineComponents[1]);
				tokenEnd   = Integer.parseInt(lineComponents[2]);

			} catch (NumberFormatException exception) {

				throw new Exception("Invalid token list file: line " + (i + 1) +
					" contains a component that is not parsable as an integer.");

			}

			IElementType tokenType =
				"WHITE_SPACE".equals(tokenName)   ? TokenType.WHITE_SPACE   :
				"BAD_CHARACTER".equals(tokenName) ? TokenType.BAD_CHARACTER :
				new AdaTokenType(tokenName);

			expectedTokens.add(new AdaLexer.Token(tokenType, tokenStart, tokenEnd));

		}

		return expectedTokens.iterator();

	}

}
