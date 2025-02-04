package com.adacore.adaintellij.analysis.lexical;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public final class GPRFileTokenTypes {

	/*
		Tokens
	*/

	/**
	 * GPR file token representing a contiguous whitespace sequence.
	 */
	static final IElementType WHITESPACES                       = TokenType.WHITE_SPACE;
	/**
	 * Token set representing GPR file whitespaces.
	 */
	public static final TokenSet WHITESPACE_TOKEN_SET = TokenSet.create(WHITESPACES);
	/**
	 * GPR file token representing a syntactically invalid character.
	 */
	static final IElementType BAD_CHARACTER                     = TokenType.BAD_CHARACTER;
	/**
	 * GPR file tokens representing single-character delimiters.
	 */
	static final GPRFileFixedTokenType AMPERSAND                = new GPRFileFixedTokenType("AMPERSAND"        , "&");
	static final GPRFileFixedTokenType APOSTROPHE               = new GPRFileFixedTokenType("APOSTROPHE"       , "'");
	static final GPRFileFixedTokenType LEFT_PARENTHESIS         = new GPRFileFixedTokenType("LEFT_PARENTHESIS" , "(");
	static final GPRFileFixedTokenType RIGHT_PARENTHESIS        = new GPRFileFixedTokenType("RIGHT_PARENTHESIS", ")");
	static final GPRFileFixedTokenType COMMA                    = new GPRFileFixedTokenType("COMMA"            , ",");
	static final GPRFileFixedTokenType FULL_STOP                = new GPRFileFixedTokenType("FULL_STOP"        , ".");
	static final GPRFileFixedTokenType COLON                    = new GPRFileFixedTokenType("COLON"            , ":");
	static final GPRFileFixedTokenType SEMICOLON                = new GPRFileFixedTokenType("SEMICOLON"        , ";");
	static final GPRFileFixedTokenType VERTICAL_LINE            = new GPRFileFixedTokenType("VERTICAL_LINE"    , "|");
	/**
	 * GPR file tokens representing compound delimiters.
	 */
	static final GPRFileFixedTokenType ARROW                    = new GPRFileFixedTokenType("ARROW"            , "=>");
	static final GPRFileFixedTokenType ASSIGNMENT               = new GPRFileFixedTokenType("ASSIGNMENT"       , ":=");
	/**
	 * Token set representing GPR file delimiters.
	 */
	public static final TokenSet DELIMITER_TOKEN_SET = TokenSet.create(

		AMPERSAND, APOSTROPHE, LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
		COMMA, FULL_STOP, COLON, SEMICOLON, VERTICAL_LINE,

		ARROW, ASSIGNMENT

	);
	/**
	 * GPR file tokens representing identifiers and literals.
	 */
	static final GPRFileTokenType      IDENTIFIER               = new GPRFileTokenType("IDENTIFIER");     // My_Project
	/**
	 * Token sets representing GPR file identifiers and literals.
	 */
	public static final TokenSet IDENTIFIER_TOKEN_SET      = TokenSet.create(IDENTIFIER);
	static final GPRFileTokenType      STRING_LITERAL           = new GPRFileTokenType("STRING_LITERAL"); // "main.adb"
	public static final TokenSet STRING_LITERAL_TOKEN_SET  = TokenSet.create(STRING_LITERAL);
	/**
	 * GPR file token representing a single comment.
	 */
	static final GPRFileTokenType      COMMENT                  = new GPRFileTokenType("COMMENT");        // -- GPR file comment
	/**
	 * Token set representing GPR file comments.
	 */
	public static final TokenSet COMMENT_TOKEN_SET = TokenSet.create(COMMENT);
	/**
	 * GPR file tokens representing reserved keywords.
	 */
	static final GPRFileFixedTokenType ABSTRACT_KEYWORD         = new GPRFileFixedTokenType("ABSTRACT_KEYWORD"        , "abstract");
	static final GPRFileFixedTokenType ALL_KEYWORD              = new GPRFileFixedTokenType("ALL_KEYWORD"             , "all");
	static final GPRFileFixedTokenType AT_KEYWORD               = new GPRFileFixedTokenType("AT_KEYWORD"              , "at");
	static final GPRFileFixedTokenType CASE_KEYWORD             = new GPRFileFixedTokenType("CASE_KEYWORD"            , "case");
	static final GPRFileFixedTokenType END_KEYWORD              = new GPRFileFixedTokenType("END_KEYWORD"             , "end");
	static final GPRFileFixedTokenType EXTENDS_KEYWORD          = new GPRFileFixedTokenType("EXTENDS_KEYWORD"         , "extends");
	static final GPRFileFixedTokenType EXTERNAL_KEYWORD         = new GPRFileFixedTokenType("EXTERNAL_KEYWORD"        , "external");
	static final GPRFileFixedTokenType EXTERNAL_AS_LIST_KEYWORD = new GPRFileFixedTokenType("EXTERNAL_AS_LIST_KEYWORD", "external_as_list");
	static final GPRFileFixedTokenType FOR_KEYWORD              = new GPRFileFixedTokenType("FOR_KEYWORD"             , "for");
	static final GPRFileFixedTokenType IS_KEYWORD               = new GPRFileFixedTokenType("IS_KEYWORD"              , "is");
	static final GPRFileFixedTokenType LIMITED_KEYWORD          = new GPRFileFixedTokenType("LIMITED_KEYWORD"         , "limited");
	static final GPRFileFixedTokenType NULL_KEYWORD             = new GPRFileFixedTokenType("NULL_KEYWORD"            , "null");
	static final GPRFileFixedTokenType OTHERS_KEYWORD           = new GPRFileFixedTokenType("OTHERS_KEYWORD"          , "others");
	static final GPRFileFixedTokenType PACKAGE_KEYWORD          = new GPRFileFixedTokenType("PACKAGE_KEYWORD"         , "package");
	static final GPRFileFixedTokenType PROJECT_KEYWORD          = new GPRFileFixedTokenType("PROJECT_KEYWORD"         , "project");
	static final GPRFileFixedTokenType RENAMES_KEYWORD          = new GPRFileFixedTokenType("RENAMES_KEYWORD"         , "renames");
	static final GPRFileFixedTokenType TYPE_KEYWORD             = new GPRFileFixedTokenType("TYPE_KEYWORD"            , "type");

	/*
		Token Sets
	*/
	static final GPRFileFixedTokenType USE_KEYWORD              = new GPRFileFixedTokenType("USE_KEYWORD"             , "use");
	static final GPRFileFixedTokenType WHEN_KEYWORD             = new GPRFileFixedTokenType("WHEN_KEYWORD"            , "when");
	static final GPRFileFixedTokenType WITH_KEYWORD             = new GPRFileFixedTokenType("WITH_KEYWORD"            , "with");
	/**
	 * Token set representing GPR file reserved keywords.
	 */
	public static final TokenSet KEYWORD_TOKEN_SET = TokenSet.create(

		ABSTRACT_KEYWORD, ALL_KEYWORD, AT_KEYWORD,

		CASE_KEYWORD,

		END_KEYWORD, EXTENDS_KEYWORD, EXTERNAL_KEYWORD, EXTERNAL_AS_LIST_KEYWORD,

		FOR_KEYWORD,

		IS_KEYWORD,

		LIMITED_KEYWORD,

		NULL_KEYWORD,

		OTHERS_KEYWORD,

		PACKAGE_KEYWORD, PROJECT_KEYWORD,

		RENAMES_KEYWORD,

		TYPE_KEYWORD,

		USE_KEYWORD,

		WHEN_KEYWORD, WITH_KEYWORD

	);
	/**
	 * GPR file tokens representing reserved qualifiers.
	 */
	static final GPRFileFixedTokenType AGGREGATE_KEYWORD        = new GPRFileFixedTokenType("AGGREGATE_KEYWORD"       , "aggregate");
	static final GPRFileFixedTokenType LIBRARY_KEYWORD          = new GPRFileFixedTokenType("LIBRARY_KEYWORD"         , "library");
	/**
	 * Token set representing GPR file reserved qualifiers.
	 */
	public static final TokenSet QUALIFIER_TOKEN_SET = TokenSet.create(AGGREGATE_KEYWORD, LIBRARY_KEYWORD);

	/**
	 * Private default constructor to prevent instantiation.
	 */
	private GPRFileTokenTypes() {}

}
