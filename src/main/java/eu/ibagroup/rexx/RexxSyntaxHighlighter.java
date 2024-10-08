/*
 * Copyright (c) 2024 IBA Group.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   candiduslynx (Alex Shcherbakov) - initial implementation
 *   IBA Group
 */

package eu.ibagroup.rexx;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class RexxSyntaxHighlighter extends SyntaxHighlighterBase {
	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	public static final TextAttributesKey VAR_SYMBOL =
		createTextAttributesKey("REXX_VAR_SYMBOL", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey SPECIAL_VAR =
		createTextAttributesKey("REXX_SPECIAL_VAR", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey KEYWORD =
		createTextAttributesKey("REXX_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey STRING =
		createTextAttributesKey("REXX_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey LINE_COMMENT =
		createTextAttributesKey("REXX_LINE_COMMENT",
			DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey BLOCK_COMMENT =
		createTextAttributesKey("REXX_BLOCK_COMMENT",
			DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	public static final TextAttributesKey INCLUDE =
		createTextAttributesKey("REXX_INCLUDE", DefaultLanguageHighlighterColors.METADATA);

	public static final TextAttributesKey COLONS =
		createTextAttributesKey("REXX_COLONS", DefaultLanguageHighlighterColors.SEMICOLON);
	public static final TextAttributesKey OPERATION_SIGN =
		createTextAttributesKey("REXX_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey DOT =
		createTextAttributesKey("REXX_DOT", DefaultLanguageHighlighterColors.DOT);
	public static final TextAttributesKey COMMA =
		createTextAttributesKey("REXX_COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey PARENTHESES =
		createTextAttributesKey("REXX_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
	public static final TextAttributesKey NUMBER =
		createTextAttributesKey("REXX_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey CONST =
		createTextAttributesKey("REXX_CONST", DefaultLanguageHighlighterColors.CONSTANT);

	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(RexxLanguage.INSTANCE,
			RexxParser.tokenNames,
			RexxParser.ruleNames);
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		RexxLexer lexer = new RexxLexer(null);
		return new ANTLRLexerAdaptor(RexxLanguage.INSTANCE, lexer);
	}

	@Override
	public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
		if (!(tokenType instanceof TokenIElementType)) return EMPTY_KEYS;
		TokenIElementType myType = (TokenIElementType) tokenType;
		int ttype = myType.getANTLRTokenType();
		TextAttributesKey attrKey;
		switch (ttype) {
			case RexxLexer.SPECIAL_VAR:
				attrKey = SPECIAL_VAR;
				break;
			case RexxLexer.CONST_SYMBOL:
				attrKey = CONST;
				break;
			case RexxLexer.NUMBER:
				attrKey = NUMBER;
				break;
			case RexxLexer.BR_O:
			case RexxLexer.BR_C:
				attrKey = PARENTHESES;
				break;
			case RexxLexer.COMMA:
				attrKey = COMMA;
				break;
			case RexxLexer.SEMICOL:
			case RexxLexer.COLON:
				attrKey = COLONS;
				break;
			case RexxLexer.STOP:
				attrKey = DOT;
				break;
			case RexxLexer.CONCAT:
			case RexxLexer.EQ:
			case RexxLexer.PLUS:
			case RexxLexer.MINUS:
			case RexxLexer.MUL:
			case RexxLexer.DIV:
			case RexxLexer.QUOTINENT:
			case RexxLexer.REMAINDER:
			case RexxLexer.POW:
			case RexxLexer.NOT:
			case RexxLexer.OR:
			case RexxLexer.XOR:
			case RexxLexer.AND:
			case RexxLexer.CMPS_Eq:
			case RexxLexer.CMPS_Neq:
			case RexxLexer.CMPS_M:
			case RexxLexer.CMPS_L:
			case RexxLexer.CMPS_MEq:
			case RexxLexer.CMPS_LEq:
			case RexxLexer.CMPS_NM:
			case RexxLexer.CMPS_NL:
			case RexxLexer.CMP_NEq:
			case RexxLexer.CMP_LM:
			case RexxLexer.CMP_ML:
			case RexxLexer.CMP_M:
			case RexxLexer.CMP_L:
			case RexxLexer.CMP_MEq:
			case RexxLexer.CMP_LEq:
			case RexxLexer.CMP_NM:
			case RexxLexer.CMP_NL:
				attrKey = OPERATION_SIGN;
				break;
			case RexxLexer.STMT_INCLUDE:
				attrKey = INCLUDE;
				break;
			case RexxLexer.VAR_SYMBOL:
				attrKey = VAR_SYMBOL;
				break;
			case RexxLexer.KWD_ADDRESS:
			case RexxLexer.KWD_ARG:
			case RexxLexer.KWD_BY:
			case RexxLexer.KWD_CALL:
			case RexxLexer.KWD_DIGITS:
			case RexxLexer.KWD_DO:
			case RexxLexer.KWD_DROP:
			case RexxLexer.KWD_ELSE:
			case RexxLexer.KWD_END:
			case RexxLexer.KWD_ENGINEERING:
			case RexxLexer.KWD_ERROR:
			case RexxLexer.KWD_EXIT:
			case RexxLexer.KWD_EXPOSE:
			case RexxLexer.KWD_EXTERNAL:
			case RexxLexer.KWD_FAILURE:
			case RexxLexer.KWD_FOR:
			case RexxLexer.KWD_FOREVER:
			case RexxLexer.KWD_FORM:
			case RexxLexer.KWD_FUZZ:
			case RexxLexer.KWD_HALT:
			case RexxLexer.KWD_IF:
			case RexxLexer.KWD_INTERPRET:
			case RexxLexer.KWD_ITERATE:
			case RexxLexer.KWD_LEAVE:
			case RexxLexer.KWD_NAME:
			case RexxLexer.KWD_NOP:
			case RexxLexer.KWD_NOVALUE:
			case RexxLexer.KWD_NUMERIC:
			case RexxLexer.KWD_OFF:
			case RexxLexer.KWD_ON:
			case RexxLexer.KWD_OPTIONS:
			case RexxLexer.KWD_OTHERWISE:
			case RexxLexer.KWD_PARSE:
			case RexxLexer.KWD_PROCEDURE:
			case RexxLexer.KWD_PULL:
			case RexxLexer.KWD_PUSH:
			case RexxLexer.KWD_QUEUE:
			case RexxLexer.KWD_RETURN:
			case RexxLexer.KWD_SAY:
			case RexxLexer.KWD_SCIENTIFIC:
			case RexxLexer.KWD_SELECT:
			case RexxLexer.KWD_SIGNAL:
			case RexxLexer.KWD_SOURCE:
			case RexxLexer.KWD_SYNTAX:
			case RexxLexer.KWD_THEN:
			case RexxLexer.KWD_TO:
			case RexxLexer.KWD_TRACE:
			case RexxLexer.KWD_UNTIL:
			case RexxLexer.KWD_UPPER:
			case RexxLexer.KWD_VALUE:
			case RexxLexer.KWD_VAR:
			case RexxLexer.KWD_VERSION:
			case RexxLexer.KWD_WHEN:
			case RexxLexer.KWD_WHILE:
			case RexxLexer.KWD_WITH:
				attrKey = KEYWORD;
				break;
			case RexxLexer.STRING:
				attrKey = STRING;
				break;
			case RexxLexer.LINE_COMMENT:
			case RexxLexer.CONTINUATION:
				attrKey = LINE_COMMENT;
				break;
			case RexxLexer.BLOCK_COMMENT:
				attrKey = BLOCK_COMMENT;
				break;
			default:
				return EMPTY_KEYS;
		}
		return new TextAttributesKey[]{attrKey};
	}
}
