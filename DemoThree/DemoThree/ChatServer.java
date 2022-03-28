import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * A multithreaded chat room server. When a client connects the server requests a screen
 * name by sending the client the text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received. After a client submits a unique name, the server acknowledges
 * with "NAMEACCEPTED". Then all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name. The broadcast messages are prefixed
 * with "MESSAGE".
 *
 * This is just a teaching example so it can be enhanced in many ways, e.g., better
 * logging. Another is to accept a lot of fun commands, like Slack.
 */
public class ChatServer {
	static int count;
	//static int port;
	private static int port;
	static InetAddress serverIp;
	public static boolean haveCoordinator;
    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();

     // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();
    
    static Set<String> getUsers() {
    	return ChatServer.names;
    }

    public static void main(String[] args) throws Exception {
    	//ChatServer serverCoordinator = new ChatServer();
    	
        System.out.println("The chat server is running...");
        System.out.println("Current number of users: " + count);
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
                //int port = ServerSocket.getLocalPort();
                count++;
                port = listener.getLocalPort();
                System.out.println("Server Port: " + port);
                System.out.println("Server IP: " + serverIp);
                //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //input = in.readLine();
                //System.out.println("Client just connected " + input + " to server " + serverIp);
            }
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        Object serverCoordinator;
        

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a screen name until a
         * unique one has been submitted, then acknowledges the name and registers the
         * output stream for the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                

                // Keep requesting a name until we get a unique one.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!name.isEmpty() && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the socket's print writer
                // to the set of all writers so this client can receive broadcast messages.
                // But BEFORE THAT, let everyone else know that the new person has joined!
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                    writer.println("MESSAGE " + "[SERVER] " + name + " has joined");
                    writer.println("MESSAGE " +"[SERVER] " + "Number of members in server: " + count);
                  
                }
                writers.add(out);
                
                printUsers();
                findCoordinator(serverCoordinator);
                
                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    else if (input.toLowerCase().startsWith("/msg")) {
                	   String args[]= input.split(",");
                	   if (args.length == 3) {
                		   String targetUser = args[1];
                		   String message = args[2]; 
                	   for (PrintWriter writer: writers) {
                		   writer.println("MESSAGE " + "[SERVER] " +"Private message to " + targetUser + " : " + message);
                		   }
                	   }
                   } else 
	                   for (PrintWriter writer : writers) {
	                	   writer.println("MESSAGE " + name + ": " + input);
	                   }
                }
             } catch (Exception e) {
            	System.out.println(e);
             } finally {
            	if (out != null) {
            		writers.remove(out);
            	}
            	if (name != null) {
            		System.out.println(name + " is leaving");
                    --count;
                    System.out.println("Number of members in server: " + count);
                    names.remove(name);
                    findCoordinator(serverCoordinator);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    	writer.println("MESSAGE " + "[SERVER] " + name + " has left");
                    	writer.println("MESSAGE "+"[SERVER] Number of members in server: " + count);
                    	printUsers();
                    }
                }
                try { socket.close(); } catch (IOException e) {}                
            }
        }
            void printUsers() {
            	if (count >= 1) {
	            	for (PrintWriter writer : writers) {
	            		writer.println("MESSAGE " + "[SERVER] Updated list of connected users: " + ChatServer.getUsers());
	            	}
            	} else if (count == 0) {
            		System.out.print("No connected users");
            	} else {
            		System.out.print("Error!");
            	}
            }
            
            void findCoordinator(Object serverCoordinator) {
            	if (count == 1) {
	            	serverCoordinator = name;
	            	haveCoordinator = true;
	            	for (PrintWriter writer : writers) {
	            		writer.println("MESSAGE " + "[SERVER] You have been selected to be the coordinator!");
	            	}
            		System.out.println(serverCoordinator+" "+haveCoordinator);
            	}
            	if (count >= 2 && haveCoordinator == true) {
            	//	System.out.println("A new member has connected: " + name);
            		System.out.println(haveCoordinator);
            	} 
            	if (count > 1 && haveCoordinator == false) {
	            	for (PrintWriter writer : writers) {
	            		writer.println("MESSAGE " + "[SERVER] Unable to make contact with coordinator, selecting new coordinator!");
	            		System.out.println(haveCoordinator);
	            		}
	            	selectCoordinator(serverCoordinator); //Select new coordinator
            	} else if (count == 0) {
            		System.out.println(haveCoordinator);
            		System.out.println("No users to give the role to.");
        			haveCoordinator = false;
        			serverCoordinator = null;
            	} else if (serverCoordinator == null || out == null) {
            			haveCoordinator = false;
            			serverCoordinator = null;
            	}
            }
		        
       
            	/*while (true) {
	            	if (serverCoordinator == null) {
	            		haveCoordinator = false;
	            		serverCoordinator = null;
	            	} else if (out == null) {
	            		haveCoordinator = false;
	            		serverCoordinator = null;
	            	}
            	} */
            }

             static void selectCoordinator(Object serverCoordinator) {
            	List<String> names = new ArrayList<String>(ChatServer.getUsers());
            	for (PrintWriter writer : writers) {
            		writer.println ("MESSAGE " + " [SERVER] Randomly selecting a new coordinator...");
            	}
            	Random rand = new Random();
            	String randomName = names.get(rand.nextInt(names.size()));
            	for (PrintWriter writer : writers) {
            		writer.println("MESSAGE "  + "[SERVER]"+ randomName + " has been selected as the new coordinator!");
            	}
            	serverCoordinator = randomName;

             }
             
         }


/*
 * Coordinator role was completely not working - no messages were getting printed - fixed by commenting out the while loop
 * Coordinator role only would switch if there was 2 users connected to the server - fixed by changing the if statement
 * Coordinator role automatically changes if there is 3 users connected to the server - no fix atm
 */
