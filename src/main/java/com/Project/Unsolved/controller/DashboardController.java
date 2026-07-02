package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.DashboardStatsDto;
import com.Project.Unsolved.dto.PlatformStatsDto;
import com.Project.Unsolved.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {

        return ResponseEntity.ok(
                dashboardService.getDashboardStats()
        );

    }
    @GetMapping("/platform-stats")
    public ResponseEntity<PlatformStatsDto> getPlatformStats() {
        return ResponseEntity.ok(
                dashboardService.getPlatformStats()
        );
    }

}