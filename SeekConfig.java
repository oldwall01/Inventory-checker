package com.brick.seek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SeekConfig implements Comparable<SeekConfig>{

	public String zipCode = "";
	
	public String itemID = "";
	
//	protected String upc = "";

	public static String parseMode = "";
	
	public static String CONFIG_FILE_NAME = "conf_OD.txt";

	@Override
	public boolean equals(Object ob){
		SeekConfig config = (SeekConfig) ob;
		return zipCode.equals(config.zipCode) && itemID.equals(config.itemID);
	}

	@Override
	public int hashCode(){
		return zipCode.hashCode()+ itemID.hashCode();
	}

	@Override
	public int compareTo(SeekConfig cfg){
		return (zipCode+itemID).compareTo(cfg.itemID+cfg.zipCode);
	}


	public static List<SeekConfig> parseConfigs() {
		List<SeekConfig> configs = new ArrayList<SeekConfig>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME));

			String zipLine = br.readLine();
			String itemIDLine = br.readLine();
			//String upcLine = br.readLine();

			if (zipLine.isEmpty()) {
				System.err.println("Please put the query input in the first line!");
				return null;
			}

			String[] zips = zipLine.trim().substring(4).split(",");
			String[] ids = itemIDLine.trim().substring(4).split(",");
			//String[] upcs = upcLine.trim().substring(4).split(",");


			if (ids.length == 1) {
				parseMode = "zip";
				int i = 0;
				while (i < zips.length) {
					SeekConfig cfg = new SeekConfig();
					cfg.zipCode = zips[i].trim();
					cfg.itemID = ids[0].trim();
					configs.add(cfg);
					i++;
				}
			} else {

				parseMode = "itemID";
				int i = 0;
				while (i < ids.length) {
					SeekConfig cfg = new SeekConfig();
					cfg.zipCode = zips[0].trim();
					cfg.itemID = ids[i].trim();
					configs.add(cfg);
					i++;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashSet<SeekConfig> set = new HashSet<SeekConfig>(configs);
		List<SeekConfig> list = new ArrayList<SeekConfig>(set);
		return list;
	}
}
