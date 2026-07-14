package com.ixigo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ixigo.entity.BookingEntity;
import com.ixigo.entity.PaymentEntity;
import com.ixigo.exception.InsufficientBalanceException;
import com.ixigo.repository.BookingRepository;
import com.ixigo.repository.PaymentRepository;
import com.ixigo.request.BookingRequest;
import com.ixigo.response.BookingResponse;

import jakarta.transaction.Transactional;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository bookingRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Transactional
	public BookingResponse flightBooking(BookingRequest bookingRequest) {
		
		BookingEntity bookingEntity = new BookingEntity();
		
		bookingEntity.setPassengerName(bookingRequest.getPassengerName());
		bookingEntity.setSource(bookingRequest.getSource());
		bookingEntity.setDestination(bookingRequest.getDestination());
		bookingEntity.setTravelDate(bookingRequest.getTravelDate());
		bookingEntity.setFlightNumber(bookingRequest.getFlightNumber());
		bookingEntity.setSeatNumber(bookingRequest.getSeatNumber());
		bookingEntity.setFare(bookingRequest.getFare());
//		bookingEntity.setPnr(PNRGenerator.generatePNR());
		bookingEntity.setBookingStatus("BOOKING_INITIALIZATION...");
		
		bookingEntity = bookingRepository.save(bookingEntity);
		
		// Initialize Payment -
		PaymentEntity paymentEntity = new PaymentEntity();
		paymentEntity.setBookingId(bookingEntity.getBookingId());
		paymentEntity.setAmount(bookingEntity.getFare());
		paymentEntity.setTransactionId("TXN291567");
		
		try {
			String statusFromPaymentGateway = "pending";
			paymentEntity.setPaymentStatus(statusFromPaymentGateway.concat(" initializing payment...."));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new InsufficientBalanceException("User does not have enough balance to book ticket");
		}
		
		PaymentEntity responsePaymentEntity = paymentRepository.save(paymentEntity);
		
		BookingResponse response = null;
		
		if (responsePaymentEntity.getPaymentId() > 0) {
			
			bookingEntity.setPnr(PNRGenerator.generatePNR());
			bookingEntity.setBookingStatus(getRandomBookingStatus());
			
			BookingEntity updatedBookingEntity = bookingRepository.save(bookingEntity);
			
			response = new BookingResponse();
			
			response.setBookingId(bookingEntity.getBookingId());
			response.setPnr(bookingEntity.getPnr());
			response.setPassengerName(bookingEntity.getPassengerName());
			response.setSource(bookingEntity.getSource());
			response.setDestination(bookingEntity.getDestination());
			response.setTravelDate(bookingEntity.getTravelDate());
			response.setFlightNumber(bookingEntity.getFlightNumber());
			response.setSeatNumber(bookingEntity.getSeatNumber());
			response.setFare(bookingEntity.getFare());
			response.setBookingStatus(bookingEntity.getBookingStatus());
		}
		
		return response;
		
	}
	
	public static String getRandomBookingStatus() {
	    String[] statuses = {"PENDING", "CONFIRMED", "CANCELLED"};
	    return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];    // statuses[0 or 1 or 2] 
	}
	
	public String getPNR(String passengerName) {
		
		Optional<BookingEntity> entityOptional = bookingRepository.findByPassengerName(passengerName);
		
		if (entityOptional.isPresent()) {
			return entityOptional.get().getPnr();
		}
		
		return "No passenger found with name: " + passengerName;
	}
	
	public String getStatus(String pnr) {
		
		Optional<BookingEntity> entityOptional = bookingRepository.findByPnr(pnr);
		
		if (entityOptional.isPresent()) {
			return entityOptional.get().getBookingStatus();
		}
		
		return "No record found with pnr: " + pnr ;
	}
	
	public List<BookingResponse> getAllTickets(int pageNumber, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("fare").descending());   // Sort.by("fare").descending() - here fare is BookingEntity fieldName, JPA uses Entity field name for sorting
		
		Page<BookingEntity> tickets = bookingRepository.findAll(pageable);
		
		List<BookingResponse> bookingList = new ArrayList<BookingResponse>();
		
		for (BookingEntity entity : tickets) {
			
			BookingResponse bookingResponse = new BookingResponse();
			
			bookingResponse.setBookingId(entity.getBookingId());
			bookingResponse.setPnr(entity.getPnr());
			bookingResponse.setPassengerName(entity.getPassengerName());
			bookingResponse.setSource(entity.getSource());
			bookingResponse.setDestination(entity.getDestination());
			bookingResponse.setTravelDate(entity.getTravelDate());
			bookingResponse.setFlightNumber(entity.getFlightNumber());
			bookingResponse.setSeatNumber(entity.getSeatNumber());
			bookingResponse.setFare(entity.getFare());
			bookingResponse.setBookingStatus(entity.getBookingStatus());
			
			bookingList.add(bookingResponse);
		}
		
		return bookingList;
	}
	
}
