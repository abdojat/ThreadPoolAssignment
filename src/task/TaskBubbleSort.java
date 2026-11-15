package task;

import java.util.Arrays;

/** Existing task preserved: sort comma-separated integers. */
public class TaskBubbleSort extends TaskImpl {
    @Override
    public void execute() {
        try {
            String[] parts = input.split(",");
            int[] data = new int[parts.length];
            for (int i = 0; i < parts.length; i++) data[i] = Integer.parseInt(parts[i].trim());
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j + 1 < data.length - i; j++) {
                    if (data[j] > data[j + 1]) {
                        int temporary = data[j];
                        data[j] = data[j + 1];
                        data[j + 1] = temporary;
                    }
                }
            }
            result = Arrays.toString(data);
        } catch (NumberFormatException e) {
            result = "Invalid input: expected comma-separated integers";
        }
    }
}
