package scripts.tasks;

import scripts.data.Vars;
import scripts.skrrt_api.task.Priority;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.util.functions.*;

import static scripts.data.Vars.initItemCount;

public class Banking implements Task {

    @Override
    public String toString(){
        return "Banking items";
    }

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return Vars.initialCheck && Inventory07.isFull();
    }

    @Override
    public void execute() {
        if (Inventory07.isFull() && !Banking07.isInBank()) {
            Logging.message("Walker", "Traversing to Closest Bank");
            Traversing.walkToBank();
        }
        if (Inventory07.isFull() && Banking07.isInBank()) {
            if(Banking07.openBank()){
                Sleep.until(Banking07::isBankLoaded);
                if(Banking07.depositAll()>0){
                    initItemCount = 0;
                    Sleep.until(Inventory07::isEmpty);
                }
            }
        }
        //TODO add stamina handling here
        if(Inventory07.isEmpty()){
            Banking07.close();
        }

    }
}
