package com.brick.seek;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.ms.http.client.util.HttpClientUtils;
import org.apache.http.conn.ConnectTimeoutException;

import static com.ms.http.client.util.HttpClientUtils.*;

public class WebClient {

	public static String HOST = "http://www.officedepot.com";
	
	public static String PATH = "/mb/stores/availability.do?";
	
	protected SeekConfig opt;

	public WebClient(SeekConfig opt) {
		this.opt = opt;
	}
	
	public String execute() {
		String s="";
		StringBuilder sb = new StringBuilder();
		sb.append(HOST);
		sb.append(PATH);
		sb.append("sku=").append(opt.itemID);
		sb.append("&");
		sb.append("zip=").append(opt.zipCode);

		try {
			URI uri = new URI(sb.toString());
			s = executeHttpGet(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
//	public static void main(String args[]) {
//		WebClient wc = new WebClient("98052", "056-10-0645", null);
//		wc.execute();
//	}
}
