
    import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkOldTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void OneJoinNetwork() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		assertNotNull(me);
		assertEquals("Hakan", me.getUserName());
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}

	
	@Test 
	public void TwoJoinNetwork() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void SendFriendRequest() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}
	
	@Test 
	public void AcceptFriendRequest() {
		SocialNetwork sn = new SocialNetwork();
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("Mary", john);
		sn.acceptFriendshipFrom("John", mary);
		assertTrue(mary.hasFriend("John"));
		assertTrue(john.hasFriend("Mary"));
	}

	@Test
	public void FindNonExistingUser() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertFalse(her.hasFriend("John"));
	}

	@Test
	public void SendFriendRequestToNonExistingUser() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("John", me);
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}
	
	@Test
	public void AcceptFriendRequestFromNonExistingUser() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("John", her);
		assertFalse(her.hasFriend("John"));
	}

	@Test
	public void ListEmptyNetwork() {
		SocialNetwork sn = new SocialNetwork();
		Collection<String> members = sn.listMembers();
		assertEquals(0, members.size());
	}

	@Test 
	public void getOutgoingRequests() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void AcceptAllFriendship() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		Account another = sn.join("Serra");
		sn.sendFriendshipTo("Cecile", me);
		sn.sendFriendshipTo("Cecile", another);
		sn.acceptAllFriendshipsTo(her);
		assertTrue(her.hasFriend("Hakan"));
		assertTrue(her.hasFriend("Serra"));
	}

	@Test 
	public void rejectFriendship() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(her.getIncomingRequests().contains("Hakan"));
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test 
	public void rejectAllFriendships() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		Account another = sn.join("Serra");
		sn.sendFriendshipTo("Cecile", me);
		sn.sendFriendshipTo("Cecile", another);
		sn.rejectAllFriendshipsTo(her);
		assertFalse(her.getIncomingRequests().contains("Hakan"));
		assertFalse(her.getIncomingRequests().contains("Serra"));
	}

	@Test 
	public void cancelFriendship() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.sendFriendshipCancellationTo("Cecile", me);
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test 
	public void leavingNetwork() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.leave(me);
		assertFalse(her.hasFriend("Hakan"));
		assertFalse(sn.listMembers().contains("Hakan"));
	}

	@Test 
	public void autoAcceptFriendship() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.autoAcceptFriendshipsTo(me);
		sn.sendFriendshipTo("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}

	@Test 
	public void leavingWhileReceivingFriendRequests() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Hakan", her);
		sn.leave(me);
		assertFalse(her.getOutgoingRequests().contains("Hakan"));
	}

	@Test 
	public void leavingWhileGivingFriendRequests() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.leave(me);
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}

	@Test
	public void requestingNullFriendship() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		me.requestFriendship(null);
		assertTrue(me.getIncomingRequests().isEmpty());
	}

	@Test
	public void joinWithExistingName() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		Account duplicate = sn.join("Hakan");
		assertNull(duplicate);
	}

	@Test
	public void joinWithNullUserName() {
		SocialNetwork sn = new SocialNetwork();
		assertNull(sn.join(null));
	}

	@Test
	public void joiniWithEmptyUserName() {
		SocialNetwork sn = new SocialNetwork();
		assertNull(sn.join(""));
	}

	@Test
	public void noAcceptWithoutRequest() {
		SocialNetwork sn = new SocialNetwork();
		Account hakan = sn.join("Hakan");
		Account serra = sn.join("Serra");
		sn.acceptFriendshipFrom("Serra", hakan);
		assertFalse(hakan.getFriends().contains("Serra"));
		assertFalse(serra.getFriends().contains("Hakan"));
	}

}

