/*
 * Copyright 2024 Jussi Virtanen
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
package org.jvirtanen.tagline.codec;

class FixConstants {

    static final byte EQUALS = '=';

    static final byte SOH = 1;

    static final byte YES = 'Y';

    static final byte NO = 'N';

    static final short YES_SHORT = 'Y' | SOH << 8;

    static final short NO_SHORT = 'N' | SOH << 8;

    static final short BEGIN_STRING_SHORT = '8' | '=' << 8;

    static final short BODY_LENGTH_SHORT = '9' | '=' << 8;

    static final int CHECK_SUM_MEDIUM = '1' << 16 | '0' << 8 | '=';

}
