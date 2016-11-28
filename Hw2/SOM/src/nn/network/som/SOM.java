package nn.network.som;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nn.dataset.Patten;
import nn.dataset.PattenSet;
import nn.network.component.Layer;
import nn.network.component.Link;
import nn.network.component.Neural;
import nn.observer.BPNSubject;

public class SOM {
	/* observable */
	private BPNSubject bpnSubject;
	
	/* newwork information */
	private int layerNum;
	private int networkInfo[];
	private int iterations;
	private double learningRateAtt;
	private double learningRateRep;
	
	/** weight[layer][neural][k-th weight] {w1,w2,...,wb} */
	private double weight[][][];
	
	/** activate[layer][neural] */
	private double activate[][];
	
	/* get/set function */
	public BPNSubject getSubject(){return this.bpnSubject;}
	
	/**
	 * creat a SOM NN
	 * @param layerNeuralNum e.g. [2,5,5] means 2-5-5 SOM NN
	 * @param iterations number of iterations each training epoch
	 * @param learningRateAtt attraction learning rate
	 * @param learningRateRep repelling learning rate 
	 */
	public SOM(int networkInfo[], int iterations, double learningRateAtt, double learningRateRep){
		/* check parameter */
		if (networkInfo.length <2) {
			throw new IllegalArgumentException("layer number must greater than 1");
		}
		
		/* set network information */
		this.layerNum = networkInfo.length;
		this.networkInfo = networkInfo;
		this.iterations = iterations;
		this.learningRateAtt = learningRateAtt;
		this.learningRateRep = learningRateRep;
		
		/* init subject */
		this.bpnSubject = new BPNSubject();
		
		/* initail newwork */
		initNetwork();
	}
	
	private void initNetwork() {
		Random random = new Random();
		
		/* init network weight */
		weight = new double[networkInfo.length][][];
		for (int layer = 1; layer < networkInfo.length; layer++) {
			weight[layer] = new double[networkInfo[layer]][];
			for (int neural = 0; neural < networkInfo[layer]; neural++) {
				weight[layer][neural] = new double[networkInfo[layer-1]+1];
				for (int link = 0; link < networkInfo[layer-1]+1; link++) {
					weight[layer][neural][link] = random.nextGaussian();
				}
			}
		}
		
		/* init activate matrix */
		activate = new double[networkInfo.length][];
		for (int layer = 0; layer < activate.length; layer++) {
			activate[layer] = new double[networkInfo[layer]];
		}
		
	}
	

	
	/** training network by patten set */
	public void training(PattenSet pattenSet){
		/* train pattenSet */
		twoClassAlg(pattenSet);
		
		/* notify GUI if any */
		//notifyObserver();
	}
	
	public void twoClassAlg(PattenSet pattenSet){
		Set<Patten> C0 = new HashSet<Patten>();
		Set<Patten> C1 = new HashSet<Patten>();
		for (Patten patten : pattenSet.getPattenList()) {
			if (patten.getTarget()[0] == 0.0) {
				C0.add(patten);
			} else {
				C1.add(patten);
			}
		}
		
		
		for (int m = 1; m < layerNum; m++) {
			for (int epoch = 0; epoch < iterations; epoch++) {
				
			}
		}
		
	}
	
	private void forwarding(double data[]){
		/* init input layer by data */
		for (int neural = 0; neural < networkInfo[0]; neural++) {
			activate[0][neural] = data[neural];
		}
		
		/* calculate output of hidden layer and output layer */
		for (int layer = 1; layer < layerNum; layer++) {
			for (int neural = 0; neural < networkInfo[layer]; neural++) {
				double baseWeight = weight[layer][neural][weight[layer][neural].length-1];
				double value = baseWeight * 1;
				for (int link = 0; link < weight[layer][neural].length-1; link++) {
					value += weight[layer][neural][link] * activate[layer-1][neural];
				}
				activate[layer][neural] = sigmoid(value);
			}
		}
	}
	
	
	private void reweight(PattenSet pattenSet, int m){
	}
	
	
	public double[][] test(double data[]){
		/* calculate output */
		forwarding(data);
		
		/* print data */
		StringBuilder resultString = new StringBuilder("Data [ ");
		for (int i = 0; i < data.length; i++) {
			resultString.append(String.format("%.2f,", data[i])) ;
		}
		resultString.replace(resultString.length()-1,resultString.length(), " ]");
		
		/* print result */
		resultString.append("\t->\t");
		for (int i = 0; i < activate[layerNum-1].length; i++) {
			resultString.append(String.format("%.10f,", activate[layerNum-1][i]));
		}
		resultString.replace(resultString.length()-1,resultString.length(), "");
		
		System.out.println(resultString);
		

		return activate;
	}
	
	private double sigmoid(double x){
		return 1 / (1+Math.exp(-x));
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void notifyObserver(){
		
		//bpnSubject.setPrecisionPoint(pointList);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void printWeight(){
		System.out.println("weight : ");
		for (int i = 0; i < weight.length; i++) {
			for (int j = 0; j < weight[i].length; j++) {
				for (int k = 0; k < weight[i][j].length; k++) {
					System.out.println(weight[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public void printOutputPath(double data[]){
		/* calculate output */
		forwarding(data);
		
		/* print data */
		System.out.printf("Test Data [ %.2f",data[0]);
		for (int i = 1; i < data.length; i++) {
			System.out.printf(",%.2f", data[i]) ;
		}
		System.out.print(" ]\t->\t");
		
		/* print output */
		System.out.printf("%f",activate[layerNum-1][0]);
		for (int i = 1; i < activate[layerNum-1].length; i++) {
			System.out.printf(",%f", activate[layerNum-1][i]) ;
		}
		System.out.println();
		
		/* print output path */
		for (int i = 1; i < activate.length; i++) {
			System.out.printf("layer:%d\t->\t",i);
			for (int j = 0; j < activate[i].length; j++) {
				System.out.printf("%1.0f",activate[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
}
