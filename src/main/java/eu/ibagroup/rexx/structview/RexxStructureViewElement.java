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

package eu.ibagroup.rexx.structview;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.antlr.intellij.adaptor.xpath.XPath;
import eu.ibagroup.rexx.RexxLanguage;
import eu.ibagroup.rexx.psi.RexxPSIFileRoot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RexxStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	protected final PsiElement element;

	public RexxStructureViewElement(PsiElement element) {
		this.element = element;
	}

	@Override
	public Object getValue() {
		return element;
	}

	@Override
	public void navigate(boolean requestFocus) {
		if (element instanceof NavigationItem ) {
			((NavigationItem) element).navigate(requestFocus);
		}
	}

	@Override
	public boolean canNavigate() {
		return element instanceof NavigationItem &&
			((NavigationItem)element).canNavigate();
	}

	@Override
	public boolean canNavigateToSource() {
		return element instanceof NavigationItem &&
			((NavigationItem)element).canNavigateToSource();
	}

	@NotNull
	@Override
	public String getAlphaSortKey() {
		String s = element instanceof PsiNamedElement ? ((PsiNamedElement) element).getName() : null;
		if ( s==null ) return "unknown key";
		return s;
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		return new RexxItemPresentation(element);
	}

	@NotNull
	@Override
	public TreeElement[] getChildren() {
		if ( element instanceof RexxPSIFileRoot ) {
			Collection<? extends PsiElement> funcs = XPath.findAll(RexxLanguage.INSTANCE, element, "/script/function/ID");
			List<TreeElement> treeElements = new ArrayList<>(funcs.size());
			for (PsiElement el : funcs) {
				treeElements.add(new RexxStructureViewElement(el));
			}
			return treeElements.toArray(new TreeElement[funcs.size()]);
		}
		return new TreeElement[0];
	}
}
