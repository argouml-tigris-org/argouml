 // create the string that begins the popup
// (to be prepended to each string in the modelElements array
var sPreModelElementPopupHTML = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"232\">" + 
 "  <tr>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"5\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"10\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"109\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"78\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"6\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"10\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"5\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"4\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"5\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"1\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td colspan=\"2\"><img name=\"border_top_left\" src=\"popup/border_top_left.gif\" width=\"15\" height=\"5\" border=\"0\"></td>" + 
 "   <td colspan=\"4\" bgcolor=\"#666699\"><img name=\"border_top\" src=\"popup/shim.gif\" width=\"203\" height=\"5\" border=\"0\"></td>" + 
 "   <td colspan=\"3\"><img name=\"border_top_right\" src=\"popup/border_top_right.gif\" width=\"14\" height=\"5\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"5\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td rowspan=\"2\" bgcolor=\"#666699\"><img name=\"border_left_top\" src=\"popup/shim.gif\" width=\"5\" height=\"9\" border=\"0\"></td>" + 
 "   <td rowspan=\"4\" colspan=\"3\" bgcolor=\"#CFCFFF\" align=\"left\" valign=\"middle\"><font face=\"system,arial\" size=2><b>&nbsp;";

// create the string that goes between the title and the body
// (to be appended to each string in the modelElements array
 var sPostTitleModelElementPopupHTML = "</b></font></td>" + 
 "   <td rowspan=\"4\" bgcolor=\"#CFCFFF\"><img name=\"titlebar_space\" src=\"popup/shim.gif\" width=\"6\" height=\"20\" border=\"0\"></td>" + 
 "   <td colspan=\"3\" bgcolor=\"#CFCFFF\"><img src=\"popup/shim.gif\" width=\"19\" height=\"3\" border=\"0\"></td>" + 
 "   <td rowspan=\"2\" bgcolor=\"#666699\"><img name=\"border_right_top\" src=\"popup/shim.gif\" width=\"5\" height=\"9\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"3\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr bgcolor=\"#CFCFFF\">" + 
 "   <td rowspan=\"2\" colspan=\"2\"><a href=\"javascript:dynPopup.hide();\"><img name=\"close button\" alt=\"click to close\" src=\"popup/close_button.gif\" width=\"15\" height=\"15\" border=\"0\"></a></td>" + 
 "   <td rowspan=\"3\"><img src=\"popup/shim.gif\" width=\"4\" height=\"17\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"6\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td rowspan=\"4\" bgcolor=\"#666699\">&nbsp;</td>" + 
 "   <td rowspan=\"4\" bgcolor=\"#666699\">&nbsp;</td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"9\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr bgcolor=\"#CFCFFF\">" + 
 "   <td colspan=\"2\"><img src=\"popup/shim.gif\" width=\"15\" height=\"2\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"2\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td colspan=\"7\" bgcolor=\"#666699\"><img name=\"titlebar_bottomborder\" src=\"popup/shim.gif\" width=\"222\" height=\"1\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"1\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td align=\"left\" valign=\"top\" rowspan=\"2\" colspan=\"7\" bgcolor=\"#d2d2d2\" background=\"popup/body_bg.gif\"><font face=\"arial\" size=\"2\">";
 
// create the string that ends the popup
// (to be appended to each string in the modelElements array
 var sPostBodyModelElementPopupHTML = "</td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"80\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td rowspan=\"2\"><img name=\"border_left_bottom\" src=\"popup/border_left_bottom.gif\" width=\"5\" height=\"13\" border=\"0\"></td>" + 
 "   <td rowspan=\"2\"><img name=\"border_right_bottom\" src=\"popup/border_right_bottom.gif\" width=\"5\" height=\"13\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"8\" border=\"0\"></td>" + 
 "  </tr>" + 
 " " + 
 "  <tr>" + 
 "   <td bgcolor=\"#666699\"><img name=\"border_bottom_left\" src=\"popup/shim.gif\" width=\"10\" height=\"5\" border=\"0\"></td>" + 
 "   <td colspan=\"4\" bgcolor=\"#666699\"><img name=\"border_bottom\" src=\"popup/shim.gif\" width=\"203\" height=\"5\" border=\"0\"></td>" + 
 "   <td colspan=\"2\" bgcolor=\"#666699\"><img name=\"border_bottom_right\" src=\"popup/shim.gif\" width=\"9\" height=\"5\" border=\"0\"></td>" + 
 "   <td><img src=\"popup/shim.gif\" width=\"1\" height=\"5\" border=\"0\"></td>" + 
 "  </tr>" + 
 "</table>";