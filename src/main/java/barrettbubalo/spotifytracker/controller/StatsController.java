package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.service.*;
import barrettbubalo.spotifytracker.model.*;
import barrettbubalo.spotifytracker.patterns.strategy.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/top")
    public ResponseEntity<?> getTop(@RequestParam String entity,
                                     @RequestParam(defaultValue = "PLAY_COUNT") String metric,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // TODO: restore session-based auth later
        Long accountId = 1L; // hardcoded for testing
        //Long accountId = (Long) session.getAttribute("accountId");

        MusicEntityType entityType = MusicEntityType.valueOf(entity.toUpperCase());
        MetricType metricType = MetricType.valueOf(metric.toUpperCase());

        return ResponseEntity.ok(statsService.getTopItems(accountId, entityType, metricType));
    }
}