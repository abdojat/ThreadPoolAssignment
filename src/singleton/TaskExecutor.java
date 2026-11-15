package singleton;

import task.*;

import java.util.Locale;

/** Turns HTTP POST form TaskName&input into a runnable application task. */
public enum TaskExecutor {
    INSTANCE;

    public String run(String parameters) {
        String[] arguments = parameters.trim().split("&", 2);
        String name = arguments.length > 0 ? arguments[0].trim() : "";
        String value = arguments.length == 2 ? arguments[1].trim() : "";
        Task task = arguments.length == 2 ? createTask(name) : new TaskIncorrectParams();
        task.setInput(value);

        String worker = Thread.currentThread().getName();
        long begin = System.nanoTime();
        System.out.printf("[START] thread=%s task=%s input=%s%n", worker, name, value);
        try {
            task.execute();
        } catch (RuntimeException e) {
            System.err.printf("[ERROR] thread=%s task=%s reason=%s%n", worker, name, e);
            return "ERROR: " + e.getMessage() + "\n";
        } finally {
            double seconds = (System.nanoTime() - begin) / 1_000_000_000.0;
            task.setExecutedTime(String.format(Locale.US, "%.3fs", seconds));
            TasksList.INSTANCE.add(task);
            System.out.printf("[END]   thread=%s task=%s elapsed=%.3fs%n", worker, name, seconds);
        }
        return task.getResult();
    }

    private Task createTask(String name) {
        switch (name) {
            case "TaskPrimeCounter": return new TaskPrimeCounter();
            case "TaskFiboRecursive": return new TaskFiboRecursive();
            case "TaskBubbleSort": return new TaskBubbleSort();
            case "TaskBitcoin": return new TaskBitcoin();
            default: return new TaskNotFound();
        }
    }
}
