package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class utilities {
	
	
	static List<String> meters = new ArrayList<>(); 
	
	public static String meterNum;
	
	public static String result;
	public static int successfulVend;
	public static int unsuccessfulVend;
	public static Scanner scan = new Scanner(System.in);
	
	public static void addMetersToList() {
		
		
		while(true){
			
			System.out.print("Please enter meter you would like to add: ");			
			meterNum = scan.nextLine();
			
			if(meterNum.trim().isEmpty() || meterNum.trim().equalsIgnoreCase("e")) {
				System.out.println("Moving to vending for meters");
				break;
			}
			meters.add(meterNum);
		}
	}
	
	public static void loopThroughMeters() {
		
		for(String meter: meters) {
			vendToken(meter);
		}
	}
		
	public static String  vendToken(String meterNum) {
		
		if(meterNum.matches("\\d{11}") && meterNum.startsWith("3100")) {			
			System.out.println("Token vended for: "+ meterNum  );
			successfulVend++;
			return "Success";
		}else {
			System.out.println("Invalid meter: "+ meterNum  );
			unsuccessfulVend++;
			return "Fail";
		}
	}
	
	
	public static void main(String[]args) {
		
		addMetersToList();
		loopThroughMeters();
		System.out.println("Successful Vend: "+ successfulVend);
		System.out.println("Unsuccessful Vend: "+ unsuccessfulVend);
	}


}
