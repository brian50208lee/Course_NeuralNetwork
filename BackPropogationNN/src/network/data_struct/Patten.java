package network.data_struct;
public class Patten {
	private double data[];
	private double target[];
	private double dataTarget[];
	
	public double[] getData(){return data;}
	public double[] getTarget(){return target;}
	public double[] getDataTarget(){return dataTarget ;}
	
	public Patten(int dataDimension , double dataTarget[]){
		double splitData[] = new double[dataDimension];
		double splitTarget[] = new double[dataTarget.length-dataDimension];
		int i = 0 ;
		for (;i < dataDimension ; i++)splitData[i] = dataTarget[i];
		for (;i < dataTarget.length ;i++)splitTarget[i - dataDimension] = dataTarget[i];
		
		this.dataTarget = dataTarget;
		this.data = splitData;
		this.target = splitTarget;
	}
}
