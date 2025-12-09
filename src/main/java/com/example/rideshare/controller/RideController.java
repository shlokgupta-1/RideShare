package com.example.rideshare.controller;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.model.Ride;
import com.example.rideshare.service.RideService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // ---------------- Create Ride ----------------
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Ride createRide(@AuthenticationPrincipal String username,
                           @Valid @RequestBody CreateRideRequest req) {
        return rideService.createRide(req, username);
    }

    // ---------------- Get My Rides ----------------
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public List<Ride> getMyRides(@AuthenticationPrincipal String username) {
        return rideService.getRidesByUser(username);
    }

    // ---------------- Complete Ride ----------------
    @PostMapping("/{rideId}/complete")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER')")
    public Ride completeRide(@AuthenticationPrincipal String username,
                             @PathVariable String rideId) {
        return rideService.completeRide(rideId, username);
    }
}
