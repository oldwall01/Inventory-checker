package com.brick.seek;

import java.util.Comparator;

import com.brick.seek.ItemInStock;


/**
 * Created by wawa-3147 on 10/22/2015.
 */
public class QuantityCompare implements Comparator<ItemInStock> {
    public String mode="";

    public  QuantityCompare(String m) {
        mode = m;
    }

    public int compare(ItemInStock ob1, ItemInStock ob2){
        if(mode.equals("zip")){
            return compare1(ob1, ob2);
        }
        else if (mode.equals("itemID")) {
            return compare2(ob1, ob2);
        }
        else
            return ob1.storeName.compareTo(ob2.storeName);
    }

    public int compare1(ItemInStock ob1, ItemInStock ob2){
        if(!ob1.saleableQty.equals(ob2.saleableQty))
            return ob1.saleableQty.compareTo(ob2.saleableQty);
        else
            return ob1.phone.compareTo(ob2.phone);
    }

    public int compare2(ItemInStock ob1, ItemInStock ob2){
        if(!ob1.phone.equals(ob2.phone))
            return ob1.phone.compareTo(ob2.phone);
        else
            return ob2.saleableQty.compareTo(ob1.saleableQty);
    }

}
