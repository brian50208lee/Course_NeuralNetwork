package nn.observer;

import java.util.ArrayList;

public class SOMObserver implements Observer{
	protected ArrayList<double[]> precisionPoint;
	protected Subject bpnSubject;
	
	public SOMObserver(SOMSubject bpnSubject) {
		this.precisionPoint = new ArrayList<double[]>();
		this.bpnSubject = bpnSubject;
		bpnSubject.register(this);
	}
	
	@Override
	public void update() {
		this.precisionPoint = ((SOMSubject)bpnSubject).getPrecisionPoint();
	}

}
