package server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import util.TokenBlackList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server running on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        System.out.println("📡 Një klient u lidh!");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Username:");
            String username = in.readLine();

            out.println("Password:");
            String password = in.readLine();

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            if (UserService.authenticate(username, password)) {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                String token = JWT.create()
                        .withSubject(username)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000))
                        .sign(algorithm);

                out.println("Authentication successful! JWT: " + token);
                System.out.println("JWT sent to client: " + token);

                out.println("Type 'logout' to log out or 'exit' to quit.");
                String command = in.readLine();

                if ("logout".equalsIgnoreCase(command)) {
                    TokenBlackList.add(token);
                    out.println("You have been logged out.");
                    System.out.println("User " + username + " u log out.");
                } else {
                    out.println("Session ended without logout.");
                    System.out.println("User " + username + " closed session pa logout.");
                }

            } else {
                out.println("Authentication failed.");
                System.out.println("Authentication failed for: " + username);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

