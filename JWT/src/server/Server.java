package server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import util.TokenBlacklist;

public class Server {
    public Server() {
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            try {
                System.out.println("Server running on port 8080...");

                while(true) {
                    Socket clientSocket = serverSocket.accept();
                    (new Thread(() -> {
                        handleClient(clientSocket);
                    })).start();
                }
            } catch (Throwable var5) {
                try {
                    serverSocket.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }
        } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        System.out.println("\ud83d\udce1 Një klient u lidh!");

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                try {
                    out.println("Username:");
                    String username = in.readLine();
                    out.println("Password:");
                    String password = in.readLine();
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + password);
                    if (UserService.authenticate(username, password)) {
                        Algorithm algorithm = Algorithm.HMAC256("secret");
                        String token = JWT.create().withSubject(username).withIssuedAt(new Date()).withExpiresAt(new Date(System.currentTimeMillis() + 3600000L)).sign(algorithm);
                        out.println("Authentication successful! JWT: " + token);
                        System.out.println("JWT sent to client: " + token);
                        out.println("Type 'logout' to log out or 'exit' to quit.");
                        String command = in.readLine();
                        if ("logout".equalsIgnoreCase(command)) {
                            TokenBlacklist.add(token);
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
                } catch (Throwable var10) {
                    try {
                        out.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }

                    throw var10;
                }

                out.close();
            } catch (Throwable var11) {
                try {
                    in.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }

                throw var11;
            }

            in.close();
        } catch (IOException var12) {
            IOException e = var12;
            e.printStackTrace();
        }

    }
}

