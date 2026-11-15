import com.sun.net.httpserver.HttpServer;
import handler.RequestsHandlerHttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Optional alternative to the raw Socket server (do not run both at once). */
public class WebServerHttp {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        server.createContext("/", new RequestsHandlerHttp());
        server.setExecutor(pool);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
            pool.shutdownNow();
        }));
        server.start();
        System.out.println("WebServerHttp listening on port 8000 with 4 workers");
    }
}
