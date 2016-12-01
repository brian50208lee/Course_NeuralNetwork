package nn.network.som;

import java.util.ArrayList;
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
	//private double activate[][];
	
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
				double maxDist = (-1)*Double.MAX_VALUE;
				Patten p = null;
				Patten q = null;
				Patten r = null;
				Patten s = null;
				
				/* finding p,q,r,s */
				for (Patten pattern1 : pattenSet.getPattenList()) {
					for (Patten pattern2 : pattenSet.getPattenList()) {// for all pair
						if (pattern1 == pattern2)continue;
						
						/* check max and min distance */
						double dist = actDistance(m, pattern1, pattern2);
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
				notifyObserver();
				if (epoch%10==0) {
					System.out.printf("%d-%d\tmaxDist:%.25f\tminDist:%.25f\n",m, epoch, maxDist, minDist);	
				} 

				
				//System.out.printf("b : maxDist:%.25f\tminDist:%.25f\n", actDistance(m, p, q), actDistance(m, r, s));	
				
				/* reweight */
				reweight(m, p, q, r, s);
				//printWeight();
				//System.out.printf("b : maxDist:%.25f\tminDist:%.25f\n", actDistance(m, p, q), actDistance(m, r, s));	
				
				
			}
		}
	}
	private double[][] forwarding(double data[]){
		return forwarding(layerNum,data);
	}
	
	private double[][] forwarding(int layerToForward, double data[]){
		/* init output matrix */
		double activate[][] = new double[layerToForward][];
		for (int layer = 0; layer < activate.length; layer++) {
			activate[layer] = new double[networkInfo[layer]];
		}
		
		/* init input layer by data */
		for (int neural = 0; neural < networkInfo[0]; neural++) {
			activate[0][neural] = data[neural];
		}
		
		/* calculate activate value of hidden layer and output layer */
		for (int layer = 1; layer < activate.length; layer++) {
			for (int neural = 0; neural < networkInfo[layer]; neural++) {
				int baseIndex = networkInfo[layer-1];
				double baseWeight = weight[layer][neural][baseIndex];
				double value = baseWeight * 1;
				for (int link = 0; link < baseIndex; link++) {
					value += weight[layer][neural][link] * activate[layer-1][link];
				}
				activate[layer][neural] = activateFunc(value);
			}
		}
		
		return activate;
	}
	
	
	private void reweight(int m, Patten p, Patten q, Patten r, Patten s){
		/* get activation vector */
		double actP[][] = forwarding(m+1, p.getData());
		double actQ[][] = forwarding(m+1, q.getData());
		double actR[][] = forwarding(m+1, r.getData());
		double actS[][] = forwarding(m+1, s.getData());
		
		
		/*
		for (int i = 0; i < actP[m].length; i++)System.out.print(actP[m][i]+"  ");
		System.out.println("p"+p.getTarget()[0]);
		for (int i = 0; i < actP[m].length; i++)System.out.print(actQ[m][i]+"  ");
		System.out.println("q"+q.getTarget()[0]);
		for (int i = 0; i < actP[m].length; i++)System.out.print(actR[m][i]+"  ");
		System.out.println("r"+r.getTarget()[0]);
		for (int i = 0; i < actP[m].length; i++)System.out.print(actS[m][i]+"  ");
		System.out.println("s"+s.getTarget()[0]+"\n");
		*/

		//System.out.println("p,q\t"+p.getData()[0]+","+p.getData()[1]+"|"+q.getData()[0]+","+q.getData()[1]);
		//System.out.println("r,s\t"+r.getData()[0]+","+r.getData()[1]+"|"+s.getData()[0]+","+s.getData()[1]);
		
		/* reweight */
		for (int n = 0; n < networkInfo[m]; n++) {
			int baseWIdx = networkInfo[m-1];
			for (int k = 0; k < baseWIdx; k++) {
				weight[m][n][k] -= learningRateAtt*((actP[m][n]-actQ[m][n])*(actP[m][n]-actP[m][n]*actP[m][n])*actP[m-1][k]);
				weight[m][n][k] += learningRateAtt*((actP[m][n]-actQ[m][n])*(actQ[m][n]-actQ[m][n]*actQ[m][n])*actQ[m-1][k]);
				weight[m][n][k] += learningRateRep*((actR[m][n]-actS[m][n])*(actR[m][n]-actR[m][n]*actR[m][n])*actR[m-1][k]);
				weight[m][n][k] -= learningRateRep*((actR[m][n]-actS[m][n])*(actS[m][n]-actS[m][n]*actS[m][n])*actS[m-1][k]);
			}
			weight[m][n][baseWIdx] -= learningRateAtt*((actP[m][n]-actQ[m][n])*(actP[m][n]-actP[m][n]*actP[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateAtt*((actP[m][n]-actQ[m][n])*(actQ[m][n]-actQ[m][n]*actQ[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateRep*((actR[m][n]-actS[m][n])*(actR[m][n]-actR[m][n]*actR[m][n])*(1));
			weight[m][n][baseWIdx] -= learningRateRep*((actR[m][n]-actS[m][n])*(actS[m][n]-actS[m][n]*actS[m][n])*(1));

		}
	}
	
	
	public double[][] test(double data[]){
		/* calculate output */
		double resut[][] = forwarding(data);
		
		/* print data */
		StringBuilder resultString = new StringBuilder("Data [ ");
		for (int i = 0; i < data.length; i++) {
			resultString.append(String.format("%.2f,", data[i])) ;
		}
		resultString.replace(resultString.length()-1,resultString.length(), " ]");
		
		/* print result */
		resultString.append("\t->\t");
		for (int i = 0; i < resut[layerNum-1].length; i++) {
			resultString.append(String.format("%.10f,", resut[layerNum-1][i]));
		}
		resultString.replace(resultString.length()-1,resultString.length(), "");
		
		System.out.println(resultString);
		

		return resut;
	}
	
	private double activateFunc(double x){
		//return Math.tanh(x);
		//System.out.println("active:" + x +"," + (2*sigmoid(x)-1));
		return sigmoid(x);
	}
	private double sigmoid(double x){
		double value = 1 / (1+Math.exp(-x));
		return value;
	}	
	
	private double actDistance(int layer, Patten pattern1, Patten pattern2){
		/* forwarding and deep clone */
		double actP1[][] = forwarding(layer+1, pattern1.getData());
		double actP2[][] = forwarding(layer+1, pattern2.getData());
		
		/* compute distance */
		double dist = 0.0;
		for (int i = 0; i < actP1[layer].length; i++) {
			dist += (actP1[layer][i] - actP2[layer][i]) * (actP1[layer][i] - actP2[layer][i]);
		}
		return dist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void notifyObserver(){
		ArrayList<double[]> precisionPoint = new ArrayList<double[]>();
		int layer = 1;
		for (int neural = 0; neural < weight[layer].length; neural++) {
			
			double point[] = new double[weight[layer][neural].length-1];
			double denominator = 0;
			
			double wn = weight[layer][neural][weight[layer][neural].length-1] ;
			if (wn == 0.0)wn = Double.MIN_NORMAL*10000;
			
			for (int k = 0; k < weight[layer][neural].length-1; k++) {
				point[k] = (-1)*weight[layer][neural][k]*wn;
				denominator += weight[layer][neural][k] * weight[layer][neural][k];
			}

			for (int i = 0; i < point.length; i++) {
				point[i] /= denominator;
			}
			
			precisionPoint.add(point);
		}
		bpnSubject.setNeuralWeightList(precisionPoint);;
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
		double result[][] = forwarding(data);
		
		/* print data */
		System.out.printf("Test Data [ %.2f",data[0]);
		for (int i = 1; i < data.length; i++) {
			System.out.printf(",%.2f", data[i]) ;
		}
		System.out.print(" ]\t->\t");
		
		/* print output */
		System.out.printf("%f",result[layerNum-1][0]);
		for (int i = 1; i < result[layerNum-1].length; i++) {
			System.out.printf(",%f", result[layerNum-1][i]) ;
		}
		System.out.println();
		
		/* print output path */
		for (int i = 1; i < result.length; i++) {
			System.out.printf("layer:%d\t->\t",i);
			for (int j = 0; j < result[i].length; j++) {
				System.out.printf("%1.0f",result[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
}
