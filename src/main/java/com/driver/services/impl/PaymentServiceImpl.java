package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
          if(mode.equalsIgnoreCase(PaymentMode.CASH.toString()) || mode.equalsIgnoreCase(PaymentMode.UPI.toString())
          || mode.equalsIgnoreCase(PaymentMode.CARD.toString())){

              Reservation reservation=reservationRepository2.findById(reservationId).get();
              int amtToBePaid=reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();

              if(amountSent>=amtToBePaid){

                  Payment payment=new Payment();
                  payment.setPaymentCompleted(true);
                  payment.setPaymentMode(PaymentMode.valueOf(mode));//String to Enum...
                  payment.setReservation(reservation);

                  reservation.setPayment(payment);

                  paymentRepository2.save(payment);
                  reservationRepository2.save(reservation);

                  return payment;
              }
              else throw new Exception("Insufficient Amount");
          }
          else throw new Exception("Payment mode not detected");


    }
}
