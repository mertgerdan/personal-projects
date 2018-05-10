package hisarai.plagiarism.detector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.sql.*;

import com.microsoft.sqlserver.jdbc.*;  
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.file.*;

@SuppressWarnings("unused")
public class SQL {
	
	public static class StatRow 
	{
		//FILE_ID WORDS_PER_SENTENC UNIQUE_WORD_COUNT UW_COMPLEXITY IS_PLAGIARIZED
		public int FILE_ID;
		public double WORDS_PER_SENTENC;
		public double UNIQUE_WORD_COUNT;
		public double UW_COMPLEXITY;
		public boolean IS_PLAGIARIZED;
		public int PAR_ID;
		
		public StatRow(int fileId, double wps, double uwc, double wcomp, boolean isplag, int parId)
		{
			FILE_ID = fileId;
			WORDS_PER_SENTENC = wps;
			UNIQUE_WORD_COUNT = uwc;
			UW_COMPLEXITY = wcomp;
			IS_PLAGIARIZED = isplag;
			PAR_ID = parId;
		}
	}
	
	public static StatRow resultSetToStatRow(ResultSet rs)
	{
		StatRow sr = null;
		try {
			sr = new StatRow(rs.getInt(rs.findColumn("FILE_ID")),
							 rs.getFloat(rs.findColumn("WORDS_PER_SENTENC")),
						     rs.getFloat(rs.findColumn("UNIQUE_WORD_COUNT")),
						     rs.getFloat(rs.findColumn("UW_COMPLEXITY")),
						     rs.getBoolean(rs.findColumn("IS_PLAGIARIZED")),
						     rs.getInt(rs.findColumn("PAR_ID")));
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sr;
	}
	
	public static void insertStatRow(Connection connection, StatRow sr)
	{
		PreparedStatement prepsInsertProduct = null;
		String insertSql = "INSERT INTO dbo.FILE_STATS (FILE_ID, WORDS_PER_SENTENCE, UNIQUE_WORD_COUNT, UW_COMPLEXITY, IS_PLAGIARIZED, PAR_ID) VALUES "  
                + "("+sr.FILE_ID+", "+sr.WORDS_PER_SENTENC+", "+sr.UNIQUE_WORD_COUNT+", "+sr.UW_COMPLEXITY+", "+(sr.IS_PLAGIARIZED ? "1":"0")+", "+sr.PAR_ID+");";  
		try {
		    prepsInsertProduct = connection.prepareStatement(  
	            insertSql,  
	            Statement.RETURN_GENERATED_KEYS);  
	        prepsInsertProduct.execute();   
			
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	}
}
