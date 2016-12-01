package nn.view;

import java.util.ArrayList;

import javax.swing.JFrame;

import nn.dataset.Patten;
import nn.dataset.PattenSet;
import nn.observer.SOMObserver;
import nn.observer.SOMSubject;

public class GUI extends SOMObserver{
	
	private PattenSet pattenSet;
	private JFrame frame;
	private PainterCoordinate painterCoordinate;
	
	
	
	public GUI(SOMSubject bpnSubject) {
		super(bpnSubject);
		initFrame();
	}
	
	
	
	public void drawPatten(PattenSet pattenSet){
		this.pattenSet = pattenSet;
		drawPatten();
	}
	
	

	private void initFrame(){
		frame = new JFrame(); 
		frame.setTitle("Self-Ognization Map");
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		painterCoordinate = new PainterCoordinate();
		frame.add(painterCoordinate);
		
		
		frame.setVisible(true);
	}
	
	
	
	private void drawPatten() {
		ArrayList<double[]> point = new ArrayList<double[]>();
		for (Patten patten : pattenSet.getPattenList()) {
			point.add(patten.getDataTarget());
		}
		painterCoordinate.drawPoint(point);
	}
	
	
	
	
	private void drawLine() {
		painterCoordinate.drawLine(precisionPoint);
	}
	
	
	
	@Override
	public void update() {
		super.update();
		drawLine();
	}
	
}
