package uci.gef;

import java.awt.Graphics;

public interface FigPainter {
  /**
   * Paint Fig f into Graphics g, allowing the
   * figure to be modified according to the current context.
   * This method usually backups and modifies attributes
   * of Fig f, paints the modified figure with <code>f.paint(g)</code>,
   * and restores the original attribute settings.
   * @param g the Graphics used for painting
   * @param f the Figure to be painted
   */
  public void paint(Graphics g, Fig f);

}
