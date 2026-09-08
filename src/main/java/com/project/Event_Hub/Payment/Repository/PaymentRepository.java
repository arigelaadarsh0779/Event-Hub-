package com.project.Event_Hub.Payment.Repository;

import com.project.Event_Hub.Payment.Entity.PaymetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymetEntity,Long> {

}
