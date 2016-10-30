package network.data_struct;
public class Patten {
	private double data[];
	private double target;
	private double dataTarget[];
	
	public double[] getData(){return data;}
	public double getTarget(){return target;}
	public double[] getDataTarget(){return dataTarget ;}
	
	public Patten(double dataTarget[]){
		double tempData[] = new double[dataTarget.length-1];
		for (int i = 0; i < tempData.length; i++) {
			tempData[i] = dataTarget[i];
		}
		this.dataTarget = dataTarget;
		this.data = tempData;
		this.target = dataTarget[dataTarget.length-1];
	}
}
