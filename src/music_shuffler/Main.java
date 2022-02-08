package music_shuffler;

import java.io.File;

public class Main {

	private static File source;
	private static File destFolder;
	private static long sizeLimit = 2;
	private static File configFile = new File("config");
	
	public Main(){
		
	}
	
	public static void main(String[] args){
		Main m = new Main();
		try{
			for(int i = 0; i<args.length; i++){
				switch(args[i]){
					case "-src": source = new File(args[i+1]); break;
					case "-size": sizeLimit = Long.valueOf(args[i+1].trim()); break;
					case "-destFolder" : destFolder = new File(args[i+1]); break;
					case "-h" : System.out.println("Usage : java -jar music_shuffler.jar -src /path/to/src -destFolder /path/to/destFolder -sizeLimit 4"); System.exit(0);
				}
			}
				if(sizeLimit == 2){
					System.out.println("You have not specified a size limit using default of 2GB");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(!source.exists()){
					System.err.println("Error "+source.getPath()+" does not exist.\n Exiting");
					System.exit(1);
				}
				if(destFolder.exists()){
					System.out.println("Destination folder already exists, it will be cleaned of contents!");
					System.out.println("If you wish to stop hit CTRL-C!");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				 sizeLimit = sizeLimit * 1073741824L;
				File_Grabber fg = new File_Grabber(source, destFolder, sizeLimit, configReader.readConfig(configFile));
		}catch (Exception e){
			System.out.println("If you had errors, did you include a -src and a -destFolder parameter?");
		}
	}
}