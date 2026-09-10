import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

	// the one member currently logged in to this SocialNetwork instance
	private Account loggedInUser = null;

	private void requireLoggedIn() throws NoUserLoggedInException {
		if (loggedInUser == null) throw new NoUserLoggedInException();
	}

	// join SN with a new user name
	public Account join(String userName) {
		if (userName == null || userName.isEmpty()) return null;
		if (findAccountForUserName(userName) != null) return null;
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		return newAccount;
	}

	// find a member by user name 
	private Account findAccountForUserName(String userName) {
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) 
					return each;
		}
		return null;
	}
	
	// list user names of all members visible to the logged-in user
	public Set<String> listMembers() throws NoUserLoggedInException {
		requireLoggedIn();
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			if (isVisibleToLoggedInUser(each)) members.add(each.getUserName());
		}
		return members;
	}

	// an account is visible to the logged-in user unless it has blocked her
	private boolean isVisibleToLoggedInUser(Account account) {
		if (account == null) return false;
		if (loggedInUser == null) return true;
		if (account == loggedInUser) return true;   // you can always see yourself
		return !account.hasBlocked(loggedInUser.getUserName());
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {
		try {
			Account accountForUserName = findAccountForUserName(userName);
			accountForUserName.requestFriendship(me);
		} catch (NullPointerException e) {
			System.out.println("No such user: " + userName);
		}
	}

	// from my account, accept a pending friend request from another user with userName
	public void acceptFriendshipFrom(String userName, Account me) {
		try {
			Account accountForUserName = findAccountForUserName(userName);
			accountForUserName.friendshipAccepted(me);
		} catch (NullPointerException e) {
			System.out.println("No such user: " + userName);
		}
	}

	public void acceptAllFriendshipsTo(Account me) {
		Set<String> requests = new HashSet<>(me.getIncomingRequests());
		requests.forEach(each -> {
			Account accountForUserName = findAccountForUserName(each);
			accountForUserName.friendshipAccepted(me);
		});
		me.getIncomingRequests().clear();
	}

	public void rejectFriendshipFrom(String userName, Account me) {
		if (me == null || userName == null) return;
		Account accountForUserName = findAccountForUserName(userName);
		if (accountForUserName != null) {
			accountForUserName.friendshipRejected(me);
		}
		me.getIncomingRequests().remove(userName);
	}

	public void rejectAllFriendshipsTo(Account me) {
		Collection<String> requests = new HashSet<>(me.getIncomingRequests());
    	requests.forEach(each -> 
			rejectFriendshipFrom(each, me));
		me.getIncomingRequests().clear();
	}

	public void sendFriendshipCancellationTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.cancelFriendship(me);
	}

	public void leave(Account me) {
		for (String each : new HashSet<>(me.getFriends())) {
			findAccountForUserName(each).cancelFriendship(me);
		}
		for (Account each : accounts) {
			each.getIncomingRequests().remove(me.getUserName());
			each.getOutgoingRequests().remove(me.getUserName());
		}
		accounts.remove(me);
	}

	public void autoAcceptFriendshipsTo(Account me) {
		me.autoAcceptFriendships();
	}

	// ----- ISocialNetwork stubs (T1): created to satisfy the compiler only -----

	@Override
	public Account login(Account me) {
		if (me == null) return null;
		if (accounts.contains(me)) {
			loggedInUser = me;
			return me;
		}
		return null;
	}

	@Override
	public boolean hasMember(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) return false;
		return isVisibleToLoggedInUser(findAccountForUserName(userName));
	}

	@Override
	public void sendFriendshipTo(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) return;
		Account target = findAccountForUserName(userName);
		if (!isVisibleToLoggedInUser(target)) return;   // blocked: cannot even see her
		target.requestFriendship(loggedInUser);
	}

	@Override
	public void block(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) return;
		loggedInUser.block(userName);
	}

	@Override
	public void unblock(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		if (userName == null) return;
		loggedInUser.unblock(userName);
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptFriendshipFrom(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptAllFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

	@Override
	public void rejectFriendshipFrom(String userName) throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

	@Override
	public void rejectAllFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

	@Override
	public void autoAcceptFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		loggedInUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships() throws NoUserLoggedInException {
		requireLoggedIn();
		loggedInUser.cancelAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends() throws NoUserLoggedInException {
		requireLoggedIn();
		Set<String> recommendations = new HashSet<String>();

		// count, for every candidate, how many of my friends they are friends with
		Map<String, Integer> commonFriendCount = new HashMap<String, Integer>();
		for (String friendName : loggedInUser.getFriends()) {
			Account friend = findAccountForUserName(friendName);
			if (friend == null) continue;
			for (String candidate : friend.getFriends()) {
				if (candidate.equals(loggedInUser.getUserName())) continue;  // not me
				if (loggedInUser.hasFriend(candidate)) continue;             // not already a friend
				Integer soFar = commonFriendCount.get(candidate);
				commonFriendCount.put(candidate, soFar == null ? 1 : soFar + 1);
			}
		}

		for (Map.Entry<String, Integer> each : commonFriendCount.entrySet()) {
			if (each.getValue() < 2) continue;                          // needs at least two
			if (loggedInUser.hasBlocked(each.getKey())) continue;       // not someone I blocked
			if (!isVisibleToLoggedInUser(findAccountForUserName(each.getKey()))) continue;
			recommendations.add(each.getKey());
		}
		return recommendations;
	}

	@Override
	public void leave() throws NoUserLoggedInException {
		requireLoggedIn();
		// TODO Auto-generated method stub
	}

}
