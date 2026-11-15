package task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/** Existing network task preserved (requires Internet access). */
public class TaskBitcoin extends TaskImpl {
    @Override
    public void execute() {
        try {
            int requests = Integer.parseInt(input.trim());
            if (requests < 1 || requests > 5) {
                result = "Please provide a count between 1 and 5";
                return;
            }
            StringBuilder values = new StringBuilder();
            for (int i = 0; i < requests; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    result = "Cancelled";
                    return;
                }
                HttpURLConnection conn = (HttpURLConnection) new URL(
                    "https://www.bitstamp.net/api/v2/ticker/btcusd/").openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line = in.readLine();
                    if (values.length() != 0) values.append(" | ");
                    values.append(line);
                } finally {
                    conn.disconnect();
                }
            }
            result = values.toString();
        } catch (Exception e) {
            result = "Bitcoin request failed: " + e.getClass().getSimpleName();
        }
    }
}
