package main.java.pageObjects;

public interface SearchElements {

	//input
	public static String inputSearch = "//textarea[@title='Buscar']";
	
	//button
	public static String buttonSearch = "//div[@class='lJ9FBc']//input[@aria-label='Buscar con Google']";
	public static String buttonLuckySearch = "//div[@class='FPdoLc lJ9FBc']//input[@aria-label='Voy a tener suerte']";
	
	//label
	public static String labelCountry = "//div[@class='uU7dJb']";
	
	//img
	public static String imgGoogle = "//img[@class='lnXdpd']";
}
