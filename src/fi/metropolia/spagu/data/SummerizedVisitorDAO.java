package fi.metropolia.spagu.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fi.metropolia.spagu.business.BusinessLogic;
import fi.metropolia.spagu.business.LogParser;

public class SummerizedVisitorDAO extends DataAccessObject {

	public static void main(String[] args) {

		insertData(BusinessLogic.summarizeVisitorList(LogParser.parseLineCreateVisitor(LogParser.FILE)));

	}
	
	private static void insertData(ArrayList<SummerizedVisitor> summarizedVisitorsList) {

		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "insert into  eventDataJuly (tagID, museumName, placeName, nodeName, startTime, " +
					"endTime) values (?, ?, ?, ?, ?, ?)";
			
			statement = connection.prepareStatement(sql);
			System.out.println("Started ...");
			for (SummerizedVisitor sumVisitor : summarizedVisitorsList) {
				statement.setInt(1, sumVisitor.getTagId());
				statement.setString(2, sumVisitor.getMuseumName());
				statement.setString(3, sumVisitor.getPlaceName());
				statement.setString(4, sumVisitor.getNodeName());
				statement.setTimestamp(5,sumVisitor.getStartTime());
				statement.setTimestamp(6, sumVisitor.getEndTime());
								
				statement.executeUpdate();
			}						
			statement.close();
			System.out.println("Done!");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement, connection);
		}
	}
	


}
