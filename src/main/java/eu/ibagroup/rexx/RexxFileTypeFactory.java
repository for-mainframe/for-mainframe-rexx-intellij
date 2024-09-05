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

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;

public class RexxFileTypeFactory extends FileTypeFactory {
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
		fileTypeConsumer.consume(RexxFileType.INSTANCE, RexxFileType.FILE_EXTENSION);
	}
}
