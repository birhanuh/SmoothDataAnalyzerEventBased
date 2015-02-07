package fi.metropolia.spagu.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;

import fi.metropolia.spagu.business.BusinessLogic;
import fi.metropolia.spagu.business.LogParser;

public class CouchDB {

	private static String urlStr = "spagu.metropolia.fi/couchdb/";
	private static Session dbSession = new Session(urlStr, 80, "admin", "admin");
	private static String dbName = "weegee-visit-spring-2012";

	private static LogParser logParser = new LogParser();
	private static BusinessLogic businessLogic = new BusinessLogic();
	
	public static void main(String[] args) {
	
		try {
			// Create new Document in by passing ArrayList of SummerizeVisitor type 
			createDocument(businessLogic.createTagIDEventMap(BusinessLogic.summarizeVisitorList(logParser.parseLineCreateVisitor(LogParser.FILE))));
			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	public static void createDocument(LinkedHashMap<Integer, ArrayList<SummerizedVisitor>> linkedHashMap) throws IOException {
		
		Database db = dbSession.getDatabase(dbName);
		// If database name is not null, create a new document 
		if (db != null) {
			
			@SuppressWarnings("rawtypes")
			Iterator it = linkedHashMap.entrySet().iterator();
			
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				
				Document newDoc = new Document();
			
				newDoc.setId(pairs.getKey().toString());
				newDoc.put("type", "events");
				newDoc.put("Event", pairs.getValue());
			
				try {
					db.deleteDocument(db.getDocument(pairs.getKey().toString()));
				} catch (Exception e) {
					System.out.println("Didnt exists: " + pairs.getKey().toString());
				}
				db.saveDocument(newDoc);
				// Avoids a ConcurrentModificationException 
				it.remove();
			}			
		}
		System.out.println("Done !");
	}	

	public static void createDesignDocument() {
		Database db = dbSession.getDatabase(dbName);
		// If not exist, create a new document
		if (db != null) {
			Document doc = new Document();
			doc.setId("_design/couchview");

			String str = "{\"TagIDA500\": {\"map\": \"function(doc) { if (doc.TagID == 'A500')  " +
					"emit(null, doc) } \"}}";

			doc.put("views", str);
			try {
				db.saveDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	

	
}