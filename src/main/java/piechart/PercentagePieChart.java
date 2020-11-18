package piechart;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 * A PercentagePieChart acts boths as a MVC View and Controller of a Percentage It maintains a reference to its model in order to
 * update it.
 *
 */
public class PercentagePieChart extends JComponent implements PercentageView {

	// Predefined cursors

	private static final Cursor HAND = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private static final Cursor CROSS = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	private static final Cursor ARROW = Cursor.getDefaultCursor();

	/**
	 * Hold a reference to the model
	 */
	private final PercentageModel myModel;
        
        private class AbstractState extends MouseInputAdapter {
        };
        
        private AbstractState InPin = new AbstractState() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentState = Adjusting;
                setCursor(CROSS);
            }
            
            @Override
            public void mouseMoved (MouseEvent e) {
                if (!inPin(e)) {
                        setCursor(ARROW);
                        currentState = Init;
                }
            }
            
        };
        
        private AbstractState Adjusting = new AbstractState() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (inPin(e)) {
                        currentState = InPin;
                        setCursor(CROSS);
                } else {
                        currentState = Init;
                        setCursor(ARROW);
                }
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                myModel.setValue(pointToPercentage(e));
                repaint();
            }
        };
        
        private AbstractState Init = new AbstractState() {
            @Override
            public void mouseMoved (MouseEvent e) {
                if (inPin(e)) {
                        setCursor(HAND);
                        currentState = InPin;
                }
            }
            
        };
        
        private AbstractState currentState = Init;
        
       

	public PercentagePieChart(PercentageModel model) {
		super();

		// "Controller" behaviour : handle mouse input and update the percentage accordingly
		MouseInputListener l = new MouseInputAdapter() {
			public void mousePressed(MouseEvent e) {
                            currentState.mousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
                            currentState.mouseReleased(e);
			}

			;
			public void mouseDragged(MouseEvent e) {
                            currentState.mouseDragged(e);
			}

			public void mouseMoved(MouseEvent e) {
                            currentState.mouseMoved(e);
			}
		};
		addMouseListener(l);
		addMouseMotionListener(l);
		myModel = model;
	}

	// "View" behaviour : when the percentage changes, the piechart must be repainted
	public void notify(PercentageModel model) {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int radius = Math.min(getWidth() - 4, getHeight() - 4) / 2;
		double angle = myModel.getValue() * 2 * Math.PI;
		g.fillOval(centerX - radius, centerY - radius, radius * 2,
			radius * 2);
		g.setColor(Color.yellow);
		g.fillArc(centerX - radius, centerY - radius, radius * 2,
			radius * 2, 0, (int) Math.toDegrees(angle));
		int pinX = centerX + (int) (Math.cos(angle) * radius);
		int pinY = centerY - (int) (Math.sin(angle) * radius);
		g.setColor(Color.gray.brighter());
		g.fill3DRect(pinX - 4, pinY - 4, 8, 8, currentState != Adjusting);
	}

	/**
	 * Test if a mouse event is inside the "Pin" that allows to change the percentage
	 */
	private boolean inPin(MouseEvent ev) {
		int mouseX = ev.getX();
		int mouseY = ev.getY();
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int radius = Math.min(getWidth() - 4, getHeight() - 4) / 2;
		double angle = myModel.getValue() * 2 * Math.PI;
		int pinX = centerX + (int) (Math.cos(angle) * radius);
		int pinY = centerY - (int) (Math.sin(angle) * radius);

		Rectangle r = new Rectangle();
		r.setBounds(pinX - 4, pinY - 4, 8, 8);
		return r.contains(mouseX, mouseY);
	}

	/**
	 * Converts a mouse position to a Percentage value
	 */
	private float pointToPercentage(MouseEvent e) {
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
		int mouseX = e.getX() - centerX;
		int mouseY = e.getY() - centerY;
		double l = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
		double lx = mouseX / l;
		double ly = mouseY / l;
		double theta;
		if (lx > 0) {
			theta = Math.atan(ly / lx);
		} else if (lx < 0) {
			theta = -1 * Math.atan(ly / lx);
		} else {
			theta = 0;
		}

		if ((mouseX > 0) && (mouseY < 0)) {
			theta = -1 * theta;
		} else if (mouseX < 0) {
			theta += Math.PI;
		} else {
			theta = 2 * Math.PI - theta;
		}

		return (float) (theta / (2 * Math.PI));
	}
}