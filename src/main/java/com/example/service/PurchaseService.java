package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Event;
import com.example.model.Purchase;
import com.example.model.User;
import com.example.repository.PurchaseRepository;

@Service
public class PurchaseService {

	@Autowired
	PurchaseRepository purchaseRepo;
	
	public Purchase createPurchase(User user, Event event, double amount, String transid){
		
		//TODO Get price from event object instead of passing it here
		
		Purchase purchase = new Purchase();
		purchase.setAmount(amount);
		purchase.setEvent(event);
		purchase.setUser(user);
		purchase.setTransid(transid);

		return purchaseRepo.save(purchase);
	}
	
}
