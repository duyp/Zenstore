package com.zenstore.order;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.text.format.DateFormat;

public class DataTools {

	public static final String DATE_FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
	public static final String DATA_DIR = "/zenstore/";
	public static final String DATA_FILE_NAME_ORDER = "orders";
	public static final String DATA_FILE_NAME_PRODUCT = "products";
	public static final String DATA_FILE_NAME_CUSTOMER = "customers";
	
	public static Date String2Date(String input) throws java.text.ParseException{ 
		return new SimpleDateFormat(DATE_FORMAT).parse(input);
	}
	
	public static String getPriceFormat(String input) {
		try {
			String price = NumberFormat.getCurrencyInstance(new Locale("EN"))
					.format(Integer.valueOf(input));
			price = price.substring(1, price.length()-3);
			price += "₫";
		return price;
		} catch (Exception e) {
			e.printStackTrace();
			return "unkown";
		}
	}
	
	public static String Date2String(Date input) {
		return DateFormat.format(DATE_FORMAT, input).toString();
	}
	
	public static void saveData(int code, String jsonData) {
		FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists())
            {
                dir.mkdirs(); 
            }

            final File myFile = new File(dir, DATA_FILE_NAME_ORDER + String.valueOf(code) + ".txt");

            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
            } 

            fos = new FileOutputStream(myFile);

            fos.write(jsonData.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public static String loadData(int code) {
		try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists()) {
                return null;
            }

            final File myFile = new File(dir, DATA_FILE_NAME_ORDER + String.valueOf(code) + ".txt");
            if (!myFile.exists()){    
                return null;
            } 
            
            StringBuilder data = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            
            String line;
            while ((line = br.readLine()) != null) {
            	data.append(line);
            	data.append('\n');
            }
            br.close();
            
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static String loadProducts() {
		try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists()) {
                return null;
            }

            final File myFile = new File(dir, DATA_FILE_NAME_PRODUCT + ".txt");
            if (!myFile.exists()){    
                return null;
            } 
            
            StringBuilder data = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            
            String line;
            while ((line = br.readLine()) != null) {
            	data.append(line);
            	data.append('\n');
            }
            br.close();
            
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static boolean saveProduct(String jsonData) {
		FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists())
            {
                dir.mkdirs(); 
            }

            final File myFile = new File(dir, DATA_FILE_NAME_PRODUCT + ".txt");

            String current = "";
            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
            } else {
            	current = loadProducts();
            	if (current != null && !current.equals("")) {
            		jsonData = JSONParser.insertJsonData(current, jsonData);
            	}
            }
            
            fos = new FileOutputStream(myFile);

            fos.write(jsonData.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
	}
	
	///////////////////////// CUSTOMER ///////////////////////////////////
	
	public static String loadCustomers() {
		try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists()) {
                return null;
            }

            final File myFile = new File(dir, DATA_FILE_NAME_CUSTOMER + ".txt");
            if (!myFile.exists()){    
                return null;
            } 
            
            StringBuilder data = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            
            String line;
            while ((line = br.readLine()) != null) {
            	data.append(line);
            	data.append('\n');
            }
            br.close();
            
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static boolean saveCustomer(String jsonData) {
		FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DATA_DIR );

            if (!dir.exists())
            {
                dir.mkdirs(); 
            }

            final File myFile = new File(dir, DATA_FILE_NAME_CUSTOMER + ".txt");

            String current = "";
            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
            } else {
            	current = loadCustomers();
            	if (current != null && !current.equals("")) {
            		jsonData = JSONParser.insertJsonData(current, jsonData);
            	}
            }
            
            fos = new FileOutputStream(myFile);

            fos.write(jsonData.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
	}
}
