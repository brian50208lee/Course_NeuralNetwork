package nn.demo;

public class ArgumentChecker {
	//public String train_data = "hw2.dat";
	//public int[] network = new int[]{2,5,5,5,5,5,1};
	
	public String train_data = null;
	public int[] network = null;
	public int iter = 5000;
	public double learn_att = 0.01;
	public double learn_rep = 10;
	

	public ArgumentChecker(String[] args) {
		if(args.length < 1)									printUsageAndExit();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--help"))					printUsageAndExit();
			else if (args[i].equals("--train_data"))		train_data = args[++i];
			else if(args[i].equals("--network"))			network = parseToIntArray(args[++i]);
			else if(args[i].equals("--iter"))				iter = Integer.parseInt(args[++i]);
			else if(args[i].equals("--learn_att"))			learn_att = Double.parseDouble(args[++i]);
			else if(args[i].equals("--learn_rep"))			learn_rep = Double.parseDouble(args[++i]);
			else {
				printArgumentError(String.format("unknow argument -> %s\n", args[i]));
				printUsageAndExit();
			}
		}
		
		if (train_data == null) {
			printArgumentError("no --train_data");
			printUsageAndExit();
		} 
		if (network == null) {
			printArgumentError("no --network");
			printUsageAndExit();
		} 
	}
	private void printArgumentError(String message){
		System.out.printf("Argument error: %s\n", message);
	}
	
	private void printUsageAndExit(){
		System.out.printf("SIR_SOM parameters:\n");

		System.out.printf("    Parameters:\n");
		System.out.printf("        --help:              parameter introduction \n");
		System.out.printf("        --train_data:        the training data file, not optional \n");
		System.out.printf("        --network:           define neural number in each layer, e.g. 2,5,5,5,5,5,1, not optional \n");
		System.out.printf("        --iter:              number of iterations each layer, default 5000 \n");
		System.out.printf("        --learn_att:         attraction learning rate, default 0.01 \n");
		System.out.printf("        --learn_rep:         repelling learning rate, default 10.0 \n");
		System.out.printf("\n");
		
		System.out.printf("    Example:\n");
		System.out.printf("        java -jar som.jar --train_data ./hw2.dat --network 2,5,5,5,5,5,1 \n");
		System.out.printf("        java -jar som.jar --train_data ./hw2.dat --network 2,5,5,5,5,5,1 --iter 5000 --learn_att 0.01 --learn_rep 10 \n");
		System.out.printf("\n");
		
		System.exit(0);
	}
	
	private int[] parseToIntArray(String strArgv){
		String[] strArray = strArgv.split("\\,");
		int[] intArray = new int[strArray.length];
		for (int j = 0; j < intArray.length; j++) {
			intArray[j] = Integer.parseInt(strArray[j]);
		}
		return intArray;
	}

}
