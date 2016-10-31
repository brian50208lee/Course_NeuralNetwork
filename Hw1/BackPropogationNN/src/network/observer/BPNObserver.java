package network.observer;

import java.util.ArrayList;

public class BPNObserver implements Observer{
	protected ArrayList<double[]> precisionPoint;
	protected BPNSubject bpnSubject;
	
	public BPNObserver(BPNSubject bpnSubject) {
		this.precisionPoint = new ArrayList<double[]>();
		this.bpnSubject = bpnSubject;
		bpnSubject.register(this);
	}
	
	@Override
	public void update() {
		this.precisionPoint = bpnSubject.getPrecisionPoint();
	}

}
