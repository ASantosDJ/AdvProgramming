import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChatServerTest {

	@Test
	void testSelectCoordinator() {
		System.out.println("Not yet implemented");
	
	}
	@Test 
	void testFindCoordinator() {
		int count = 0; 
		String name = "Anabella";
    	String serverCoordinator = null;
    	boolean haveCoordinator = false;
    	boolean expectedResult = false;
    	if (count == 1) {
        	System.out.println("[SERVER] You have been selected to be the coordinator!");
        	serverCoordinator = name;
        	haveCoordinator = true;
    		System.out.println(serverCoordinator+" "+haveCoordinator);
    		assertEquals(expectedResult, haveCoordinator);
    	}
    	if (count >= 2 && haveCoordinator == true) {
			System.out.println("A new member has connected: " + name);
    		System.out.println(haveCoordinator);
    		assertEquals(expectedResult, haveCoordinator);
    	} 
    	if (count > 1 && haveCoordinator == false) {
    		System.out.println("[SERVER] Unable to make contact with coordinator, selecting new coordinator!");
        	System.out.println(haveCoordinator);
        	System.out.println("Selecting new server coordinator"); //Select new coordinator
        	assertEquals(expectedResult, haveCoordinator);
    	} else if (count == 0) {
    		System.out.println(haveCoordinator);
    		System.out.println("No users to give the role to.");
			haveCoordinator = false;
			serverCoordinator = null;
			assertEquals(expectedResult, haveCoordinator);
    	} else {
    		System.out.println("Error");
    		return;
		}
	}
}


