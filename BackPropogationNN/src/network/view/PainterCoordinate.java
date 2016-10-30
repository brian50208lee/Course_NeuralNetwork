package network.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class PainterCoordinate extends JComponent implements MouseInputListener,MouseWheelListener{
	private ArrayList<double[]> dataPoint = null;
	private ArrayList<double[]> linePoint = null;
	
	/* coordinate data */
	private double unitWidth = 500; 
	private double unitHeight = 500;
	private double coordinate_originX = getWidth()/2 -unitWidth/2;
	private double coordinate_originY = getHeight()/2 + unitHeight/2;
	
	/* mouse control */
	private boolean isPressed = false;
	private int pressX;
	private int pressY;
	
	public PainterCoordinate() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	public void drawPoint(ArrayList<double[]> dataPoint){
		initCoordinate();
		this.dataPoint = dataPoint;
		repaint();
	}

	public void initCoordinate(){
		unitWidth = 250; 
		unitHeight = 250;
		coordinate_originX = 100;
		coordinate_originY = 400;
	}
	public void drawLine(ArrayList<double[]> point){
		this.linePoint = point;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/* coordinate information */
		//unitWidth = 500; 
		//unitHeight = 500;
		//coordinate_originX = getWidth()/2 -unitWidth/2;
		//coordinate_originY = getHeight()/2 + unitHeight/2;
		
		/* draw background */
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		/* draw coordinate */
		g.setColor(Color.gray);
		g.drawLine((int)coordinate_originX-1000, (int)coordinate_originY, (int)coordinate_originX+1000, (int)coordinate_originY);
		g.drawLine((int)coordinate_originX, (int)coordinate_originY-1000, (int)coordinate_originX, (int)coordinate_originY+1000);

		/* draw data */
		if (dataPoint != null) {
			for(double db[] : dataPoint){
				if (db[db.length-1] ==1)g.setColor(Color.BLUE);
				else g.setColor(Color.GREEN);
				double pX = coordinate_originX + db[0]*unitWidth - 2.5;
				double pY = coordinate_originY - db[1]*unitHeight - 2.5;
				g.fillOval((int)pX , (int)pY , 5, 5);
			}
		}
		
		/* draw precision line and point */
		if (linePoint != null) {
			for(double db[] : linePoint){
				double pX = coordinate_originX + db[0]*unitWidth;
				double pY = coordinate_originY - db[1]*unitHeight;

				g.setColor(Color.WHITE);
				g.fillOval((int)(pX-2.5) , (int)(pY-2.5) , 5, 5);
				double factor = Math.max(10000/db[1], 10000/db[0]);
				double sX = pX  +  (-1)*db[1]*factor ;
				double sY = pY  +  (-1)*db[0]*factor;
				double eX = pX  -  (-1)*db[1]*factor;
				double eY = pY  -  (-1)*db[0]*factor;

				g.setColor(Color.RED);
				g.drawLine((int)sX, (int)sY, (int)eX,(int)eY);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		unitWidth += (-10)*e.getWheelRotation();
		unitHeight += (-10)*e.getWheelRotation();
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isPressed) {
			coordinate_originX += e.getX() - pressX;
			coordinate_originY += e.getY() - pressY;
			pressX = e.getX();
			pressY = e.getY();
			repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isPressed = true;
		pressX = e.getX();
		pressY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isPressed = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	
	
}
