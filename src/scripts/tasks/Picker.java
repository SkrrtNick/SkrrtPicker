package scripts.tasks;

import scripts.skrrt_api.task.Priority;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.util.functions.Logging;

public class Picker implements Task {


    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void execute() {
        Logging.message("Test");
    }
}
