package network.bpn;

import java.util.ArrayList;
import network.component.Layer;
import network.component.Link;
import network.component.Neural;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;

public class BPN {
	private final static int INPUT_LAYER = 0;
	private final static int HIDDEN_LAYER = 1;
	private final static int OUTPUT_LAYER = 2;
	private BPNSubject bpnSubject;
	private int inNum;
	private int hidNum;
	private int outNum;
	private double learningRate = 0.5;
	private Layer layer[];
	
	
	public void setSubject(BPNSubject bpnSubject){this.bpnSubject = bpnSubject;}
	public BPNSubject getSubject(){return this.bpnSubject ;}
	
	
	public BPN(int inNum , int hidNum , int outNum , double learningRate){
		this.inNum = inNum;
		this.hidNum = hidNum;
		this.outNum = outNum;
		this.learningRate = learningRate;
		initNetwork();
	}
	
	private void initNetwork() {
		/* init layer */
		layer = new Layer[3];
		layer[INPUT_LAYER] = new Layer(inNum);
		layer[HIDDEN_LAYER] = new Layer(hidNum);
		layer[OUTPUT_LAYER] = new Layer(outNum);
		
		/* link neural */
		Layer.linkNeural(layer[INPUT_LAYER], layer[HIDDEN_LAYER]);
		Layer.linkNeural(layer[HIDDEN_LAYER], layer[OUTPUT_LAYER]);
	}
	
	/**
	 * 1.Calculate neural output value.
	 * 2.Calculate error delta value.
	 * 3.Reweight link.
	 */
	public void train(Patten patten){
		double data[] = patten.getData();
		double target = patten.getTarget();
		double output = calOutput(data);
		//System.out.println("MSE:" + MSE(target, output));
		calErrorDelta(target);
		reweight();
	}

	public void train(PattenSet pattenSet){
		for (Patten patten : pattenSet.getPattenList()){
			train(patten);
		}
		notifyObserver();
	}
	
	private double calOutput(double data[]){
		/* init input layer */
		for (int i = 0; i < layer[INPUT_LAYER].neural.length; i++) {
			layer[INPUT_LAYER].neural[i].outputValue = data[i];
		}
		
		/* calculate output of hidden layer  */
		for (Neural neural : layer[HIDDEN_LAYER].neural) {
			double value = neural.baseWeight * neural.baseInput;
			for (Link link : neural.inLink) {
				value += link.weight * link.inNeural.outputValue;
			}
			neural.outputValue = sigmoid(value);
		}
		
		/* calculate output of output layer  */
		for (Neural neural : layer[OUTPUT_LAYER].neural) {
			double value = neural.baseWeight * neural.baseInput;
			for (Link link : neural.inLink) {
				value += link.weight * link.inNeural.outputValue;
			}
			neural.outputValue = sigmoid(value);
		}
		
		return layer[OUTPUT_LAYER].neural[0].outputValue;
	}
	
	private void calErrorDelta(double target){
		/* calculate output Layer ErrorDelta */
		for (Neural neural : layer[OUTPUT_LAYER].neural) {
			double outputValue = neural.outputValue;
			neural.errorDelta = (target - outputValue)*outputValue*(1-outputValue);
		}
		
		/* calculate hidden Layer ErrorDelta */
		for (Neural neural : layer[HIDDEN_LAYER].neural) {
			double outputValue = neural.outputValue;
			double outputLayerDelta = 0.0;
			for (Link link : neural.outLink) {
				outputLayerDelta += link.weight * link.outNeural.errorDelta;
			}
			neural.errorDelta = outputLayerDelta*outputValue*(1-outputValue);
		}
	}
	
	private void reweight(){
		/* reweight output layer */
		for(Neural neural : layer[OUTPUT_LAYER].neural){
			for (Link link : neural.inLink) {
				link.weight += learningRate*neural.errorDelta*link.inNeural.outputValue;
			}
			neural.baseWeight += learningRate*neural.errorDelta*neural.baseInput;
		}
		
		/* reweight hidden layer */
		for(Neural neural : layer[HIDDEN_LAYER].neural){
			for (Link link : neural.inLink) {
				link.weight += learningRate*neural.errorDelta*link.inNeural.outputValue;
			}
			neural.baseWeight += learningRate*neural.errorDelta*neural.baseInput;
		}
	}
	
	public double test(double data[]){
		return calOutput(data);
	}
	
	private double sigmoid(double x){
		//return Math.tanh(x);
		return 1 / (1+Math.exp(-x));
	}
	
	private double MSE(double targetOutput, double actualOutput){
		return Math.pow(targetOutput-actualOutput, 2)/2;
	}
	
	private void notifyObserver(){
		ArrayList<double[]> pointList = new ArrayList<double[]>();
		for (Neural neural : layer[HIDDEN_LAYER].neural ) {
			pointList.add(neural.getPrecisionPoint());
		}
		bpnSubject.setPrecisionPoint(pointList);
	}
	
	
}
