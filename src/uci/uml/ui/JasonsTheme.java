/*
 * @(#)DefaultMetalTheme.java	1.6 98/02/04
 * 
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 * 
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 */

package uci.uml.ui;

import com.sun.java.swing.plaf.metal.*;
import com.sun.java.swing.plaf.*;
import com.sun.java.swing.*;
import java.awt.*;

/**
 * This class describes the default Metal Theme.
 *
 * @version 1.6 02/04/98
 * @author Steve Wilson
 */

public class JasonsTheme extends MetalTheme {

    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);
    private final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);
    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);

    private final ColorUIResource secondary1 = new ColorUIResource(102, 102, 102);
    private final ColorUIResource secondary2 = new ColorUIResource(153, 153, 153);
    private final ColorUIResource secondary3 = new ColorUIResource(204, 204, 204);

    private final FontUIResource controlFont = new FontUIResource("SansSerif", Font.BOLD, 10);
    private final FontUIResource systemFont = new FontUIResource("Dialog", Font.PLAIN, 10);
    private final FontUIResource windowTitleFont = new FontUIResource("SansSerif", Font.BOLD, 10);
    private final FontUIResource userFont = new FontUIResource("SansSerif", Font.PLAIN, 10);
    private final FontUIResource smallFont = new FontUIResource("Dialog", Font.PLAIN, 9);

    public String getName() { return "Steel"; }

// these are blue in Metal Default Theme
    protected ColorUIResource getPrimary1() { return primary1; } 
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

// these are gray in Metal Default Theme
    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

    public FontUIResource getControlTextFont() { return controlFont;}
    public FontUIResource getSystemTextFont() { return systemFont;}
    public FontUIResource getUserTextFont() { return userFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getEmphasisTextFont() { return windowTitleFont;}
    public FontUIResource getSubTextFont() { return smallFont;}
}
