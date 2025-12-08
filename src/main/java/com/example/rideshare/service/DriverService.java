package com.example.rideshare.service;

import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DriverService {

    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public DriverService(RideRepository rideRepo, UserRepository userRepo) {
        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    public List<Ride> getPendingRides() {
        return rideRepo.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String username) {
        User driver = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Driver not found"));
        if(!"ROLE_DRIVER".equals(driver.getRole()))
            throw new BadRequestException("Only drivers can accept rides");
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if(!"REQUESTED".equals(ride.getStatus()))
            throw new BadRequestException("Ride already accepted");
        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        return rideRepo.save(ride);
    }
}
