package org.example.nemo.controller;

import java.util.List;
import org.example.nemo.model.Obstacle;
import org.example.nemo.service.ObstacleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/obstacle")
public class ObstacleController {

  private final ObstacleService obstacleService;

  public ObstacleController(ObstacleService obstacleService) {
    this.obstacleService = obstacleService;
  }

  @GetMapping
  public ResponseEntity<List<Obstacle>> getObstacles() {
    return ResponseEntity.ok(obstacleService.getObstacles());
  }

  @PostMapping("/position/obstacle")
  public ResponseEntity<Obstacle> newObstacle(@RequestBody Obstacle obstacle) {
    return ResponseEntity.ok(obstacleService.setObstacle(obstacle));
  }
}
