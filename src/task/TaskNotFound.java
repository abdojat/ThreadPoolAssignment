package task;

public class TaskNotFound extends TaskImpl {
    @Override
    public void execute() { result = "ERROR: task does not exist"; }
}
