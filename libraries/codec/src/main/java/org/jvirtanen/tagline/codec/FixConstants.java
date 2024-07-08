/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

class FixConstants {

    static final byte EQUALS = '=';

    static final byte SOH = 1;

    static final byte YES = 'Y';

    static final byte NO = 'N';

    static final short YES_SHORT = 'Y' << 8 | SOH;

    static final short NO_SHORT = 'N' << 8 | SOH;

    static final short BEGIN_STRING_SHORT = '8' << 8 | '=';

    static final short BODY_LENGTH_SHORT = '9' << 8 | '=';

    static final int CHECK_SUM_MEDIUM = '1' << 16 | '0' << 8 | '=';

}
