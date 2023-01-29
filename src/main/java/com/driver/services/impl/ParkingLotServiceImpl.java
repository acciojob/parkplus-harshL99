package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
       ParkingLot parkingLot=new ParkingLot(name,address);
       return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        SpotType spotType;
        if(numberOfWheels<=2) spotType=SpotType.TWO_WHEELER;
        else if(numberOfWheels>2 && numberOfWheels<=4) spotType=SpotType.FOUR_WHEELER;
        else spotType=SpotType.OTHERS;

        Spot newSpot=new Spot(spotType,pricePerHour,false);
        newSpot.setParkingLot(parkingLot);

        parkingLot.getSpotList().add(newSpot);

        parkingLotRepository1.save(parkingLot);
        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot;
        try {
             spot = spotRepository1.findById(spotId).get();
        }catch (Exception e){
            System.out.println("No Such Element exists!");
            return;
        }
        ParkingLot parkingLot=spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);
        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot=spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        ParkingLot parkingLot=spot.getParkingLot();

        List<Spot> spots=parkingLot.getSpotList();
        int idx=-1;

        for(int i=0;i<spots.size();i++){
            if(spots.get(i).getId()==spotId){
                idx=i;
                break;
            }
        }
        spots.get(idx).setPricePerHour(pricePerHour);
        parkingLot.setSpotList(spots);

        spotRepository1.save(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        for(Spot spot : parkingLot.getSpotList()){
            spotRepository1.delete(spot);
        }
        parkingLotRepository1.delete(parkingLot);
    }
}
