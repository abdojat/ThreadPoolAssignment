package task;

public class TaskIncorrectParams extends TaskImpl {
    @Override
    public void execute() { result = "ERROR: format must be TaskName&input"; }
}
