package imports;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import simulation.Sheep;

//http://stackoverflow.com/questions/2850674/where-to-put-a-textfile-i-want-to-use-in-eclipse
//http://stackoverflow.com/questions/3432970/import-textfile-and-read-line-by-line-in-java
//http://docs.oracle.com/javase/tutorial/essential/io/charstreams.html

public class DataImporter {
	
	// http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html
	public static final Pattern lineComment = Pattern.compile("//");
	public static final Pattern blockComment = Pattern.compile("/");
	public static Matcher m;
	
	public static ArrayList<Sheep> importHerd() throws IOException {
		File file = new File("src/imports/defaultHerd.txt");
		BufferedReader inputStream = null;
		Scanner sc;
		
		ArrayList<Sheep> herd = new ArrayList<Sheep>();
		
		try {
			
			inputStream = new BufferedReader(new FileReader("src/imports/defaultHerd.txt"));
			
			String line;
			
			sc = new Scanner(file);
			sc.useLocale(Locale.US); // Til þess að geta borið kennsl á double.
			
			int n = 0;
			int m = 3;
			Object[] data = new Object[3];
			Object o;
			
			while (sc.hasNext()){
				if(n == 0 || n == 1){
					o = sc.nextInt();
					System.out.print(o + " ");
					n = rotate(n, m);
					data[n] = 0;
				}
				if(n == 2){
					o = sc.nextDouble();
					System.out.println(o + " ");
					n = rotate(n, m);
					data[n] = 0;
					herd.add(parseSheepData(data));
				}
			}
			
			
		} finally {
	    	
	    	if (inputStream != null) {
                inputStream.close();
            }
	    	
	    }
		
		return herd;
	
	}
	
	public static Sheep parseSheepData(Object[] data){
		return new Sheep();
	}
	
	public static int rotate(int n, int m){
		return ++n%m;
	}
	
    public static void main(String[] args)  {
    	/*
    	try{
    		importHerd();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	*/
    }
	
	public static void exportHerd(){

	}
}
