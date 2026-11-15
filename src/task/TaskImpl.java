package task;

public abstract class TaskImpl implements Task {
    protected String input = "";
    protected String result = "";
    protected String executedTime = "";

    @Override
    public String getInput() { return input; }

    @Override
    public void setInput(String input) { this.input = input; }

    @Override
    public void setExecutedTime(String duration) { this.executedTime = duration; }

    @Override
    public String getResult() {
        return "Result: " + result + "; Executed in: " + executedTime + "\n";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " -> Params: " + input + "; " + getResult();
    }
}
