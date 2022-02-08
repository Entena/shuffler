package music_shuffler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystemLoopException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class File_Grabber {

	private File musicFolder;
	private File destFolder;
	private List<File> songs = new ArrayList<File>();
	private List<File> fresh = new ArrayList<File>();
	private List<String> nocopy = new ArrayList<String>(); //This is a list of files not to copy.
	private long counter = 0L;
	private long limit;
	private boolean go_on =  true;
	
	public File_Grabber(File musicFolder, File destFolder, long limit, List<String> nocopy){
		this.limit = limit;
		this.musicFolder = musicFolder;
		this.nocopy = nocopy;
		this.destFolder = destFolder;
		dataGatherer(musicFolder);
	if(destFolder.exists()){
			clean(destFolder);
		}
		destFolder.mkdirs();
		begin();
		middle();
		System.out.println("Done: tracks copied "+fresh.size()+"\n Playlist file: playlist.m3u");
	}
	
	private void begin(){
		randomize();
		playListMaker();
	}
	
	private void middle(){
		File dest;
		for(File s : fresh){
			try {
				dest = new File(destFolder.getPath()+"/"+s.getName());
				copy(s , dest);
				System.out.println("Copied "+s.getName());
			} catch (IOException e) {
				e.getMessage();
			}
		}
	}
	
	
	private void dataGatherer(File folder){
		if(folder.isDirectory()){
			File[] files = folder.listFiles();
			for(int i=0; i < files.length; i++){
				if(files[i].isDirectory()){
					dataGatherer(files[i]);
				}else if(files[i].getName().toLowerCase().contains("mp3") || files[i].getName().toLowerCase().contains(".aac") || files[i].getName().toLowerCase().contains(".m4a")){
					songs.add(files[i]);
				}
			}
		}else{
			songs.add(folder);
		}
	}
	
	private void randomize(){
		Random rand = new Random();
		while(go_on){	
			if(counter < limit){
				File song =	songs.get(rand.nextInt(songs.size()));
				for(String notallowed : nocopy){
					if(song.getPath().toLowerCase().contains(notallowed)){
						song = songs.get(rand.nextInt(songs.size()));
						break;
					}
				}
				if(!fresh.contains(song) && !fresh.contains(song.getName())){
					counter += song.length();
					if(counter < limit){
						fresh.add(song);
					}else{
						go_on = false;
					}
				}else{
				 go_on = true;
				}
			}else{
				go_on = false;
			}
		}
	}
	
	private void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

	private void clean(File dir){
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for(int i = 0; i<files.length; i++){
				clean(files[i]);
			}
		}else{
			dir.delete();
		}
	}
	
	private void playListMaker(){
		try {
			File playlist = new File(destFolder.getPath()+"/Playlist.m3u");
			if(playlist.exists()){
				playlist.delete();
			}
			FileWriter fw = new FileWriter(playlist.getPath());
			for(File song : fresh){
				fw.write(song.getName()+"\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
