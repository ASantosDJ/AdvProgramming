public class Coordinator {
    private String serverCoordinator;
    
    //Constructor
    public Coordinator (String serverCoordinator) { 
    	this.serverCoordinator = serverCoordinator;
    }
    //Assigns serverCoordinator	
    	public void setServerCoordinator(String serverCoordinator) {
    		this.serverCoordinator = serverCoordinator;
    	}
    //Returns serverCoordinator
    	public String getServerCoordinator() {
    		return serverCoordinator;
    	
    }
}
