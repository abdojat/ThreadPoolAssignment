package task;

/** New CPU-bound task: count prime numbers from 2 through N (inclusive). */
public class TaskPrimeCounter extends TaskImpl {
    @Override
    public void execute() {
        try {
            int limit = Integer.parseInt(input.trim());
            if (limit < 2 || limit > 20000000) {
                result = "Please provide N between 2 and 20000000";
                return;
            }

            int count = 0;
            for (int n = 2; n <= limit; n++) {
                // React to interruption during emergency shutdown.
                if ((n & 1023) == 0 && Thread.currentThread().isInterrupted()) {
                    result = "Cancelled";
                    return;
                }
                if (isPrime(n)) count++;
            }
            result = "Prime numbers between 2 and " + limit + ": " + count;
        } catch (NumberFormatException e) {
            result = "Invalid input: expected an integer";
        }
    }

    private boolean isPrime(int number) {
        if (number < 2) return false;
        for (int divisor = 2; divisor <= number / divisor; divisor++) {
            if (number % divisor == 0) return false;
        }
        return true;
    }
}
