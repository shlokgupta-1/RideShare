package com.example.rideshare.controller;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) { this.rideService = rideService; }

    @PostMapping
    public Ride createRide(@AuthenticationPrincipal String username,
                           @Valid @RequestBody CreateRideRequest req) {
        return rideService.createRide(req, username);
    }

    @GetMapping("/user")
    public List<Ride> getMyRides(@AuthenticationPrincipal String username) {
        return rideService.getRidesByUser(username);
    }

    @PostMapping("/{rideId}/complete")
    public Ride completeRide(@AuthenticationPrincipal String username,
                             @PathVariable String rideId) {
        return rideService.completeRide(rideId, username);
    }
}
