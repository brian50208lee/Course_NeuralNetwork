package network.component;

public class Neural {
	public double baseWeight = -0.2;
	public double baseInput = 1; 
	public Link inLink[] = null;
	public Link outLink[] = null;
	public double outputValue = 0; 
	public double errorDelta = 0; 
	
	public double[] getPrecisionPoint(){
		/* calculate prcision point and return */
		double denominator = 0;
		double point[] = new double[inLink.length];
		for (int i = 0; i < inLink.length; i++) {
			point[i] = (-1)*inLink[i].weight*baseWeight;
			denominator += inLink[i].weight * inLink[i].weight;
		}

		for (int i = 0; i < inLink.length; i++) {
			point[i] /= denominator;
		}
		
		return point;
	}
}
