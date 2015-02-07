package fi.metropolia.spagu.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ResourceMapping {

	public final static HashMap <String, String> ROOMMAPPING = ResourceMapping.initRooms(); 
	public final static HashMap <String, String> MUSEUMMAPPING = ResourceMapping.initMuseums();
	
	public static void main(String[] args) {
		System.out.println(ROOMMAPPING);
		System.out.println(MUSEUMMAPPING);
		
		String nodename = "node1_4";
		String result = ROOMMAPPING.get(nodename);
		if(result== null) {
			System.err.println("Fail, unknown room!");
		} else {
			System.out.println("Corresponding room:" + ROOMMAPPING.get(nodename));
			System.out.println("Corresponding museum:" + MUSEUMMAPPING.get(nodename));
		}
		
		
	}
	
	public static HashMap<String, String> initRooms() {
		
		HashMap <String, String> result = new HashMap<String, String>();
		
		Scanner scanner;
		try {
			scanner = new Scanner(new File("data/rooms.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			int sep = line.indexOf(':');
			String key = line.substring(0, sep);
			String value = line.substring(sep+1, line.length()).trim();		
			
			result.put(key, value);
		}
		
		return result;
	}
	
	public static HashMap<String, String> initMuseums() {
		
		HashMap <String, String> result = new HashMap<String, String>();
		
		Scanner scanner;
		try {
			scanner = new Scanner(new File("data/museums.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			int sep = line.indexOf(':');
			String key = line.substring(0, sep);
			String value = line.substring(sep+1, line.length()).trim();
			
			result.put(key, value);
		}
		
		return result;
	}
	
	
}
