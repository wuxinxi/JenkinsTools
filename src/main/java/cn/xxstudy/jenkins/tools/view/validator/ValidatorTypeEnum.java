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

package cn.xxstudy.jenkins.tools.view.validator;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public enum ValidatorTypeEnum {

    POSITIVE_INTEGER(new PositiveIntegerValidator()),
    URL(new UrlValidator()),
    NOTNULL(new NotNullValidator()),
    FILE(new FileValidator());

    private final UIValidator<JComponent> validator;

    @SuppressWarnings("unchecked")
    ValidatorTypeEnum(UIValidator<? extends JComponent> validator) {
        this.validator = (UIValidator<JComponent>) validator;
    }

    @NotNull
    public UIValidator<JComponent> getValidator() {
        return validator;
    }
}
