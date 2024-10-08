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

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class RexxColorSettingsPage implements ColorSettingsPage {
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
		new AttributesDescriptor("Special variables (RC, RESULT, SIGL)",
			RexxSyntaxHighlighter.SPECIAL_VAR),
		new AttributesDescriptor("Identifier", RexxSyntaxHighlighter.VAR_SYMBOL),
		new AttributesDescriptor("Constant", RexxSyntaxHighlighter.CONST),
		new AttributesDescriptor("Number", RexxSyntaxHighlighter.NUMBER),

		new AttributesDescriptor("Keyword", RexxSyntaxHighlighter.KEYWORD),

		new AttributesDescriptor("String", RexxSyntaxHighlighter.STRING),

		new AttributesDescriptor("Line comment",
			RexxSyntaxHighlighter.LINE_COMMENT),
		new AttributesDescriptor("Block comment",
			RexxSyntaxHighlighter.BLOCK_COMMENT),

		new AttributesDescriptor("Include statement", RexxSyntaxHighlighter.INCLUDE),

		new AttributesDescriptor("Colons and semicolons", RexxSyntaxHighlighter.COLONS),
		new AttributesDescriptor("Dots", RexxSyntaxHighlighter.DOT),
		new AttributesDescriptor("Comma", RexxSyntaxHighlighter.COMMA),
		new AttributesDescriptor("Operation signs", RexxSyntaxHighlighter.OPERATION_SIGN),
		new AttributesDescriptor("Parentheses", RexxSyntaxHighlighter.PARENTHESES),
	};

	@Nullable
	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return Icons.REXX_ICON;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new RexxSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return "/* REXX */\n" +
			"/* Single-line comment */\n" +
			"/* Multi-line\n" +
			" * comment\n" +
			" */\n" +
			"\n" +
			"/* %include statement */\n" +
			"\n" +
			"label:\n" +
			"  parse upper arg input_, setting ;\n" +
			"  constant = 1const\n" +
			"  special = sigl\n" +
			"  strWithHex = \"40\"x\"This is actually another string\"\n" +
			"  strWithBin = \"101010\"b\n" +
			"\n" +
			"  if length(input_) > 12 then\n" +
			"    say \"too long: <\" || input_ || '> is longer than 12 characters!'\n" +
			"  else do\n" +
			"    parse var input_ i1 i2 i3 .\n" +
			"    i1 = strip(i1)\n" +
			"    i2 = strip(i2)\n" +
			"    i3 = strip(i3)\n" +
			"    if i3 = '' then\n" +
			"      i3 = SIGL\n" +
			"    do while (i1 > i2)\n" +
			"      i1 = i1 + length(i3) + , /*<--continuation*/\n" +
			"                 15\n" +
			"      say i1\n" +
			"    end\n" +
			"  end\n" +
			"return 0\n";
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return DESCRIPTORS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "Rexx";
	}
}
