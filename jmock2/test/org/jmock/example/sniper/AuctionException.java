package org.jmock.example.sniper;

public class AuctionException extends Exception {
	public AuctionException(String message) {
		super(message);
	}
	
	public AuctionException(String message, Exception cause) {
		super(message, cause);
	}
}
