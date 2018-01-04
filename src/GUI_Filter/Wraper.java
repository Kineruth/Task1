package GUI_Filter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Algorithms.Algorithms;
import KML.ConvertCSVToKML;
import KML.LinesToSamples;
import MergedCSV.FileFormat;
import MergedCSV.MergeCSVfiles;
import MergedCSV.Sample;
import MergedCSV.WiFiNetwork;

public class Wraper {

	public static void folderAdded( String folderPath) {
		MergeCSVfiles mg = new MergeCSVfiles(folderPath);
		 DataBase.addData(mg.getSamplesFromFiles());
		 JOptionPane.showMessageDialog(new JFrame(), "Folder Added Succesfully!");
	}

	public static void mergedFileAdded(String filePath) throws IOException {
		FileFormat fm = new FileFormat();
		LinesToSamples ls = new LinesToSamples();
		File f = new File(filePath);
		if (fm.checkMergedCSVFormat(f))
			 DataBase.addData(ls.convertLines(ls.readCSV(filePath)));
		JOptionPane.showMessageDialog(new JFrame(), "File Added Succesfully !");
	}

	public static void saveMergedCSV() {
		MergeCSVfiles mg = new MergeCSVfiles();
		mg.writeFile(DataBase.dataBase);
		JOptionPane.showMessageDialog(new JFrame(), "File Saved To Desktop!");

	}

	public static void saveAsKML() throws FileNotFoundException, MalformedURLException {
		ConvertCSVToKML kml = new ConvertCSVToKML();
		kml.writeFile(DataBase.dataBase);
		JOptionPane.showMessageDialog(new JFrame(), "File Saved To Desktop!");
	}

	public static void clearance() {
		System.out.println("Samples amount before delete: " +  DataBase.dataBase.size());
		DataBase.deleteAllData();
		System.out.println("Samples amount after delete: " +DataBase.dataBase.size());
	}

	
public static void writeCurrentFilter(Filter f) throws IOException{
	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
	f.toString();
	FileOutputStream fOut = null;
	ObjectOutputStream objOut;
	try {
		fOut = new FileOutputStream("C:\\Users\\admin\\Desktop\\Filter - "+timeStamp+".txt");
		objOut = new ObjectOutputStream(fOut);

	// Write objects to file
	objOut.writeObject(f);

	objOut.close();
	fOut.close();
	JOptionPane.showMessageDialog(new JFrame(), "Filter Saved To Desktop!");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/**
 * This function gets a file path to a File that has a filter object written in bytes.
 * It reads from the file and retrieves the Filter.
 * @param filterFilePath a given file path.
 * @return the Filter object from given file.
 * @throws IOException
 * @throws ClassNotFoundException
 */
public static Filter readFilterFile(String filterFilePath) throws IOException, ClassNotFoundException{
	
	Filter filter ;
	FileInputStream fis = new FileInputStream(filterFilePath);
	ObjectInputStream ois = new ObjectInputStream(fis);

     filter = (Filter) ois.readObject();
	ois.close();
	fis.close();
	JOptionPane.showMessageDialog(new JFrame(), "Filter Uploaded Succesfully!");
	return filter;


}
public static void createAlgo1(String mac){
	List<Sample> temp = new ArrayList<Sample>(DataBase.dataBase);
	Algorithms a = new Algorithms();
	List<Sample> s=a.strongestMacLocation(temp, 4);
	for (Sample smp: s) {
		if(smp.getCommonNetworks().get(0).getMAC().equals(mac)){
			JOptionPane.showMessageDialog(new JFrame(), "LAT: "+smp.getLAT()+", LON: "+smp.getLON()+", ALT"+smp.getALT());
			break;
		}
	}
	
}

public static void createAlgo2(Sample s){
	
	
}

public static Sample convertToSample(String text) {
	Sample s = new Sample();
	String [] str= text.split(",");
	s.setTime(str[0]);
	s.setID(str[1]);
	s.setLAT(str[2]);
	s.setLON(str[3]);
	s.setALT(str[4]);
	for (int i = 5; i < str.length; i=i+4) {
		WiFiNetwork wn = new WiFiNetwork(str[i], str[i+1], str[i+2], str[i+3]);
		s.addNetwork(wn);
	}
	return s;	
}
}
