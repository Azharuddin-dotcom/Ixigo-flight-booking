package com.ixigo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ixigo.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

}
