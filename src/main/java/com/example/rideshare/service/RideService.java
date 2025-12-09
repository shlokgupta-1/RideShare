package com.example.rideshare.service;

import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.exception.NotFoundException;
import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.User;
import com.example.rideshare.repository.RideRepository;
import com.example.rideshare.repository.UserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class RideService {
    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public RideService(RideRepository rideRepo, UserRepository userRepo) {
        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    public Ride createRide(CreateRideRequest req, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(!"ROLE_USER".equals(user.getRole()))
            throw new BadRequestException("Only users can request rides");
        Ride ride = new Ride(user.getId(), req.getPickupLocation(), req.getDropLocation());
        return rideRepo.save(ride);
    }

    public List<Ride> getRidesByUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return rideRepo.findByUserId(user.getId());
    }

    public Ride completeRide(String rideId, String username) {
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if(!"ACCEPTED".equals(ride.getStatus()))
            throw new BadRequestException("Ride not accepted yet");
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(!ride.getUserId().equals(user.getId()) &&
                (ride.getDriverId() == null || !ride.getDriverId().equals(user.getId())))
            throw new BadRequestException("Not authorized to complete this ride");
        ride.setStatus("COMPLETED");
        return rideRepo.save(ride);
    }
}
