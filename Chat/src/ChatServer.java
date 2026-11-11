import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int DEFAULT_PORT = 5000;

    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default " + DEFAULT_PORT);
            }
        }

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("chat server started on port " + port + " waiting for clients...");
            System.out.println("Type a message to send to clients...");

            Thread console = new Thread(() -> {
                try(BufferedReader key = new BufferedReader(new InputStreamReader(System.in))) {
                    String line;
                    while ((line = key.readLine()) != null) {
                        if(!line.trim().isEmpty()) {
                            broadcast("server: " + line, null);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("console input error: " + e.getMessage());
                }
            });
            console.setDaemon(true);
            console.start();

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("new connection from " + socket.getRemoteSocketAddress());

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("server error: " + e.getMessage());
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        for(ClientHandler client : clients) {
            if(client != sender) {
                client.send(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String name = "Unknown";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        void send(String message) {
            if(out != null) {
                out.println(message);
            }
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                send("Enter your name: ");
                String input = in.readLine();
                if(input == null || input.trim().isEmpty()) {
                    name = "User" + socket.getPort();
                } else {
                    name = input.trim();
                }

                send("Welcome, " + name + "! type /quit to leave");
                System.out.println(name + " joined the chat");
                broadcast("***" + name + " joined the chat ***", this);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("/quit")) {
                        break;
                    }

                    if (message.trim().isEmpty()) {
                        continue;
                    }

                    System.out.println(name + ": " + message);
                    broadcast(name + ": " + message, this);
                }
            } catch (IOException e) {
                System.out.println("Connection error with: " + name + ": " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            clients.remove(this);
            try {
                socket.close();
            } catch (IOException ignored) {
                System.out.println(name + "left the chat");
                broadcast("***" + name + " left the chat ***", this);
            }
        }
    }
}
