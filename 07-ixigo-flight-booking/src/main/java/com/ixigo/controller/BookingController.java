package com.ixigo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.request.BookingRequest;
import com.ixigo.response.BookingResponse;
import com.ixigo.service.BookingService;

@RestController
@RequestMapping("ixigo")
public class BookingController {

	@Autowired
	BookingService bookingService;
	
	@PostMapping("/bookflight")
	public BookingResponse doBooking(@RequestBody BookingRequest bookingRequest) {
		return bookingService.flightBooking(bookingRequest);
	}
	
	@GetMapping("/pnr")
	public String getPNR(@RequestParam String passengerName) {
		return bookingService.getPNR(passengerName);
	}
	
	@GetMapping("/status")
	public String getBookingStatus(@RequestParam String pnr) {
		return bookingService.getStatus(pnr);
	}
	
	@GetMapping("/getalltickets")
	public List<BookingResponse> getTickets(@RequestParam int pageNum, @RequestParam int pageSize) {
		return bookingService.getAllTickets(pageNum, pageSize);
	}
	
}
