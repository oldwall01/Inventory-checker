package com.brick.seek;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

public class ItemInStock implements Comparable<ItemInStock> {

	protected String storeName;
	
	public String address;

	public String phone;
	
	public String distance;
	
	public String inStock;
	
	public String saleableQty;

	public String sku="";

	public String prodName="";


	public String getStoreName(){
		return storeName;
	}

	public String getAdddress(){
		return address;
	}

	// new codes for dereplicate using HashSet.
    @Override
	public boolean equals(Object ob){
		ItemInStock iis = (ItemInStock) ob;
		return address.equals(iis.address) && sku.equals(iis.sku);
	}

	@Override
	public int hashCode(){

		return address.hashCode() + sku.hashCode();
	}

	@Override
	public int compareTo(ItemInStock iis){
		return address.compareTo(iis.getStoreName());
	}

	//end of dereplicate using HashSet.

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("storeName=" + storeName + "\n");
		sb.append("address=" + address + "\n");
		sb.append("phone=" + phone + "\n");
		sb.append("distance=" + distance + "\n");
		sb.append("inStock=" + inStock + "\n");
		sb.append("saleableQty=" + saleableQty + "\n");
		
		return sb.toString();
	}
	
	public String getCSVString() {
		StringBuilder sb = new StringBuilder();
		sb.append(storeName + ",");
		sb.append("\"" + address + "\",");
		sb.append(phone + ",");
		sb.append(distance + ",");
		sb.append(inStock + ",");
		sb.append(saleableQty + ",");
		sb.append(",");
		sb.append("\""+prodName+ "\",");

		return sb.toString();
	}

	public void setOnlineID(String s){
		sku = s;
	}
	public static String getCSVTitle() {
		return "storeName,address,phone,distance,inStock,saleableQty";
	}
	
	public static void dumpCSVFile(List<ItemInStock> items, String outPath) {
		try {
			PrintWriter out = new PrintWriter(new File(outPath));
			out.println(getCSVTitle());
			
			for(ItemInStock itm : items) {
				out.println(itm.getCSVString());
			}
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
