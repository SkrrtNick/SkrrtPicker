package scripts.api.util.functions;

import org.tribot.api2007.types.RSItem;

import java.util.ArrayList;


public class Inventory07 extends org.tribot.api2007.Inventory {

    public static boolean isEmpty() {
        return Inventory07.getAll().length == 0;
    }

    public static RSItem[] find(ArrayList<Integer> ids){
        for(Integer id:ids){
            if(Inventory07.find(id).length > 0){
                return Inventory07.find(id);
            }
        } return new RSItem[0];
    }
}
