// Allow for parsing out tokens in Strings

package uci.uml.xmi;

public class ParseString { 
  String _ps;  // Parse String
  int startPos = -1;
  int endPos = -1;

  public ParseString (String toParse) {
    _ps = toParse;
  }

  public String nextToken() {
    // find out where the start & end of the next token is
    startPos = _ps.indexOf("$", endPos+1);
    endPos   = _ps.indexOf("$", startPos+1);

    //System.out.println("Token currentlny is - " + _ps);
    //System.out.println("NextTok: endPos = " + endPos + " ; startPos = " + startPos);

    // If there is not another one, return null
    if (startPos == -1) return null; 
    else return _ps.substring(startPos+1, endPos);
  }

  public void replaceCurrentToken(String newValue) {
    // Put the new token where the current one is
    _ps = _ps.substring(0, startPos) + newValue + _ps.substring(endPos+1, _ps.length());

    // Adjust the end for the length of the replacement
    // so that the next matching is correct
    endPos += (newValue.length() - ((endPos - startPos) + 1));

    //System.out.println("replCurr: endPos = " + endPos + " ; startPos = " + startPos);
  }

  public String toString () {
    return _ps;
  }
}
