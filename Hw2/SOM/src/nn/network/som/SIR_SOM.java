package nn.network.som;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


import nn.dataset.Patten;
import nn.dataset.PattenSet;
import nn.observer.SOMSubject;

/**
 * Neural Network Hw2 <br>
 * Supervised Self-Ognization Map <br>
 * Reference Paper "Forced Accretion andyAssimilation Based on Self-Organizing Neural Network" <br>
 * @author Brian Lee
 */
public class SIR_SOM {
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
	 * @param layerNeuralNum e.g. [2,5,5] means 2-5-5 SIR_SOM NN
	 * @param iterations number of iterations each training epoch
	 * @param learningRateAtt attraction learning rate
	 * @param learningRateRep repelling learning rate 
	 */
	public SIR_SOM(int networkInfo[], int iterations, double learningRateAtt, double learningRateRep){
		/* check parameter */
		if (networkInfo.length <2) {
			throw new IllegalArgumentException("layer number must be greater than 1");
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
	public void train(PattenSet pattenSet){
		/* train pattenSet */
		System.out.println("Start Trainning ...");
		twoClassAlg(pattenSet);
		System.out.println("Done Traning");
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
				double lonDist = (-1) * Double.MAX_VALUE;
				double shrDist = Double.MAX_VALUE;
				Patten p = null, q = null;//same class longest pair
				Patten r = null, s = null;//diff class closet pair
				
				/* forwarding all traning data and get activation before compute */
				for(Patten patten : pattenSet){
					patten.setActivate(forwarding(patten.getData()));
				}

				/* find closet pair (p,q), farthest pair (r,s) */
				for (int i = 0; i < pattenSet.size(); i++) {// for all pair
					for (int j = i + 1; j < pattenSet.size(); j++) {
						/* get data pair */
						Patten pattern1 = pattenSet.get(i);
						Patten pattern2 = pattenSet.get(j);
						
						/* update closest and farthest by distance */
						double dist = actiDistance(m, pattern1, pattern2);
						if (pattern1.getTarget()[0] == pattern2.getTarget()[0]) {//same class
							if (dist > lonDist) {
								p = pattern1;
								q = pattern2;
								lonDist = dist;
							}
						} else {//diff class
							if (dist < shrDist) {
								r = pattern1;
								s = pattern2;
								shrDist = dist;
							}
						}
					}
				}
				
				/* print cloest and farthest distance */
				if (epoch % 1 == 0) {
					System.out.printf("Layer: %d\tEpoch: %d\t", m, epoch);	
					System.out.printf("Log-LongestDist:%.25f\t", Math.log(lonDist));
					System.out.printf("Log-ShortestDist:%.25f\n", Math.log(shrDist));
				}
	
				/* reweight */
				reweight(m, p, q, r, s);
				
				/* update gui if any */
				if(m == 1)notifyObserver();
			}
		}
	}
	
	
	private void randSelectPairAndReweight(int m, int epoch, PattenSet pattenSet){
		/* initial distance and patten referance */
		double lonDist = (-1) * Double.MAX_VALUE;
		double shrDist = Double.MAX_VALUE;
		Patten p = null, q = null;//same class longest pair
		Patten r = null, s = null;//diff class closet pair
		
		/* forwarding all traning data and get activation before compute */
		for(Patten patten : pattenSet){
			patten.setActivate(forwarding(patten.getData()));
		}
		
		/* random select pair */
		do{ p = pattenSet.get(new Random().nextInt(pattenSet.size())); }while(false);
		do{ q = pattenSet.get(new Random().nextInt(pattenSet.size())); }while(q==p || q.getTarget()[0] != p.getTarget()[0]);
		do{ r = pattenSet.get(new Random().nextInt(pattenSet.size())); }while(false);
		do{ s = pattenSet.get(new Random().nextInt(pattenSet.size())); }while(s==r || s.getTarget()[0] == r.getTarget()[0]);
		
		/* compute distance */
		lonDist = actiDistance(m, p, q);
		shrDist = actiDistance(m, r, s);
		
		/* print distance */
		if (epoch % 1 == 0) {
			System.out.printf("Layer: %d\tEpoch: %d\t", m, epoch);	
			System.out.printf("Log-LongestDist:%.25f\t", Math.log(lonDist));
			System.out.printf("Log-ShortestDist:%.25f\n", Math.log(shrDist));
		}
		
		/* reweight */
		reweight(m, p, q, r, s);
		
		/* update GUI if any */
		if(m == 1)notifyObserver();
	}

	
	/**
	 * @param m layer number
	 * @param p same class, longest pair
	 * @param q same class, longest pair
	 * @param r diff class, closet pair
	 * @param s diff class, closet pair
	 */
	private void reweight(int m, Patten p, Patten q, Patten r, Patten s){
		/* get activation matrix */
		double lonP[][] = forwarding(p.getData());//same class, longest pair
		double lonQ[][] = forwarding(q.getData());//same class, longest pair
		double shrR[][] = forwarding(r.getData());//diff class, closet pair
		double shrS[][] = forwarding(s.getData());//diff class, closet pair 
			
		/* reweight */
		for (int n = 0; n < networkInfo[m]; n++) {//eash neural
			int baseWIdx = networkInfo[m-1];
			for (int k = 0; k < baseWIdx; k++) {//each weight
				weight[m][n][k] -= learningRateAtt*((lonP[m][n]-lonQ[m][n])*(lonP[m][n]-lonP[m][n]*lonP[m][n])*lonP[m-1][k]);
				weight[m][n][k] += learningRateAtt*((lonP[m][n]-lonQ[m][n])*(lonQ[m][n]-lonQ[m][n]*lonQ[m][n])*lonQ[m-1][k]);
				weight[m][n][k] += learningRateRep*((shrR[m][n]-shrS[m][n])*(shrR[m][n]-shrR[m][n]*shrR[m][n])*shrR[m-1][k]);
				weight[m][n][k] -= learningRateRep*((shrR[m][n]-shrS[m][n])*(shrS[m][n]-shrS[m][n]*shrS[m][n])*shrS[m-1][k]);
			}
			/* base weight */
			weight[m][n][baseWIdx] -= learningRateAtt*((lonP[m][n]-lonQ[m][n])*(lonP[m][n]-lonP[m][n]*lonP[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateAtt*((lonP[m][n]-lonQ[m][n])*(lonQ[m][n]-lonQ[m][n]*lonQ[m][n])*(1));
			weight[m][n][baseWIdx] += learningRateRep*((shrR[m][n]-shrS[m][n])*(shrR[m][n]-shrR[m][n]*shrR[m][n])*(1));
			weight[m][n][baseWIdx] -= learningRateRep*((shrR[m][n]-shrS[m][n])*(shrS[m][n]-shrS[m][n]*shrS[m][n])*(1));

		}
	}

	
	/** Activation function with sigmoid function */
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
	 * @param layer layer number of output space
	 * @param pattern1 first data object
	 * @param pattern2 second data object
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
	
	/**
	 * Bugging ... (because of labeler unimplement)
	 * @param pattenSet training data set 
	 * @return percntage of correct rate
	 */
	public double testCorrectRate(PattenSet pattenSet){
		System.out.println("Bugging ... (because of labeler unimplement)");
		int numberOfCorrect = 0;
		for(Patten patten : pattenSet){
			double tag[] = patten.getTarget();
			double resultTag[] = forwarding(patten.getData())[layerNum-1];
			numberOfCorrect++;
			for (int i = 0; i < tag.length; i++) {
				if((int)(tag[i] + 0.5) != (int)(resultTag[i] + 0.5)){
					numberOfCorrect--;
					break;
				}
			}
		}
		double correctRate = 1.0 * numberOfCorrect / pattenSet.size();
		System.out.printf("Correct Rate: %.2f%%\n", correctRate*100);
		return correctRate;
	}
	
	
	
	/** Compute precision point in first hidden layer and signal observer */ 
	private void notifyObserver(){
		ArrayList<double[]> precisionPoint = new ArrayList<double[]>();
		
		/* conmpute precision point in first hidden layer */
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
		
		/* update precision point and signal all observers */
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
			for (Patten patten : pattenSet) {
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
