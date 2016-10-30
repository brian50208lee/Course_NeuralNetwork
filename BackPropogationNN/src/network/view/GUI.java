package network.view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNObserver;
import network.observer.BPNSubject;

public class GUI extends BPNObserver{
	
	private PattenSet pattenSet;
	private JFrame frame;
	private PainterCoordinate painterCoordinate;
	
	
	
	public GUI(BPNSubject bpnSubject) {
		super(bpnSubject);
		initFrame();
	}
	
	
	
	public void drawPatten(PattenSet pattenSet){
		this.pattenSet = pattenSet;
		drawPatten();
	}
	
	

	private void initFrame(){
		frame = new JFrame(); 
		frame.setTitle("Back Propogation");
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
