package scripts.api.util.functions;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;

import java.util.ArrayList;

public class Banking07 extends Banking {

    public static boolean closeBankTutorial() {
        if (Banking07.isBankLoaded() && Interfaces.isInterfaceSubstantiated(664, 29)) {
            Logging.debug("Closing Bank Tutorial");
            RSInterface close = Interfaces.get(664, 29);
            if (close != null) {
                if(close.click()){
                    Sleep.until(()->!Interfaces.isInterfaceSubstantiated(664));
                }
            }
        }
        return !Interfaces.isInterfaceSubstantiated(664, 29);
    }
    public static RSItem[] find(ArrayList<Integer> ids){
        for(Integer id:ids){
            if(Banking.find(id).length > 0){
                return Banking.find(id);
            }
        } return new RSItem[0];
    }

}
