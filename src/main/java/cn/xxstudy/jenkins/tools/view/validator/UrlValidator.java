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

import org.apache.commons.lang.StringUtils;
import cn.xxstudy.jenkins.tools.exception.ConfigurationException;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator implements UIValidator<JTextField> {
    public void validate(JTextField component) throws ConfigurationException {
        String value = component.getText();
        if (StringUtils.isEmpty(value)) {
            return;
        }
        try {
            URL url = new URL(value);
            String userInfo = url.getUserInfo();
            if (StringUtils.isEmpty(userInfo)) {
                return;
            }

            throw new ConfigurationException("Credentials should not be embedded in the url. Use the above form instead.");
        } catch (MalformedURLException ex) {
            throw new ConfigurationException(String.format("URL '%s' is malformed", value));
        }
    }
}
