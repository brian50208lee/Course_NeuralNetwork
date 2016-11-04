package network.bpn;

import java.util.ArrayList;
import java.util.Random;

import network.component.Layer;
import network.component.Link;
import network.component.Neural;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;

public class BPN {
	/* observable */
	private BPNSubject bpnSubject;
	
	/* newwork information */
	private int layerNum;
	private int layerNeuralNum[];
	private double learningRate;
	
	/* notwork component */
	private Layer layer[];
	
	/* error recorder */
	private ArrayList<Double> errorRecord;
	
	/* get/set function */
	public ArrayList<Double> getErrorRecord(){return errorRecord;}
	public BPNSubject getSubject(){return this.bpnSubject ;}
	public void setSubject(BPNSubject bpnSubject){this.bpnSubject = bpnSubject;}
	
	/** constructor */
	public BPN(int layerNeuralNum[] , double learningRate){
		/* set network information */
		this.layerNum = layerNeuralNum.length;
		this.layerNeuralNum = layerNeuralNum;
		this.learningRate = learningRate;
		
		/* check legal */
		if (layerNum <2) {
			System.out.println("layer number must greater than 1");
			return;
		}
		
		/* initail newwork */
		initNetwork();
	}
	
	private void initNetwork() {
		/* init layer and create neural */
		layer = new Layer[layerNum];
		for (int i = 0; i < layerNum; i++) {
			layer[i] = new Layer(layerNeuralNum[i]);
		}
		
		/* link neural */
		for (int i = 0; i < layerNum-1; i++) {
			Layer.linkNeural(layer[i], layer[i+1]);
		}
		
		/* set weight */
		for (int layerIdx = 1 ; layerIdx < layerNum ; layerIdx++) {
			for (Neural neural : layer[layerIdx].neural) {
				for (Link link : neural.inLink) {
					link.weight = new Random().nextGaussian();
				}
				neural.baseWeight = new Random().nextGaussian();
			}
		}
		
		/* initial error record */
		errorRecord = new ArrayList<Double>();
	}
	
	/** training network by sigle patten */
	public void train(Patten patten){
		/* get traning data and target value */
		double data[] = patten.getData();
		double target[] = patten.getTarget();
		
		/* setp by step */
		calOutput(data);
		calDelta(target);
		reweight();
	}
	
	/** training network by patten set */
	public void train(PattenSet pattenSet){
		/* train patten in pattenSet and calculate average error */
		double avgError = 0.0;
		for (Patten patten : pattenSet.getPattenList()){
			train(patten);
			avgError += lossFunction_MSE(patten.getTarget());
		}
		errorRecord.add(avgError/pattenSet.size());
		
		/* notify GUI if any */
		notifyObserver();
	}
	
	private double[] calOutput(double data[]){
		/* init input layer by data */
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
		
		/* create output array of output layer */
		double output[] = new double[layer[layerNum-1].neural.length];
		for (int i = 0; i < layer[layerNum-1].neural.length; i++) {
			output[i] = layer[layerNum-1].neural[i].outputValue;
		}
		
		/* return output of each neural in output layer */
		return output;
	}
	
	private void calDelta(double target[]){
		/* calculate ErrorDelta of output Layer */
		for (int i = 0; i < layer[layerNum-1].neural.length; i++) {
			Neural neural = layer[layerNum-1].neural[i];
			double outputValue = neural.outputValue;
			neural.errorDelta = (target[i] - outputValue)*desigmoid(outputValue);
		}
		
		/* calculate ErrorDelta of hidden Layer */
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
		/* calculate output */
		double[] output = calOutput(data);
		
		/* print data */
		StringBuilder resultString = new StringBuilder("Data [ ");
		for (int i = 0; i < data.length; i++) {
			resultString.append(String.format("%.2f,", data[i])) ;
		}
		resultString.replace(resultString.length()-1,resultString.length(), " ]");
		
		/* print result */
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
	private double lossFunction_MSE(double targetOutput[]){
		double MSE = 0.0;
		for (int i = 0  ; i < layer[layerNum-1].neural.length ; i++) {
			Neural neural = layer[layerNum-1].neural[i];
			MSE += Math.pow(targetOutput[i]-neural.outputValue, 2);
		}
		return MSE;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void printWeight(){
		for (int i=1 ;i < layer.length;i++) {
			Layer lay = layer[i];
			System.out.println("Layer: " + i);
			for (int j =0 ; j < lay.neural.length ;j++) {
				Neural neural = lay.neural[j];
				System.out.print("Neural: "+j+"\tWeight: ");
				System.out.printf("(%.5f",neural.inLink[0].weight);
				for (int k = 1; k < neural.inLink.length; k++) {
					System.out.printf(",%.5f",neural.inLink[k].weight);
				}
				System.out.printf(",%.5f)\n",neural.baseWeight);
			}
			System.out.println();
		}
	}
	
	public void printOutputPath(double data[]){
		/* calculate output */
		double[] output = calOutput(data);
		
		/* print data */
		System.out.printf("Test Data [ %.2f",data[0]);
		for (int i = 1; i < data.length; i++) {
			System.out.printf(",%.2f", data[i]) ;
		}
		System.out.print(" ]\t->\t");
		
		/* print output */
		System.out.printf("%f",output[0]);
		for (int i = 1; i < output.length; i++) {
			System.out.printf(",%f", output[i]) ;
		}
		System.out.println();
		
		/* print output path */
		for (int i = 1; i < layer.length; i++) {
			System.out.printf("layer:%d\t->\t",i);
			for (int j = 0; j < layer[i].neural.length; j++) {
				System.out.printf("%1.0f",layer[i].neural[j].outputValue);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printErrorRecord(){
		for ( Double error : errorRecord ) {
			System.out.println("error = " + error.doubleValue() );
		}
		System.out.println();
	}
	
}