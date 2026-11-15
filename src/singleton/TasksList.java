package singleton;

import task.Task;
import java.util.concurrent.ConcurrentLinkedQueue;

/** Thread-safe task history, shared by all request worker threads. */
public enum TasksList {
    INSTANCE;

    private final ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<>();

    public void add(Task task) { tasks.add(task); }

    public String list() {
        StringBuilder builder = new StringBuilder("Completed tasks:\n");
        for (Task task : tasks) builder.append(task.toString());
        if (tasks.isEmpty()) builder.append("(none yet)\n");
        return builder.toString();
    }
}
