package nn.network.som;

import java.util.Random;

import nn.dataset.Patten;
import nn.dataset.PattenSet;
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
				weight[layer][neural] = new double[networkInfo[layer - 1] + 1];
				for (int link = 0; link < networkInfo[layer - 1] + 1; link++) {//+1 for base weight
					weight[layer][neural][link] = random.nextGaussian();
				}
			}
		}
		
		/* init activate matrix */
		activate = new double[networkInfo.length][];
		for (int layer = 0; layer < networkInfo.length; layer++) {
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
		for (int m = 1; m < layerNum; m++) {//each layer
			for (int epoch = 0; epoch < iterations; epoch++) {//limited epochs
				double minDist = Double.MAX_VALUE;
				double maxDist = Double.MIN_VALUE;
				Patten p = null;
				Patten q = null;
				Patten r = null;
				Patten s = null;
				
				/* finding p,q,r,s */
				for (Patten pattern1 : pattenSet.getPattenList()) {
					for (Patten pattern2 : pattenSet.getPattenList()) {// for all pair
						/* forwarding and deep clone */
						double actP1[][] = forwarding(pattern1.getData()).clone();
						for (int i = 0; i < actP1.length; i++)actP1[i] = actP1[i].clone();
						double actP2[][] = forwarding(pattern2.getData()).clone();
						for (int i = 0; i < actP2.length; i++)actP2[i] = actP2[i].clone();
						
						/* check max and min distance */
						double dist = distance(actP1[m], actP2[m]);
						if (pattern1.getTarget()[0] == pattern2.getTarget()[0]) {//same class
							if (dist > maxDist) {
								p = pattern1;
								q = pattern2;
								maxDist = dist;
							}
						} else {//diff class
							if (dist < minDist) {
								r = pattern1;
								s = pattern2;
								minDist = dist;
							}
						}
						
					}
				}
				if (epoch%10==0) {
					System.out.printf("%d-%d\tmaxDist:%.25f\tminDist:%.25f\n",m, epoch, maxDist, minDist);	
				} 

				
				
				/* reweight */
				reweight(m, p, q, r, s);
			}
		}
	}
	
	private double[][] forwarding(double data[]){
		/* init input layer by data */
		for (int neural = 0; neural < networkInfo[0]; neural++) {
			activate[0][neural] = data[neural];
		}
		
		/* calculate activate value of hidden layer and output layer */
		for (int layer = 1; layer < layerNum; layer++) {
			for (int neural = 0; neural < networkInfo[layer]; neural++) {
				int baseIndex = weight[layer][neural].length-1;
				double baseWeight = weight[layer][neural][baseIndex];
				double value = baseWeight * 1;
				for (int link = 0; link < baseIndex; link++) {
					value += weight[layer][neural][link] * activate[layer-1][link];
				}
				activate[layer][neural] = sigmoid(value);
			}
		}
		
		return this.activate;
	}
	
	
	private void reweight(int m, Patten p, Patten q, Patten r, Patten s){
		/* get activation vector */
		double actP[][] = forwarding(p.getData()).clone();
		for (int i = 0; i < actP.length; i++)actP[i] = actP[i].clone();
		double actQ[][] = forwarding(q.getData()).clone();
		for (int i = 0; i < actQ.length; i++)actQ[i] = actQ[i].clone();
		double actR[][] = forwarding(r.getData()).clone();
		for (int i = 0; i < actR.length; i++)actR[i] = actR[i].clone();
		double actS[][] = forwarding(s.getData()).clone();
		for (int i = 0; i < actS.length; i++)actS[i] = actS[i].clone();
		
		/* reweight */
		for (int n = 0; n < weight[m].length; n++) {
			int baseIndex = weight[m][n].length-1;
			for (int k = 0; k < baseIndex; k++) {
				weight[m][n][k] -= learningRateAtt*((actP[m][n]-actQ[m][n])*(actP[m][n]-actP[m][n]*actP[m][n])*actP[m-1][k]);
				weight[m][n][k] += learningRateAtt*((actP[m][n]-actQ[m][n])*(actQ[m][n]-actQ[m][n]*actQ[m][n])*actQ[m-1][k]);
				weight[m][n][k] += learningRateRep*((actR[m][n]-actS[m][n])*(actR[m][n]-actR[m][n]*actR[m][n])*actR[m-1][k]);
				weight[m][n][k] -= learningRateRep*((actR[m][n]-actS[m][n])*(actS[m][n]-actS[m][n]*actS[m][n])*actS[m-1][k]);
			}
			weight[m][n][baseIndex] -= learningRateAtt*((actP[m][n]-actQ[m][n])*(actP[m][n]-actP[m][n]*actP[m][n])*(-1));
			weight[m][n][baseIndex] += learningRateAtt*((actP[m][n]-actQ[m][n])*(actQ[m][n]-actQ[m][n]*actQ[m][n])*(-1));
			weight[m][n][baseIndex] += learningRateRep*((actR[m][n]-actS[m][n])*(actR[m][n]-actR[m][n]*actR[m][n])*(-1));
			weight[m][n][baseIndex] -= learningRateRep*((actR[m][n]-actS[m][n])*(actS[m][n]-actS[m][n]*actS[m][n])*(-1));

		}
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
	
	private double distance(double yp[], double yq[]){
		if (yp.length != yq.length) {
			throw new IllegalArgumentException("array length not equal.");
		} 
		
		double dist = 0.0;
		for (int i = 0; i < yq.length; i++) {
			dist += (yp[i] - yq[i]) * (yp[i] - yq[i]);
		}
		return dist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void notifyObserver(){
		
		//bpnSubject.setPrecisionPoint(pointList);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void printWeight(){
		System.out.println("weight : ");
		for (int i = 1; i < weight.length; i++) {
			for (int j = 0; j < weight[i].length; j++) {
				for (int k = 0; k < weight[i][j].length; k++) {
					System.out.print(weight[i][j][k] + " ");
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
