import handler.RequestsHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/** Accepts HTTP connections on a raw ServerSocket and delegates to 4 workers. */
public class WebServerSocket {
    private static final int PORT = 8000;
    private static final int WORKERS = 4;

    public static void main(String[] args) throws IOException {
        final ExecutorService pool = Executors.newFixedThreadPool(WORKERS);
        final ServerSocket server = new ServerSocket(PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { server.close(); } catch (IOException ignored) { }
            pool.shutdownNow();
            try { pool.awaitTermination(5, TimeUnit.SECONDS); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }));

        System.out.println("WebServerSocket listening on port " + PORT + " with " + WORKERS + " workers");
        try {
            while (!server.isClosed()) {
                Socket client = server.accept();
                try {
                    pool.execute(new RequestsHandlerSocket(client));
                } catch (RejectedExecutionException e) {
                    client.close();
                }
            }
        } catch (IOException e) {
            if (!server.isClosed()) throw e;
        } finally {
            pool.shutdown();
        }
    }
}
