package nn.observer;

import java.util.ArrayList;

public class BPNSubject implements Subject{
	ArrayList<Observer> observers;
	private ArrayList<double[]> precisionPoint;
	
	public ArrayList<double[]> getPrecisionPoint(){
		return this.precisionPoint;
	}
	
	public BPNSubject() {
		this.observers = new ArrayList<Observer>();
		this.precisionPoint = new ArrayList<double[]>();
	}
	
	@Override
	public void register(Observer newObserver) {
		this.observers.add(newObserver);
	}

	@Override
	public void unregister(Observer deletObserver) {
		this.observers.remove(deletObserver);
	}

	@Override
	public void notifyObserver() {
		for(Observer observer : observers){
			observer.update();
		}
	}
	
	public void setPrecisionPoint(ArrayList<double[]> newPrecisionPoint){
		this.precisionPoint = newPrecisionPoint;
		notifyObserver();
	}

}
