package DataBaseFiles;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class SQLDatabaseEngine extends DatabaseEngine {
		public String giverecommendation(String userid) throws Exception{
		
		String re = "";
		try {
			Connection connection = getConnection();
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT (DISTINCT userid) FROM dishtable");
			int user_num = 0;
			if(rs.next())
				user_num= rs.getInt(1);
			ResultSet rs1 = stmt.executeQuery("SELECT COUNT (DISTINCT dishname) FROM dishtable");
			int dish_num =0;
			if(rs.next())
				dish_num = rs1.getInt(1);
			
			String[] user = new String[user_num];
			ResultSet rs2 = stmt.executeQuery("SELECT DISTINCT userid FROM dishtable ORDER BY userid");
			int count = 0;
			while(rs2.next()) {
				user[count] = rs2.getString("userid");
				count++;
			}
			
			String[] dish = new String[dish_num];
			ResultSet rs3 = stmt.executeQuery("SELECT DISTINCT dishname FROM dishtable ORDER BY dishname");
			count = 0;
			while(rs3.next()) {
				dish[count] = rs3.getString("dishname");
				count++;
			}
			
			//construct table
	    	    int[][] counttable = new int[user_num][dish_num];
	    		for (int j=0; j<user_num; j++) {
					for (int k=0; k<dish_num; k++) {
						ResultSet rss = stmt.executeQuery("SELECT COUNT (*) FROM dishtable WHERE userid='"+user[j]+"' AND dishname='"+dish[k]+"'" );
						//counttable[j][k] = repo.countByUserIDAndDishName(user[j],dish[k]);
					}
			}
	    		//calculate standard deviation
	    		int[] sd = new int[user_num];
	    		int user_index=0;
	    		for (int j=0; j<user_num; j++) {
	    			if (user[j].equals(userid)) {
	    				user_index = j;
	    			}
	    		}
	    		for (int j=0; j<user_num; j++) {
	    			sd[j] = 0;
	    			for (int k=0; k<dish_num; k++) {
	    				sd[j] = sd[j] + (counttable[user_index][k]-counttable[j][k])*(counttable[user_index][k]-counttable[j][k]);
	    			}
	    		}
	    		//find min sd
	    		int min_sd = 1000000;
	    		int min_index = 0;
	    		for (int j=0; j<user_num; j++) {
	    			if(j!=user_index) {
	    				if(sd[j]<min_sd) {
	    					min_sd = sd[j];
	    					min_index = j;
	    				}
	    			}
	    		}
	    		//System.out.println(min_sd);
	    		//give recommendation
	    		int[] frequency = new int[dish_num];
	    		for (int j=0; j<dish_num; j++) {
	    			frequency[j] = counttable[min_index][j];
	    			if(counttable[user_index][j]>0) {
	    				frequency[j] = 0;
	    			}
	    		}
	    		
	    		int max_freq = frequency[0];
			int max_index = 0;
			for (int j=0; j<dish_num; j++) {
				if(frequency[j]>max_freq) {
					max_freq = frequency[j];
					max_index = j;
				}
			}
	        if (max_freq == 0) {
	        		re = "We may need more data to give you recommendation";
	        }
	        else {
	        		re = "We recommend you to eat " + dish[max_index] + ". Our another user who have similar eating habit with you also loves this.";
	        }
	       	return re;
			
			
		}catch (Exception e) {
			throw new Exception("NOT FOUND");
		}
	}
	
	@Override
	String search(String text) throws Exception {
		//Write your code here
		Connection connection = this.getConnection();
		
		String result = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT response FROM CHAT WHERE keyword = ?");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
			rs.close();
			stmt.close();
			connection.close();
			/*
			isr = new InputStreamReader(
                    this.getClass().getResourceAsStream(FILENAME));
			br = new BufferedReader(isr);
			String sCurrentLine;
			
			while (result == null && (sCurrentLine = br.readLine()) != null) {
				String[] parts = sCurrentLine.split(":");
				if (text.toLowerCase().equals(parts[0].toLowerCase())) {
					result = parts[1];
				} 
			}
			*/
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
//				log.info("IOException while closing file: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
		
	private Connection getConnection() throws URISyntaxException, SQLException {
		
		Connection connection;

//		URI dbUri = new URI("postgres://gevhlqslflxoyi:f1a55c58e48e04773beb8551d5eafb251459133b057bdfad3f5bc61cb8337819@ec2-54-235-88-58.compute-1.amazonaws.com:5432/dere2fsq7t4fl0");
//							 
//		String username = dbUri.getUserInfo().split(":")[0];
//		String password = dbUri.getUserInfo().split(":")[1];
//		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
//				+ "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

//		URI dbUri = new URI("postgres://@");
		 
//		String username = "gevhlqslflxoyi";
//		String password = "f1a55c58e48e04773beb8551d5eafb251459133b057bdfad3f5bc61cb8337819";
//		String dbUrl = "jdbc:postgresql://ec2-54-235-88-58.compute-1.amazonaws.com:5432/dere2fsq7t4fl0?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		
		
		String username = "gevhlqslflxoyi";
		String password = "f1a55c58e48e04773beb8551d5eafb251459133b057bdfad3f5bc61cb8337819";
		String dbUrl = "jdbc:postgresql://ec2-54-235-88-58.compute-1.amazonaws.com:5432/dere2fsq7t4fl0?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		
		
//		String username = "gevhlqslflxoyi";
//		String password = "f1a55c58e48e04773beb8551d5eafb251459133b057bdfad3f5bc61cb8337819";
//		String dbUrl = "jdbc:postgresql://localhost:5432/chatbotDB";
		
//		String username = "djrcekbdoygkzb";
//		String password = "fe791340e0b2bdc8fbc34f5565942fd2a061297bdd1f300f2257a0e2d37d11d9";
//		String dbUrl = "jdbc:postgresql://ec2-54-163-233-201.compute-1.amazonaws.com:5432/d7nia57h74gn04?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
				
//		log.info("Username: {} Password: {}", username, password);
//		log.info("dbUrl: {}", dbUrl);

		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

	public void recordWeight(String userId, double weight) throws Exception {
		
		Connection connection = this.getConnection();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			String sql = "INSERT INTO WeightHistory VALUES(?,?,?,?,?,?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setDouble(2, weight);
			Calendar now = Calendar.getInstance();
			int day = now.get(Calendar.DAY_OF_MONTH);
			int month = now.get(Calendar.MONTH);
			int year = now.get(Calendar.YEAR);
			int dayofyear = now.get(Calendar.DAY_OF_YEAR);
			stmt.setInt(3, day);
			stmt.setInt(4, month);
			stmt.setInt(5, year);
			stmt.setInt(6, dayofyear);
			
			stmt.executeUpdate();

			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
//				log.info("IOException while closing file: {}", ex.toString());
			}
		}
	}


	public void recordCalories(String userID, double calories) throws Exception {
	
		Connection connection = this.getConnection();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			String sql = "INSERT INTO CaloriesHistory VALUES(?,?,?,?,?,?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, userID);
			stmt.setDouble(2, calories);
			Calendar now = Calendar.getInstance();
			int day = now.get(Calendar.DAY_OF_MONTH);
			int month = now.get(Calendar.MONTH);
			int year = now.get(Calendar.YEAR);
			int dayofyear = now.get(Calendar.DAY_OF_YEAR);
			stmt.setInt(3, day);
			stmt.setInt(4, month);
			stmt.setInt(5, year);
			stmt.setInt(6, dayofyear);
			stmt.executeQuery();
	
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
//				log.info("IOException while closing file: {}", ex.toString());
			}
		}
}


	public double[] getWeightFromDB(String userID) throws Exception {
		
		double[] weightHistory =new double[] {0,0,0,0,0,0,0};
		
		int lastupdate = 0;
	
		Connection connection = this.getConnection();
		
		String result = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT userID,weight,day,month,year,dayofyear FROM WeightHistory WHERE userID = ?");
			
			stmt.setString(1,userID);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(lastupdate == 0) {
					lastupdate = rs.getInt(6);
					weightHistory[6] = rs.getDouble(2);
				}
				
				else {
					int diff = rs.getInt(6) - lastupdate;
					if(diff<=6 && diff >0) {
						for(int i =diff;i<7;i++) {
							weightHistory[i-diff] = weightHistory[i];
							weightHistory[i] = 0;
						}
						
						weightHistory[6] = rs.getDouble(2);
						lastupdate = rs.getInt(6);
					}
					
					else if (diff == 0) {
						weightHistory[6] = rs.getDouble(2);
					}
					
					else if (diff > 7) {
						for(int i = 0; i<7;i++) {
							weightHistory[i] = 0;
						}
						weightHistory[6] = rs.getDouble(2);
						lastupdate = rs.getInt(6);
					}
					
					else {}
				}
			}
			Calendar now = Calendar.getInstance();
			
			int today = now.get(Calendar.DAY_OF_YEAR);
			int timeshift = today - lastupdate;
			
			for(int i = timeshift; i<7;i++) {
				weightHistory[i-timeshift] = weightHistory[i];
				weightHistory[i] = 0;
			}
			
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	
		return weightHistory;	
}


	public double[] getCaloriesFromDB(String userID) throws Exception {
		double[] calHistory =new double[] {0,0,0,0,0,0,0};
	
		int lastupdate = 0;

		Connection connection = this.getConnection();
	
		String result = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT userID,calories,day,month,year,dayofyear FROM CaloriesHistory WHERE userID = ?");
			
			stmt.setString(1,userID);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(lastupdate == 0) {
					lastupdate = rs.getInt(6);
					calHistory[6] = rs.getDouble(2);
				}
				
				else {
					int diff = rs.getInt(6) - lastupdate;
					if(diff<=6 && diff >0) {
						for(int i =diff;i<7;i++) {
							calHistory[i-diff] = calHistory[i];
							calHistory[i] = 0;
						}
						
						calHistory[6] = calHistory[6] + rs.getDouble(2);
						lastupdate = rs.getInt(6);
					}
					
					else if (diff == 0) {
						calHistory[6] = calHistory[6] + rs.getDouble(2);
					}
					
					else if (diff > 7) {
						for(int i = 0; i<7;i++) {
							calHistory[i] = 0;
						}
						calHistory[6] = rs.getDouble(2);
						lastupdate = rs.getInt(6);
					}
					
					else {}
				}
			}
			Calendar now = Calendar.getInstance();
			
			int today = now.get(Calendar.DAY_OF_YEAR);
			int timeshift = today - lastupdate;
			
			for(int i = timeshift; i<7;i++) {
				calHistory[i-timeshift] = calHistory[i];
				calHistory[i] = 0;
			}
			
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return calHistory;
	
}


	public double[] calculateDishNutrition(String[] ingredients) throws Exception {
	// the first element is cal
	// the second element is sodium
	// the thrid element is fat
	
		Connection connection = this.getConnection();
		
		String result = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		double[] nutrition = new double[] {0,0,0};
		
		try {
			int size =  ingredients.length;
			
			for(int i=0;i<size;i++) {
				PreparedStatement stmt = connection.prepareStatement(
						"SELECT name,calories,sodium,fat FROM Ingredienttable WHERE name = ?");
				
				stmt.setString(1, ingredients[i]);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					nutrition[0] = nutrition[0] + rs.getDouble(2);
					nutrition[1] = nutrition[1] + rs.getDouble(3);
					nutrition[2] = nutrition[2] + rs.getDouble(4);
				}
				rs.close();
				stmt.close();
			}
			 	connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return nutrition;

	}

}