package nn.network.component;

import java.util.Random;

public class Neural {
	public double baseWeight = new Random().nextDouble();
	public double baseInput = 1; 
	public Link inLink[] = null;
	public Link outLink[] = null;
	public double outputValue = 0; 
	public double errorDelta = 0; 
	
	/** newWeight[] = [w1,w2,...,wn,baseW] */
	public void setWeight(double newWeight[]){
		for (int i = 0; i < inLink.length; i++){
			inLink[i].weight = newWeight[i];
		}
		baseWeight = newWeight[newWeight.length-1];
	}
	
	public double[] getPrecisionPoint(){
		/* 
		 * calculate prcision point and return 
		 * point = (a1,a2,...,an)
		 * ak = -(wk * wn) / (w1^2 + w2^2 + ... + wn-1^2) 
		 */
		double point[] = new double[inLink.length];
		double denominator = 0;
		for (int i = 0; i < inLink.length; i++) {
			double wn = baseWeight == 0.0 ? Double.MIN_NORMAL*10000 : baseWeight;
			point[i] = (-1)*inLink[i].weight*wn;
			denominator += inLink[i].weight * inLink[i].weight;
		}

		for (int i = 0; i < inLink.length; i++) {
			point[i] /= denominator;
		}
		
		return point;
	}
}
