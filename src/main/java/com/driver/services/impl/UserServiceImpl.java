package com.driver.services.impl;

import com.driver.model.Reservation;
import com.driver.model.User;
import com.driver.repository.ReservationRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository4;

    @Autowired
    ReservationRepository reservationRepository4;
    @Override
    public void deleteUser(Integer userId) {
//        User user;
//        try {
//            user = userRepository4.findById(userId).get();
//        }catch (Exception e){
//            System.out.println("User does not exists!");
//            return;
//        }
//        for(Reservation reservation : user.getReservationList()){
//            reservationRepository4.deleteById(reservation.getId());
//        }
        userRepository4.deleteById(userId);
    }

    @Override
    public User updatePassword(Integer userId, String password) {
        User user=userRepository4.findById(userId).get();
        user.setPassword(password);
        return userRepository4.save(user);//Update after setting new Password...
    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        User user=new User(name,phoneNumber,password);
        userRepository4.save(user);
    }
}
