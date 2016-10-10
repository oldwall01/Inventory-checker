package com.brick.seek;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.ms.http.client.util.HttpClientUtils;
import com.ms.http.parse.NodePredicate;

public class ResultParser {

   /* private static String IN_STOCK_LABEL = "In Stock:";

    private static String SALEABLE_QTY_LABEL = "Saleable Qty:";

    private static String ON_HAND_QTY_LABEL = "On Hand Qty:";

    private static String EST_PRICE_LABEL = "Est Price:";*/

    private NodePredicate itmTrPred = new NodePredicate() {

        @Override
        public boolean pass(Node node) {
            if (node.nodeName().equalsIgnoreCase("li")
                    && node.hasAttr("class")
                    && node.attr("class").equals("line_bottom vpadding_top_bottom section")
                    && node.childNodeSize() == 3
                    )
                return true;
            else
                return false;
        }
    };

    private NodePredicate itmDivPred = new NodePredicate() {
        @Override
        public boolean pass(Node node) {
            if (node.nodeName().equalsIgnoreCase("span")
                    && node.hasAttr("class")
                    && node.attr("class").equalsIgnoreCase("skuDescription")
                   // && node.parentNode().nodeName().equalsIgnoreCase("div")
            )
                return true;
            else
                return false;
        }
    };

    public List<ItemInStock> parse(String s, String mode) {
        List<ItemInStock> items = new ArrayList<ItemInStock>();

        Document root = Jsoup.parse(s, WebClient.HOST);
        List<Node> liNodes = HttpClientUtils.findNodes(root, itmTrPred);
        List<Node> divNodes = HttpClientUtils.findNodes(root, itmDivPred);

        for (Node li : liNodes) {
            ItemInStock itm = new ItemInStock();

            Node nameNode = li.childNode(1).childNode(1).childNode(1);
            Node nameTypeNode = li.childNode(1).childNode(1).childNode(1).childNode(0);

            String qtyDiv = com.ms.http.parse.util.Utils.getNodeLabel(li.childNode(1).childNode(3).childNode(1).childNode(3).childNode(1).childNode(0)).trim();
            StringTokenizer st = new StringTokenizer(qtyDiv," ");

            String token = st.nextToken().trim();

            if (token.equalsIgnoreCase("out")) {
                if (mode.equals("itemID")) {
                    continue;
                }
                else {
                    itm.inStock = "Out Of Stock";
                    itm.saleableQty = "0";
                }
            } else if (token.equalsIgnoreCase("in")) {
                itm.inStock = "In Stock";
                itm.saleableQty = "4 or more";
            } else if (token.equalsIgnoreCase("call")) {
                itm.inStock = "Call Store";
                itm.saleableQty = "NA";
            } else if(token.indexOf("3") > -1 || token.indexOf("2") > -1){
                itm.inStock = token.substring(0, 1) + " In Stock";
                itm.saleableQty = token.substring(0, 1) + " or less";
            }
            else {
                itm.inStock = token.substring(0, 1) + " In Stock";
                itm.saleableQty = token.substring(0, 1);
            }

            String nameDiv= nameNode.attr("onclick").trim();
            String nameTypeDiv = nameTypeNode.attr("class");
            String storeType="OfficeDepot";
            String storeCode="";

            int idxStoreCode = nameDiv.indexOf("storeCode=");
            int idxLatitude = nameDiv.indexOf("latitude=");
           // int idxStoreCount = nameTypeDiv.indexOf("black_text_storeCount");
            if(nameTypeDiv.indexOf("store_type_omax")>-1)
                storeType = "OfficeMax";
            storeCode = nameDiv.substring(idxStoreCode + 10,idxLatitude-1 ).trim();
            itm.storeName = storeType + " " + storeCode;

            itm.address = com.ms.http.parse.util.Utils.getNodeLabel(li.childNode(1).childNode(3).childNode(2)).trim()+
                    " " +
                    com.ms.http.parse.util.Utils.getNodeLabel(li.childNode(1).childNode(3).childNode(4)).trim();

            itm.phone = com.ms.http.parse.util.Utils.getNodeLabel(li.childNode(1).childNode(3).childNode(5).childNode(1)).trim();

            itm.distance = com.ms.http.parse.util.Utils.getNodeLabel(li.childNode(1).childNode(3).childNode(1).childNode(0)).trim();



            if (divNodes.size() > 0) {
                String strin = com.ms.http.parse.util.Utils.getNodeLabel(divNodes.get(0).childNode(0)).trim();
                if (strin.length() > 60) {
                    strin = strin.substring(0, 60).trim();
                }
                int idxQuote = strin.indexOf("\"");
                while(idxQuote > -1){
                    strin = strin.substring(0, idxQuote) + strin.substring(idxQuote+1);
                    idxQuote = strin.indexOf("\"");
                }
                itm.prodName = strin;
            } else
                itm.prodName = "NA";


            items.add(itm);

        }

        return items;
    }

//	public static void main(String args[]) {
//		ResultParser rp = new ResultParser();
//		rp.parse("tmp.html");
//	}
}
