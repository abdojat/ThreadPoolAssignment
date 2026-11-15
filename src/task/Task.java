package task;

public interface Task {
    void setInput(String input);
    String getInput();
    void execute();
    void setExecutedTime(String duration);
    String getResult();
}
