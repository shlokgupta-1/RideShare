package com.example.rideshare.controller;

import org.example.rideshare.model.Ride;
import com.example.rideshare.service.DriverService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
@PreAuthorize("hasAuthority('ROLE_DRIVER')")
public class DriverController {

    private final DriverService driverService;
    public DriverController(DriverService driverService) { this.driverService = driverService; }

    @GetMapping("/rides/requests")
    public List<Ride> getPendingRides() {
        return driverService.getPendingRides();
    }

    @PostMapping("/rides/{rideId}/accept")
    public Ride acceptRide(@AuthenticationPrincipal String username,
                           @PathVariable String rideId) {
        return driverService.acceptRide(rideId, username);
    }
}
