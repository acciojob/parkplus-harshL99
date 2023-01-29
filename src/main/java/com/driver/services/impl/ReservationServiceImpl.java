package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
       if(!userRepository3.existsById(userId) || !parkingLotRepository3.existsById(parkingLotId))
           throw new Exception("Cannot make reservation");

       ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();

       List<Spot> spots=parkingLot.getSpotList();

       int idx=-1;
       int minPrice=Integer.MAX_VALUE;

       Map<SpotType,Integer> hm=new HashMap<>();
       hm.put(SpotType.TWO_WHEELER,2);
       hm.put(SpotType.FOUR_WHEELER,4);
       hm.put(SpotType.OTHERS,100);

       for(int i=0;i<spots.size();i++){
           if(!spots.get(i).getOccupied() && spots.get(i).getPricePerHour()<minPrice && hm.get(spots.get(i).getSpotType())<=numberOfWheels){
               minPrice=spots.get(i).getPricePerHour();
               idx=i;
           }
       }
       if(idx==-1) throw new Exception("Cannot make reservation");
       Spot reqSpot=spots.get(idx);
       reqSpot.setOccupied(true);
       parkingLot.setSpotList(spots);

       User user=userRepository3.findById(userId).get();

       Reservation newReservation=new Reservation();
       newReservation.setNumberOfHours(timeInHours);
       newReservation.setUser(user);
       newReservation.setSpot(reqSpot);
       reqSpot.getReservationList().add(newReservation);
       user.getReservationList().add(newReservation);

       reservationRepository3.save(newReservation);
       spotRepository3.save(reqSpot);
       parkingLotRepository3.save(parkingLot);
       userRepository3.save(user);
       return newReservation;
    }
}
