package fi.metropolia.spagu.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import fi.metropolia.spagu.data.Visitor;

public class LogParser {

	public static final File FILE = new File("/home/birhanuh/Documents/Materials_Txt/weegee-location_16.07-29.07.txt");
	
	static LogParser logParser = new LogParser();

	public static void main(String[] args) {

		//logParser.parseLineCreateVisitor(LogParser.file);
		//logParser.summarizeList(logParser.parseVisitor(logParser.parseLineAndCreateVisitor(LogParser.file)));
		
		//System.out.println(logParser.summarizeList(logParser.parseVisitor(logParser.parseLineCreateVisitor(LogParser.file))).get(0).getRouteList().size());
			
		
	}
		
	/**
    public void readLog() {
		
		File file = new File("data/weegee-location.txt");
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanner.hasNextLine()) {
			parseRowLine(scanner.nextLine());
		}
		
	}		*/	
	
	public static ArrayList<Visitor> parseLineCreateVisitor(File file) {
		// Add a new Visitor object to the list, when a new line starts with 'person_mobilenumber:'
		ArrayList<Visitor> visitorsList = new ArrayList<Visitor>();
		
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanner.hasNextLine()) {
			String rowLine = scanner.nextLine();
						
			if (rowLine.startsWith("{type: location change|person_mobilenumber: ")) {
				Visitor visitor = new Visitor();
				visitorsList.add(visitor);
								
			} else {
				
				if (rowLine.startsWith("location:")){
					String location = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setLocation(location.trim());
					
				} else if (rowLine.startsWith("tag_id:")) {
					String tagId = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setTagId(tagId.trim());
				
				} else if (rowLine.startsWith("layout:")) {
					String layout = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setLayout(layout.trim()); 
					
				} else if (rowLine.startsWith("date:")) {
					String date = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setDate(date.trim());
					
				} else if (rowLine.startsWith("name:")) {
					String name = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setName(name.trim());
					
				} else if (rowLine.startsWith("time:")) {
					String time = rowLine.substring(rowLine.indexOf(' '), rowLine.length());
					visitorsList.get(visitorsList.size()-1).setTime(time.trim());
				}
			}					
		}	
		
		return visitorsList;
	}	

}
