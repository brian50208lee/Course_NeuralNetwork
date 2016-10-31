package network.bpn;

import java.util.ArrayList;
import network.component.Layer;
import network.component.Link;
import network.component.Neural;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;

public class BPN {
	private BPNSubject bpnSubject;
	private int layerNum;
	private int layerNeuralNum[];
	private double learningRate = 0.5;
	private Layer layer[];
	private ArrayList<Double> errorRecord;
	
	
	public ArrayList<Double> getErrorRecord(){return errorRecord;}
	public void setSubject(BPNSubject bpnSubject){this.bpnSubject = bpnSubject;}
	public BPNSubject getSubject(){return this.bpnSubject ;}
	
	
	public BPN(int layerNum , int layerNeuralNum[] , double learningRate){
		/* check */
		if (layerNum != layerNeuralNum.length || layerNum <2) {
			System.out.println("error ! cant creat BPN.");
			return;
		}
		
		this.layerNum = layerNum;
		this.layerNeuralNum = layerNeuralNum;
		this.learningRate = learningRate;
		initNetwork();
	}
	
	private void initNetwork() {
		/* init layer */
		layer = new Layer[layerNum];
		for (int i = 0; i < layerNum; i++) {
			layer[i] = new Layer(layerNeuralNum[i]);
		}
		
		/* link neural */
		for (int i = 0; i < layerNum-1; i++) {
			Layer.linkNeural(layer[i], layer[i+1]);
		}
		
		/* error record */
		errorRecord = new ArrayList<Double>();
	}
	
	/**
	 * 1.Calculate neural output value.
	 * 2.Calculate error delta value.
	 * 3.Reweight link.
	 */
	public void train(Patten patten){
		double data[] = patten.getData();
		double target[] = patten.getTarget();
		calOutput(data);
		calErrorDelta(target);
		reweight();
		
	}

	public void train(PattenSet pattenSet){
		/* error record */
		//calOutput(pattenSet.getPattenList().get(500).getData());
		//errorRecord.add(MSE(pattenSet.getPattenList().get(500).getTarget()));
		double sumMSE = 0;
		for (Patten patten : pattenSet.getPattenList()){
			train(patten);
			sumMSE += MSE(patten.getTarget());
		}
		errorRecord.add(sumMSE/pattenSet.size());
		notifyObserver();
	}
	
	private double[] calOutput(double data[]){
		/* init input layer */
		for (int i = 0; i < layer[0].neural.length; i++) {
			layer[0].neural[i].outputValue = data[i];
		}
		
		/* calculate output of hidden layer  */
		for (int i = 1; i < layerNum-1; i++) {
			for (Neural neural : layer[i].neural) {
				double value = neural.baseWeight * neural.baseInput;
				for (Link link : neural.inLink) {
					value += link.weight * link.inNeural.outputValue;
				}
				neural.outputValue = sigmoid(value);
			}
		}

		
		/* calculate output of output layer  */
		for (Neural neural : layer[layerNum-1].neural) {
			double value = neural.baseWeight * neural.baseInput;
			for (Link link : neural.inLink) {
				value += link.weight * link.inNeural.outputValue;
			}
			neural.outputValue = sigmoid(value);
		}
		
		double output[] = new double[layer[layerNum-1].neural.length];
		for (int i = 0; i < layer[layerNum-1].neural.length; i++) {
			output[i] = layer[layerNum-1].neural[i].outputValue;
		}
		
		return output;
	}
	
	private void calErrorDelta(double target[]){
		/* calculate output Layer ErrorDelta */
		for (int i = 0; i < layer[layerNum-1].neural.length; i++) {
			Neural neural = layer[layerNum-1].neural[i];
			double outputValue = neural.outputValue;
			neural.errorDelta = (target[i] - outputValue)*desigmoid(outputValue);
		}
		
		/* calculate hidden Layer ErrorDelta */
		for (int i = layerNum-2; i >0; i--) {
			for (Neural neural : layer[i].neural) {
				double outputValue = neural.outputValue;
				double outputLayerDelta = 0.0;
				for (Link link : neural.outLink) {
					outputLayerDelta += link.weight * link.outNeural.errorDelta;
				}
				neural.errorDelta = outputLayerDelta*desigmoid(outputValue);
			}
		}

	}
	
	private void reweight(){
		/* reweight output layer */
		for(Neural neural : layer[layerNum-1].neural){
			for (Link link : neural.inLink) {
				link.weight += learningRate*neural.errorDelta*link.inNeural.outputValue;
			}
			neural.baseWeight += learningRate*neural.errorDelta*neural.baseInput;
		}
		
		/* reweight hidden layer */
		for (int i = layerNum-2; i >0; i--) {
			for(Neural neural : layer[i].neural){
				for (Link link : neural.inLink) {
					link.weight += learningRate*neural.errorDelta*link.inNeural.outputValue;
				}
				neural.baseWeight += learningRate*neural.errorDelta*neural.baseInput;
			}
		}
	}
	
	public double[] test(double data[]){
		double[] output = calOutput(data);
		
		StringBuilder resultString = new StringBuilder("Data [ ");
		for (int i = 0; i < data.length; i++) {
			resultString.append(String.format("%.2f,", data[i])) ;
		}
		resultString.replace(resultString.length()-1,resultString.length(), " ]");
		
		
		resultString.append("\t->\t");
		for (int i = 0; i < output.length; i++) {
			resultString.append(String.format("%.10f,", output[i]));
		}
		resultString.replace(resultString.length()-1,resultString.length(), "");
		
		System.out.println(resultString);
		return output;
	}
	
	private double sigmoid(double x){
		return 1 / (1+Math.exp(-x));
	}	
	private double desigmoid(double y){
		return y*(1-y);
	}
	
	private double MSE(double targetOutput[]){
		double vMSE = 0.0;
		for (int i = 0  ; i < layer[layerNum-1].neural.length ; i++) {
			Neural neural = layer[layerNum-1].neural[i];
			vMSE += Math.pow(targetOutput[i]-neural.outputValue, 2);
		}
		//System.out.printf("MSE:\t%f\n" , vMSE);
		return vMSE;
	}
	
	private void notifyObserver(){
		ArrayList<double[]> pointList = new ArrayList<double[]>();
		for (int i = 1; i <=1; i++) {
			for (Neural neural : layer[i].neural ) {
				pointList.add(neural.getPrecisionPoint());
			}
		}

		bpnSubject.setPrecisionPoint(pointList);
	}
	
	
}
