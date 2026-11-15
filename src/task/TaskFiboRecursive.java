package task;

/** Existing task preserved: recursively calculate a Fibonacci value. */
public class TaskFiboRecursive extends TaskImpl {
    @Override
    public void execute() {
        try {
            int n = Integer.parseInt(input.trim());
            if (n < 0 || n > 40) {
                result = "Please provide N between 0 and 40";
                return;
            }
            result = Long.toString(fib(n));
        } catch (NumberFormatException e) {
            result = "Invalid input: expected an integer";
        }
    }

    private long fib(int n) {
        if (Thread.currentThread().isInterrupted()) return 0;
        if (n < 2) return n;
        return fib(n - 1) + fib(n - 2);
    }
}
