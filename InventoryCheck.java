package com.brick.seek.test;

import java.util.*;
import java.lang.Runnable;

import com.brick.seek.SeekConfig;
import com.brick.seek.ItemInStock;
import com.brick.seek.ResultParser;
import com.brick.seek.WebClient;
import com.brick.seek.QuantityCompare;

// previous version without multiple threading
public class InventoryCheck implements Runnable{
	protected List<ItemInStock> qryResults = new ArrayList<ItemInStock>();
	public static List<SeekConfig> configs = SeekConfig.parseConfigs();
	public int numOfThread;
	public int part;

	public InventoryCheck(int n, int p){
		numOfThread = n;
		part = p;
	}

	public InventoryCheck(){
		numOfThread = 1;
		part = 0;
	}

	public List<ItemInStock> getQryResults(){
		return qryResults;
	}

	@Override
	public void run(){
		if(part <configs.size()){
			for(int i=part; i<configs.size(); i = i + numOfThread){
				query(configs.get(i));
			}
		}
	}

	public void query(SeekConfig config){
		WebClient wc = new WebClient(config);
		String s = wc.execute();
		//System.out.print(s);
		ResultParser rp = new ResultParser();
		List<ItemInStock> items = rp.parse(s,SeekConfig.parseMode);
		for(ItemInStock iis : items){
			iis.setOnlineID(config.itemID);
		}
		qryResults.addAll(items);
	}



	public static void main(String args[]) {

		int numOfThread = 2;
		InventoryCheck test[] = new InventoryCheck[numOfThread];
		for(int i = 0; i<numOfThread; i++){
			test[i] = new InventoryCheck(numOfThread, i);
		}

		Thread t[] = new Thread[numOfThread];
		for(int i=0; i<numOfThread; i++){
			t[i] = new Thread(test[i]);
			t[i].start();
		}

		for (Thread thread : t) {
			try {
				thread.join();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}

		List<ItemInStock> qrs = new ArrayList<ItemInStock>();
		for(int i = 0; i<numOfThread; i++){
			qrs.addAll(test[i].getQryResults());
		}
		QuantityCompare quantityCompare = new QuantityCompare(SeekConfig.parseMode);
		if(quantityCompare.mode.equals("zip")) {
			HashSet<ItemInStock> qrySet = new HashSet<ItemInStock>(qrs);
			List<ItemInStock> results = new ArrayList<ItemInStock>(qrySet);

			Collections.sort(results, quantityCompare);
			Collections.reverse(results);

			ItemInStock.dumpCSVFile(results, "res_OD.csv");
		}
		else {
			HashSet<ItemInStock> qrySet = new HashSet<ItemInStock>(qrs);
			List<ItemInStock> results = new ArrayList<ItemInStock>(qrySet);

			Collections.sort(results, quantityCompare);

			ItemInStock.dumpCSVFile(results, "res_OD.csv");
		}
	}
}
