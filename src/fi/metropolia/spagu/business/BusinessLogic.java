package fi.metropolia.spagu.business;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import fi.metropolia.spagu.data.SummerizedVisitor;
import fi.metropolia.spagu.data.SummerizedVisitorContainer;
import fi.metropolia.spagu.data.Visitor;

public class BusinessLogic {

	static LogParser logParser = new LogParser();
	static BusinessLogic businessLogic = new BusinessLogic();

	public static void main(String[] args) {
/**
		System.out.println("Event No: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getEventNo() +"\n"
				+ "TagID: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getTagId() +"\n"
				+ "Layout: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getLayout() +"\n"
				+ "Name: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getName() +"\n"
				+ "Museum name: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getMuseumName() +"\n"
				+ "Place name: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getPlaceName() +"\n"
				+ "Node name: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getNodeName() +"\n"
				+ "Start time: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getStartTime() +"\n"
				+ "End time: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getEndTime() +"\n"
				+ "Duration: " + businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)).get(2).getDuration() +"\n");
		
		System.out.println("Event Map: " + businessLogic.createTagIDEventMap(businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE))).size());
		*/
		for (SummerizedVisitor summarizedVisitorsList : businessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE)) ) {
			System.out.println("------------------------------------------------------------------|");
			System.out.println("Event No: " + summarizedVisitorsList.getEventNo() +"\n"
					+ "TagID: " + summarizedVisitorsList.getTagId() +"\n"
					+ "Name: " + summarizedVisitorsList.getName() +"\n"
					+ "Museum name: " + summarizedVisitorsList.getMuseumName() +"\n"
					+ "Place name: " + summarizedVisitorsList.getPlaceName() +"\n"
					+ "Node name: " + summarizedVisitorsList.getNodeName() +"\n"
					+ "Start time: " + summarizedVisitorsList.getStartTime() +"\n"
					+ "End time: " + summarizedVisitorsList.getEndTime() +"\n"
					+ "Duration: " + summarizedVisitorsList.getDuration() +"\n");
		}
		
	}
	
	//public HashMap<String, Long> placeREMOVE = new HashMap<String, Long>();
	//public HashMap<String, Long> museumREMOVE = new HashMap<String, Long>();
	
	public static ArrayList<SummerizedVisitor> summarizeVisitorList(ArrayList<Visitor> visitorsList) {

		ArrayList<SummerizedVisitor> summarizedVisitorList = new ArrayList<SummerizedVisitor>();
		ArrayList<SummerizedVisitorContainer> summarizedVisitorContainerList = new ArrayList<SummerizedVisitorContainer>();	
		
		for (Visitor visitor : visitorsList) {
			String tagId = visitor.getTagId();	
			String locatioin = visitor.getLocation();
			String date = visitor.getDate();
			String time = visitor.getTime();
  			
			if (summarizedVisitorContainerList.size() == 0) {
				SummerizedVisitorContainer summeriVisitorNew = new SummerizedVisitorContainer();
				summeriVisitorNew.setTagId(visitor.getTagId());
				summeriVisitorNew.setLayout(visitor.getLayout());
				summeriVisitorNew.setName(visitor.getName());
				summeriVisitorNew.setNodeName(visitor.getLocation());
				summeriVisitorNew.setStartTime(visitor.getDate()+ " " +visitor.getTime());
				
				summarizedVisitorContainerList.add(summeriVisitorNew);
			
			} else {
				// flag for checking if it is the same visitor 
				boolean sameVisitor = false;
				
				for (SummerizedVisitorContainer visitorSummeri : summarizedVisitorContainerList) {
					
					if (visitorSummeri.getTagId().equals(tagId)) {
						sameVisitor = true;
						
						if (!visitorSummeri.getNodeName().equals(locatioin)) {  
																
							SummerizedVisitor summeriVisitor = new SummerizedVisitor();
							
							summeriVisitor.setTagId(Integer.parseInt(visitorSummeri.getTagId()));
							summeriVisitor.setLayout(visitorSummeri.getLayout());
							summeriVisitor.setName(visitorSummeri.getName());
							summeriVisitor.setNodeName(visitorSummeri.getNodeName());
							summeriVisitor.setStartTime(strToData2(visitorSummeri.getStartTime()));
							summeriVisitor.setEndTime(strToData2(date+ " " +time));
							
							long elapsedTime = getElapsedTimeMillisecs(stringToDate(visitorSummeri.getStartTime()),
									stringToDate(date+ " " +time));
							summeriVisitor.setDuration(elapsedTime);
						
							String place = ResourceMapping.ROOMMAPPING.get(visitorSummeri.getNodeName());
							
							if(place == null) {
								// End parsing
								System.err.println("Fail, room null for node: " +visitorSummeri.getNodeName());
								System.exit(0);
							} else {
								summeriVisitor.setPlaceName(place);								
							}								
							String museum = ResourceMapping.MUSEUMMAPPING.get(visitorSummeri.getNodeName());
							
							if(museum == null) {
								// End parsing
								System.err.println("Fail, museum null for node: " +visitorSummeri.getNodeName());
								System.exit(0);
							} else {
								summeriVisitor.setMuseumName(museum);					
							}							
							// Update visitor data in summarizedVisitorContainerList
							int index = summarizedVisitorContainerList.indexOf(visitorSummeri);
							summarizedVisitorContainerList.get(index).setTagId(visitor.getTagId());
							summarizedVisitorContainerList.get(index).setLayout(visitor.getLayout());
							summarizedVisitorContainerList.get(index).setName(visitor.getName());
							summarizedVisitorContainerList.get(index).setNodeName(visitor.getLocation());
							summarizedVisitorContainerList.get(index).setStartTime(visitor.getDate()+ " " +visitor.getTime());
							
							summarizedVisitorList.add(summeriVisitor);
						}							
					}								
				}
				if (!sameVisitor) {
					// Different visitor, create new visitor object in the container 
					SummerizedVisitorContainer summeriVisitorNew = new SummerizedVisitorContainer();
					summeriVisitorNew.setTagId(visitor.getTagId());
					summeriVisitorNew.setLayout(visitor.getLayout());
					summeriVisitorNew.setName(visitor.getName());
					summeriVisitorNew.setNodeName(visitor.getLocation());
					summeriVisitorNew.setStartTime(visitor.getDate()+ " " +visitor.getTime());
						
					summarizedVisitorContainerList.add(summeriVisitorNew);					
				}
			}
		}				
		return summarizedVisitorList;
	}
	
	public LinkedHashMap<Integer, ArrayList<SummerizedVisitor>> createTagIDEventMap(ArrayList<SummerizedVisitor> summerizedVisitorList) {
		
		LinkedHashMap<Integer, ArrayList<SummerizedVisitor>> tagIDEventMap = new LinkedHashMap<Integer, ArrayList<SummerizedVisitor>>();
		// Start the event number from '1' for each event
		int eventNo = 1;
		
		for (SummerizedVisitor visitorSummeri : summerizedVisitorList) {
			
			if (tagIDEventMap.containsKey(visitorSummeri.getTagId())) {  
				int previousEventNo = tagIDEventMap.get(visitorSummeri.getTagId()).get(tagIDEventMap.get(visitorSummeri.getTagId()).size()-1).getEventNo();
				visitorSummeri.setEventNo(previousEventNo + 1);
				tagIDEventMap.get(visitorSummeri.getTagId()).add(visitorSummeri);
					
			} else {
				ArrayList<SummerizedVisitor> eventListNew = new ArrayList<SummerizedVisitor>();
				visitorSummeri.setEventNo(eventNo);
				eventListNew.add(visitorSummeri);
				tagIDEventMap.put(visitorSummeri.getTagId(), eventListNew);				
			}
		}				
		return tagIDEventMap;		
	}
	
	public static long getElapsedTimeMillisecs(java.util.Date start, java.util.Date end) {

		long long1 = start.getTime();
		long long2 = end.getTime();
		long diff = long2 - long1;
		
		return diff;
	}

	public String getElapsedTimeStandard(java.util.Date start, java.util.Date end) {

		long long1 = start.getTime();
		long long2 = end.getTime();
		long diff = long2 - long1;

		long secondInMillis = 1000;
		long minuteInMillis = secondInMillis * 60;
		long hourInMillis = minuteInMillis * 60;
		long dayInMillis = hourInMillis * 24;
		long yearInMillis = dayInMillis * 365;

		long elapsedYears = diff / yearInMillis;
		diff = diff % yearInMillis;
		long elapsedDays = diff / dayInMillis;
		diff = diff % dayInMillis;
		long elapsedHours = diff / hourInMillis;
		diff = diff % hourInMillis;
		long elapsedMinutes = diff / minuteInMillis;
		diff = diff % minuteInMillis;
		long elapsedSeconds = diff / secondInMillis;

		return (elapsedDays+ "/" +elapsedYears+ " - " +elapsedHours + ":" + 
				elapsedMinutes + ":" + elapsedSeconds).toString();
	}

	public String longToStandardTime(Long timeInMills) {

		long timeInMillis = timeInMills;

		long secondInMillis = 1000;
		long minuteInMillis = secondInMillis * 60;
		long hourInMillis = minuteInMillis * 60;
		long dayInMillis = hourInMillis * 24;
		long yearInMillis = dayInMillis * 365;

		long elapsedYears = timeInMillis / yearInMillis;
		timeInMillis = timeInMillis % yearInMillis;
		long elapsedDays = timeInMillis / dayInMillis;
		timeInMillis = timeInMillis % dayInMillis;
		long elapsedHours = timeInMillis / hourInMillis;
		timeInMillis = timeInMillis % hourInMillis;
		long elapsedMinutes = timeInMillis / minuteInMillis;
		timeInMillis = timeInMillis % minuteInMillis;
		long elapsedSeconds = timeInMillis / secondInMillis;

		return (elapsedYears+ "/" +elapsedDays+ " - " +elapsedHours + ":" + 
				elapsedMinutes + ":" + elapsedSeconds).toString();
	}
		
	public static java.util.Date stringToDate(String strDate) {

		java.util.Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date = (java.util.Date) dateFormat.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public String stringToRFC822(String strDate) {
		
		java.util.Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date = (java.util.Date) dateFormat.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		String newDateFormat = "MM d yyyy HH:mm:ss Z";
		SimpleDateFormat sdf = new SimpleDateFormat(newDateFormat);

		return sdf.format(date).toString();
	}
	
	public static java.sql.Timestamp strToData2(String strDate) {
		java.util.Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date = (java.util.Date) dateFormat.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Timestamp(date.getTime());
	}

	public String dateToString(java.util.Date date) {
		String dateFormat = "dd-MM-yyyy HH:mm:ss";
		// Create object of SimpleDateFormat and pass the desired date format
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date).toString();

	}
}
