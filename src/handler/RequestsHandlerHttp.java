package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import singleton.TaskExecutor;
import singleton.TasksList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** Alternative server implementation using the JDK HttpServer. */
public class RequestsHandlerHttp implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        int code = 200;
        if ("GET".equals(exchange.getRequestMethod())) {
            response = TasksList.INSTANCE.list();
        } else if ("POST".equals(exchange.getRequestMethod())) {
            byte[] body;
            try (InputStream input = exchange.getRequestBody()) {
                body = readLimited(input, 16384);
            } catch (IOException e) {
                response = "Request body too large\n";
                code = 413;
                write(exchange, code, response);
                return;
            }
            response = TaskExecutor.INSTANCE.run(new String(body, StandardCharsets.UTF_8));
        } else {
            code = 405;
            response = "Only GET and POST are supported\n";
        }
        write(exchange, code, response);
    }

    private byte[] readLimited(InputStream in, int maximum) throws IOException {
        java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            if (result.size() + n > maximum) throw new IOException("Too large");
            result.write(buffer, 0, n);
        }
        return result.toByteArray();
    }

    private void write(HttpExchange exchange, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(bytes); }
    }
}
