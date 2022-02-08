package music_shuffler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class configReader {

	public configReader(){
		
	}
	
	public static List<String> readConfig(File configFile){
		List<String> lines = new ArrayList<>();
		if(configFile.exists()){
			try{
				Scanner conf = new Scanner(configFile);
				String line;
				while(conf.hasNextLine()){
					line = conf.nextLine().toLowerCase();
					if(!line.startsWith("#")){
						lines.add(line);
					}
				}
			}catch (FileNotFoundException e){
				e.getMessage();
			}
	}else{
		System.out.println("Error no config file! No song checking will happen, this means ALL your library is eligible to be copied");
	}
		return lines;
	}
	
}
