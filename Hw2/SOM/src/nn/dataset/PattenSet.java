package nn.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PattenSet extends ArrayList<Patten>{

	/** create a data set */
	public PattenSet() {
		
	}
	
	/**
	 * auto load specific file and create patten set 
	 * @param fileName fileName with path
	 * @param dataDim dimension of data
	 * @param tagDim dimesion of tag with input data
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public PattenSet(String fileName,int dataDim,int tagDim) throws NumberFormatException, IOException {
		load(fileName, dataDim, tagDim);
	}

	/**
	 * load dataset from specific file name
	 * @param fileName fileName with path
	 * @param dataDim dimension of data
	 * @param tagDim dimesion of tag with input data
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void load(String fileName, int dataDim, int tagDim) throws NumberFormatException, IOException {
		BufferedReader br = br = new BufferedReader(new FileReader(new File(fileName)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* change to double tyoe */
			String strPT[] = line.split("\\s+");
			double dbPT[] = new double[strPT.length];
			for (int i = 0; i < dbPT.length; i++) {
				dbPT[i] = Double.parseDouble(strPT[i]);
			}

			/* normalize target value to 1 and -1 */
			for (int i = 0; i < tagDim; i++) {
				dbPT[dbPT.length - 1 - i] = dbPT[dbPT.length - 1 - i] > 0.5 ? 1 : 0;
			}
			this.add(new Patten(dataDim, dbPT));
		}
	}
}
