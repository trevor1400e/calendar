package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Event;
import com.example.model.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{
	
	Event findById(int id);
	
}
