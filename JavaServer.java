import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaServer {

    private static final int MAX_THREADS = 50; // Adjust based on your needs

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java JavaServer <port number>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                final Socket clientSocket = serverSocket.accept(); // Accept the client connection
                executor.execute(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            String requestLine = in.readLine();

            if (requestLine != null && !requestLine.isEmpty()) {
                System.out.println("Request: " + requestLine);
                String[] requestParts = requestLine.split(" ");
                if (requestParts.length >= 2 && requestParts[0].equals("GET")) {
                    serveFile(out, requestParts[1]);
                }
            }
        }
    }

    private static void serveFile(OutputStream out, String path) throws IOException {
        if (path.equals("/")) {
            listFiles(out);
            return;
        }

        Path filePath = Paths.get("." + path);
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] fileBytes = Files.readAllBytes(filePath);
            sendResponse(out, "200 OK", Files.probeContentType(filePath), fileBytes);
        } else {
            sendResponse(out, "404 Not Found", "text/plain", "File not found".getBytes());
        }
    }

    private static void listFiles(OutputStream out) throws IOException {
        File curDir = new File(".");
        File[] filesList = curDir.listFiles();
        StringBuilder response = new StringBuilder("<html><body><ul>");

        for (File f : filesList) {
            if (f.isDirectory())
                response.append("<li>Directory: ").append(f.getName()).append("</li>");
            else if (f.isFile()) {
                response.append("<li>File: <a href='/").append(f.getName()).append("'>").append(f.getName()).append("</a></li>");
            }
        }
        response.append("</ul></body></html>");
        sendResponse(out, "200 OK", "text/html", response.toString().getBytes());
    }

    private static void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        out.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + content.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.flush();
    }
}

