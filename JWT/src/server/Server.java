package server;

import util.JWTUtil;
import util.TokenBlackList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

                String token = JWTUtil.generateToken(username); // Përdorim JWTUtil për RSA
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

