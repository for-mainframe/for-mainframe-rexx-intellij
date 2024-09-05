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

package eu.ibagroup.rexx.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.antlr.intellij.adaptor.SymtabUtils;
import org.antlr.intellij.adaptor.psi.ScopeNode;
import eu.ibagroup.rexx.Icons;
import eu.ibagroup.rexx.RexxFileType;
import eu.ibagroup.rexx.RexxLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RexxPSIFileRoot extends PsiFileBase implements ScopeNode {
	public RexxPSIFileRoot(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, RexxLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return RexxFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "Rexx Language file";
	}

	@Override
	public Icon getIcon(int flags) {
		return Icons.REXX_ICON;
	}

	/** Return null since a file scope has no enclosing scope. It is
	 *  not itself in a scope.
	 */
	@Override
	public ScopeNode getContext() {
		return null;
	}

	@Nullable
	@Override
	public PsiElement resolve(PsiNamedElement element) {
//		System.out.println(getClass().getSimpleName()+
//		                   ".resolve("+element.getName()+
//		                   " at "+Integer.toHexString(element.hashCode())+")");
		if ( element.getParent() instanceof InstructionSubtree ) {
			return SymtabUtils.resolve(this, RexxLanguage.INSTANCE,
				element, "/script/function/ID");
		}
		return SymtabUtils.resolve(this, RexxLanguage.INSTANCE,
			element, "/script/vardef/ID");
	}
}
