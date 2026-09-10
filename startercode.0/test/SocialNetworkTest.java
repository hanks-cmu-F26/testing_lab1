import static org.junit.Assert.*;

import java.beans.Transient;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me, her, another;

	// these are some example tests: you can merge them with your own tests from A0 
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void canJoinSocialNetwork() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		Set<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		sn.join("Cecile");
		Set<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sendAndAcceptFriendRequestToBecomeFriends() {
		// test sending friend request
	    sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
	
	@Test
	public void canLoginAfterJoining() {
		sn = new SocialNetwork();
		Account account = sn.join("Hakan");
		Account loggedIn = sn.login(account);
		assertNotNull(loggedIn);
		assertEquals("Hakan", loggedIn.getUserName());
	}

	@Test
	public void loginReturnsNullForNullAccount() {
		sn = new SocialNetwork();
		Account loggedIn = sn.login(null);
		assertNull(loggedIn);
	}

	@Test
	public void cannotLoginWithAccountNotInNetwork() {
		sn = new SocialNetwork();
		Account account = new Account("Hakan");
		Account loggedIn = sn.login(account);
		assertNull(loggedIn);
	}

	@Test
	public void canSwitchAccountsWithoutLoggingOut() {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		
		// Login as Hakan
		Account first = sn.login(me);
		assertNotNull(first);
		assertEquals("Hakan", first.getUserName());
		
		// Switch to Cecile without logging out
		Account second = sn.login(her);
		assertNotNull(second);
		assertEquals("Cecile", second.getUserName());
	}

	@Test
	public void loginMultipleTimes() {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		
		// Login first time
		Account first = sn.login(me);
		assertNotNull(first);
		
		// Login same account again
		Account second = sn.login(me);
		assertNotNull(second);
		assertEquals("Hakan", second.getUserName());
	}

}
