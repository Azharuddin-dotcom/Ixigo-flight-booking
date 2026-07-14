package com.ixigo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ixigo.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
	
	public Optional<BookingEntity> findByPassengerName(String passengerName);
	
	public Optional<BookingEntity> findByPnr(String pnr);
	
}
