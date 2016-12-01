package nn.network.som;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;




import jdk.nashorn.internal.ir.annotations.Ignore;
import nn.dataset.Patten;
import nn.dataset.PattenSet;
import nn.observer.SOMSubject;

/**
 * Neural Network Hw2 <br>
 * Supervised Self-Ognization Map <br>
 * Reference Paper "Forced Accretion andyAssimilation Based on Self-Organizing Neural Network" <br>
 * @author Owner brian lee
 */
public class SOM {
	/** observable */
	private SOMSubject bpnSubject;
	
	/** total layer in network include input and output layer */
	private int layerNum;
	
	/** information about how many neural in each layer */
	private int networkInfo[];
	
	/** max epochs each training layer */
	private int epochs;
	
	/** learning rate attraction */
	private double learningRateAtt;
	
	/** learning rate repelling */
	private double learningRateRep;
	
	/** weight[layer][neural][k-th weight] {w1,w2,...,wb} */
	private double weight[][][];
	
	
	/** @return subject of SOM */
	public SOMSubject getSubject(){return this.bpnSubject;}
	
	/**
	 * Constructor of SOM
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
		this.epochs = iterations;
		this.learningRateAtt = learningRateAtt;
		this.learningRateRep = learningRateRep;
		
		/* init subject */
		this.bpnSubject = new SOMSubject();
		
		/* initail newwork */
		initNetwork();
	}
	
	/** Training SOM network by patten set */
	public void training(PattenSet pattenSet){
		/* train pattenSet */
		twoClassAlg(pattenSet);
	}
	
	/** Initial network weight by random gaussian in [0,1] */
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
	
	/**
	 * Return activation matrix with data
	 * @param data data array
	 * @return matrix activation[layer][neural]
	 */
	private double[][] forwarding(double data[]){
		/* init activation matrix */
		double activate[][] = new double[layerNum][];
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
	
	
	/**
	 * Simple SOM algorithm <br>
	 * Can only handle two classes data
	 * @param pattenSet training data set
	 */
	public void twoClassAlg(PattenSet pattenSet){
		for (int m = 1; m < layerNum; m++) {//each layer
			for (int epoch = 1; epoch <= epochs; epoch++) {//limited epochs
				
				/* initial distance and patten referance */
				double clsDist = Double.MAX_VALUE;
				double farDist = (-1)*Double.MAX_VALUE;
				Patten p = null;//closet pair
				Patten q = null;//closet pair
				Patten r = null;//closet farthest
				Patten s = null;//closet farthest
				
				/* forwarding all traning data and get activation before compute */
				for(Patten patten : pattenSet.getPattenList()){
					patten.setActivate(forwarding(patten.getData()));
				}
				
				/* find closet pair (p,q), farthest pair (r,s) */
				for (Patten pattern1 : pattenSet.getPattenList()) {// for all pair
					for (Patten pattern2 : pattenSet.getPattenList()) {
						/* ignore self pair */
						if (pattern1 == pattern2)continue;
						
						/* update closet and farthest by distance */
						double dist = actiDistance(m, pattern1, pattern2);
						if (pattern1.getTarget()[0] == pattern2.getTarget()[0]) {//same class
							if (dist > farDist) {
								p = pattern1;
								q = pattern2;
								farDist = dist;
							}
						} else {//diff class
							if (dist < clsDist) {
								r = pattern1;
								s = pattern2;
								clsDist = dist;
							}
						}
					}
				}
				
				/* print cloest and farthest distance */
				if (epoch % 10 == 0) {
					System.out.printf("Layer: %d\tEpoch: %d\t",m, epoch);	
					System.out.printf("Shortest Dist:%.25f\t", farDist);
					System.out.printf("Longest Dist:%.25f\n", clsDist);
				}
	
				/* reweight */
				reweight(m, p, q, r, s);
				
				/* update gui if any */
				if(m == 1)notifyObserver();
			}
		}
	}

	
	/**
	 * @param m layer number
	 * @param p closet pair
	 * @param q closet pair
	 * @param r farthest pair
	 * @param s farthest pair
	 */
	private void reweight(int m, Patten p, Patten q, Patten r, Patten s){
		/* get activation matrix */
		double clsP[][] = forwarding(p.getData());//closet 
		double cloQ[][] = forwarding(q.getData());//closet 
		double farR[][] = forwarding(r.getData());//farthest 
		double farS[][] = forwarding(s.getData());//farthest 
			
		/* reweight */
		for (int n = 0; n < networkInfo[m]; n++) {//eash neural
			int baseWIdx = networkInfo[m-1];
			for (int k = 0; k < baseWIdx; k++) {//each weight
				weight[m][n][k] -= learningRateAtt*((clsP[m][n]-cloQ[m][n])*(clsP[m][n]-clsP[m][n]*clsP[m][n])*clsP[m-1][k]);
				weight[m][n][k] += learningRateAtt*((clsP[m][n]-cloQ[m][n])*(cloQ[m][n]-cloQ[m][n]*cloQ[m][n])*cloQ[m-1][k]);
				weight[m][n][k] += learningRateRep*((farR[m][n]-farS[m][n])*(farR[m][n]-farR[m][n]*farR[m][n])*farR[m-1][k]);
				weight[m][n][k] -= learningRateRep*((farR[m][n]-farS[m][n])*(farS[m][n]-farS[m][n]*farS[m][n])*farS[m-1][k]);
			}
			/* base weight */
			weight[m][n][baseWIdx] -= learningRateAtt*((clsP[m][n]-cloQ[m][n])*(clsP[m][n]-clsP[m][n]*clsP[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateAtt*((clsP[m][n]-cloQ[m][n])*(cloQ[m][n]-cloQ[m][n]*cloQ[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateRep*((farR[m][n]-farS[m][n])*(farR[m][n]-farR[m][n]*farR[m][n])*(1));
			weight[m][n][baseWIdx] -= learningRateRep*((farR[m][n]-farS[m][n])*(farS[m][n]-farS[m][n]*farS[m][n])*(1));

		}
	}
	
	/**
	 * Test data by current network and return ouput value in each layers
	 * @param data intput data array
	 * @return matrix activat[layer][neural]
	 */
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
	
	/** Activation function */
	private double activateFunc(double x){
		//return Math.tanh(x);
		return sigmoid(x);
	}
	
	/** Sigmoid function */
	private double sigmoid(double x){
		double value = 1 / (1+Math.exp(-x));
		return value;
	}	
	
	/**
	 * Compute activate distance in euclide space with specify layer output space 
	 * @param layer layer of output space
	 * @param pattern1 first patten object
	 * @param pattern2 second patten object
	 * @return euclide distance in ouput space
	 */
	private double actiDistance(int layer, Patten pattern1, Patten pattern2){
		/* get activation Value */
		double actiP1[] = pattern1.getActivate()[layer];
		double actiP2[] = pattern2.getActivate()[layer];
		
		/* compute distance */
		double dist = 0.0;
		int neuralNumber = actiP1.length;
		for (int neural = 0; neural < neuralNumber; neural++) {
			dist += (actiP1[neural] - actiP2[neural]) * (actiP1[neural] - actiP2[neural]);
		}
		
		return dist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** Compute precision point in first hidden layer and signal observer */ 
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** Print neural weight (w1 w1 ... wb) in each layer instead of input layer */
	public void printWeight(){
		System.out.println("weight : ");
		for (int i = 1; i < weight.length; i++) {
			System.out.printf("Layer: %d\n", i);
			for (int j = 0; j < weight[i].length; j++) {
				System.out.printf("Neural: %d\t", j);
				for (int k = 0; k < weight[i][j].length; k++) {
					System.out.printf("%.10f",weight[i][j][k]);
					if(k == weight[i][j].length - 1)System.out.println();
					else System.out.print(",");
				}
			}
		}
		System.out.println();
	}
	
	/** print data output value in each layer */ 
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
	
	
	/**
	 * Compute 2D class image by sampling <br>
	 * Point with Cartesian Coordinate
	 * @pattenSet training data set
	 * @param imgWidth sampling image width
	 * @param imgHeight sampling image height
	 * @param sPoint top-left sampling data point double[x,y]
	 * @param ePoint bottom-right sampling data point double[x,y]
	 * @return image
	 */
	public BufferedImage get2DClassImage(PattenSet pattenSet, int imgWidth, int imgHeight, double[] sPoint, double[] ePoint){
		BufferedImage classImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		double deltX = (ePoint[0] - sPoint[0]) / imgWidth;
		double deltY = (sPoint[1] - ePoint[1]) / imgHeight;
		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgHeight; x++) {
				double cartX = sPoint[0] + x * deltX;
				double cartY = sPoint[1] - y * deltY;
				int classify1 = (int)(forwarding(new double[]{cartX, cartY})[layerNum-1][0]+0.5);
				
				if (classify1 == 1) {
					classImg.setRGB(x, y, Color.RED.getRGB());
				} else {
					classImg.setRGB(x, y, Color.BLUE.getRGB());
				}
			}
		}
		
		/* graw patten set */
		if (pattenSet != null) {
			for (Patten patten : pattenSet.getPattenList()) {
				int pixelX = (int)((patten.getData()[0] - sPoint[0]) / deltX);
				int pixelY = (int)((patten.getData()[1] - sPoint[1]) / deltY)*(-1);
				
				/* draw circle */
				int radius = 2;
				for (int x = pixelX-radius; x <= pixelX+radius; x++) {
					for (int y = pixelY-radius; y <= pixelY+radius; y++) {
						if ((x-pixelX)*(x-pixelX)+(y-pixelY)*(y-pixelY) > radius*radius) {
							continue;
						}
						try {
							if (patten.getTarget()[0] == 1) {
								classImg.setRGB(x, y, Color.BLACK.getRGB());
							} else {
								classImg.setRGB(x, y, Color.WHITE.getRGB());
							}
						} catch (Exception e) {
							//ignore
						}
					}
				}
			}
		}
		
		return classImg;
	}
	
	
}
