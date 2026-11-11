import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = 5000;
        if(args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default 5000");
            }
        }

        try(Socket socket = new Socket(host, port);
            BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader key = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chat server at " + host + ":" + port);

            Thread receiver = new Thread(() -> {
                try {
                    String line;
                    while ((line = bin.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {}
                System.out.println("Disconnected from chat server");
                System.exit(0);
            });

            receiver.setDaemon(true);
            receiver.start();

            String input;
            while ((input = key.readLine()) != null) {
                sout.println(input);
                if(input.equalsIgnoreCase("/quit")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
        } catch (IOException e) {
            System.err.println("could not connect: " + e.getMessage());
        }

    }
}
