package handler;

import singleton.TaskExecutor;
import singleton.TasksList;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.SocketTimeoutException;

/** A Runnable: one accepted Socket is handled by one ThreadPool worker. */
public class RequestsHandlerSocket implements Runnable {
    private final Socket socket;
    private static final int MAX_BODY = 16384;

    public RequestsHandlerSocket(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        try (Socket connection = socket) {
            connection.setSoTimeout(20000);
            InputStream input = connection.getInputStream();
            OutputStream output = connection.getOutputStream();
            String requestLine = readLine(input);
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                respond(output, 400, "Bad Request", "Invalid request line\n");
                return;
            }

            int length = 0;
            String line;
            while ((line = readLine(input)) != null && !line.isEmpty()) {
                int colon = line.indexOf(':');
                if (colon > 0 && line.substring(0, colon).trim().equalsIgnoreCase("Content-Length")) {
                    try {
                        length = Integer.parseInt(line.substring(colon + 1).trim());
                    } catch (NumberFormatException e) {
                        respond(output, 400, "Bad Request", "Invalid Content-Length\n");
                        return;
                    }
                }
            }

            if (length < 0 || length > MAX_BODY) {
                respond(output, 413, "Payload Too Large", "Request body too large\n");
                return;
            }

            if ("GET".equals(parts[0])) {
                respond(output, 200, "OK", TasksList.INSTANCE.list());
            } else if ("POST".equals(parts[0])) {
                byte[] body = readBody(input, length);
                if (body.length != length) {
                    respond(output, 400, "Bad Request", "Incomplete request body\n");
                    return;
                }
                String parameters = new String(body, StandardCharsets.UTF_8);
                respond(output, 200, "OK", TaskExecutor.INSTANCE.run(parameters));
            } else {
                respond(output, 405, "Method Not Allowed", "Only GET and POST are supported\n");
            }
        } catch (SocketTimeoutException e) {
            System.err.println("[TIMEOUT] Request timed out on " + Thread.currentThread().getName());
        } catch (IOException e) {
            System.err.println("[CONNECTION] " + e.getMessage());
        }
    }

    private static String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        int current;
        while ((current = in.read()) != -1) {
            if (current == '\n') break;
            if (current != '\r') line.write(current);
            if (line.size() > 8192) throw new IOException("HTTP header line too long");
        }
        if (current == -1 && line.size() == 0) return null;
        return new String(line.toByteArray(), StandardCharsets.US_ASCII);
    }

    private static byte[] readBody(InputStream in, int length) throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int remaining = length;
        while (remaining > 0) {
            int count = in.read(buffer, 0, Math.min(buffer.length, remaining));
            if (count == -1) break;
            body.write(buffer, 0, count);
            remaining -= count;
        }
        return body.toByteArray();
    }

    private static void respond(OutputStream out, int code, String status, String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        String headers = "HTTP/1.1 " + code + " " + status + "\r\n" +
                         "Content-Type: text/plain; charset=utf-8\r\n" +
                         "Content-Length: " + bytes.length + "\r\n" +
                         "Connection: close\r\n\r\n";
        out.write(headers.getBytes(StandardCharsets.US_ASCII));
        out.write(bytes);
        out.flush();
    }
}
