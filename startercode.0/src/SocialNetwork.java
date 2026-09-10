import java.util.HashSet;
import java.util.Collection;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

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
	
	// list user names of all members
	public Set<String> listMembers() {
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			members.add(each.getUserName());
		}
		return members;
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
		Collection<String> requests = new HashSet<>(me.getIncomingRequests());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMember(String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendFriendshipTo(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void block(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unblock(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptFriendshipFrom(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptAllFriendships() {
		// TODO Auto-generated method stub
	}

	@Override
	public void rejectFriendshipFrom(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void rejectAllFriendships() {
		// TODO Auto-generated method stub
	}

	@Override
	public void autoAcceptFriendships() {
		// TODO Auto-generated method stub
	}

	@Override
	public void cancelAutoAcceptFriendships() {
		// TODO Auto-generated method stub
	}

	@Override
	public Set<String> recommendFriends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
	}

}
