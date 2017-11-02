package testttttt;

import DataBaseFiles.*;


public class testRecommendation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SQLDatabaseEngine engine = new SQLDatabaseEngine();
		String st;
		try {
			st = engine.giverecommendation("0001");
			System.out.println(st);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
