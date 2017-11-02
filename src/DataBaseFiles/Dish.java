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

public class Dish {
	private String name;
	private String[] ingredients;
	
	private int day;
	private int month;
	private int year ;
	private int dayofyear;
	private double calories;
	private double sodium;
	private double fat;
	
	public Dish(String name, String[] ingredients) {
		this.name = name;
		this.ingredients = ingredients;
		
		this.day = 0;
		this.month = 0;
		this.year = 0;
		this.calories = 0;
		this.sodium = 0;
		this.fat = 0;
		this.dayofyear = 0;
		calculateNutritionFactors();
	}
	
	public String getName() {
		return name;
	}
	
	public double getCalories() {
		return calories;
	}
	
	public double getSodium() {
		return sodium;
	}
	
	public double getFat() {
		return fat;
	}
	
	public double getDay() {
		return day;
	}
	
	public double getMonth() {
		return month;
	}
	
	public double getYear() {
		return year;
	}
	
	public double getDayofyear() {
		return dayofyear;
	}
	public void updateDate() {
		Calendar now = Calendar.getInstance();
		
		this.day = now.get(Calendar.DAY_OF_MONTH);
		this.month = now.get(Calendar.MONTH);
		this.year = now.get(Calendar.YEAR);
		this.dayofyear = now.get(Calendar.DAY_OF_YEAR);
	}
	
	public String[] getIngredients() {
		return ingredients;
	}
	
	public void calculateNutritionFactors() {
		try {
		SQLDatabaseEngine engine = new SQLDatabaseEngine();
		double[] temp= engine.calculateDishNutrition(ingredients);
		calories = temp[0];
		sodium = temp[1];
		fat = temp[2];
		} catch (Exception e) {
			
		}
	}
}


