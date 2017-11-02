package DataBaseFiles;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;

public class User {
	private String userID;
	private double[] calories;
	private double[] weight;

	public User(String userid) {
		this.userID = userid;
		this.calories = new double[] { 0, 0, 0, 0, 0, 0, 0 };
		this.weight = new double[] { 0, 0, 0, 0, 0, 0, 0 };
	}

	public String getID() {
		return this.userID;
	}

	public double[] getCalories() {
		return this.calories;
	}

	public double[] getWeight() {
		return this.weight;
	}

	public User update() {
		try {
			SQLDatabaseEngine engine = new SQLDatabaseEngine();
			this.calories = engine.getCaloriesFromDB(this.userID);
			this.weight = engine.getWeightFromDB(this.userID);
		} catch (Exception e) {

		}
		return this;
	}
}