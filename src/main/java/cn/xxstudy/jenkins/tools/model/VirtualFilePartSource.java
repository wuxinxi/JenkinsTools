/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.xxstudy.jenkins.tools.model;

import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.httpclient.methods.multipart.PartSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description
 *
 * @author Yuri Novitsky
 */
public class VirtualFilePartSource implements PartSource {

    private VirtualFile file;

    public VirtualFilePartSource(VirtualFile file) {
        this.file = file;
    }

    @Override
    public long getLength() {
        return file.getLength();
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public InputStream createInputStream() throws IOException {
        return file.getInputStream();
    }
}
