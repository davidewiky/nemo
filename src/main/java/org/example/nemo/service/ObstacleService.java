package org.example.nemo.service;

import java.util.ArrayList;
import java.util.List;
import org.example.nemo.model.Obstacle;
import org.springframework.stereotype.Service;

@Service
public class ObstacleService {

  private static final List<Obstacle> OBSTACLES = new ArrayList<>();

  public List<Obstacle> getObstacles() {
    return OBSTACLES;
  }

  public Obstacle setObstacle(Obstacle obstacle) {
    OBSTACLES.add(obstacle);
    return OBSTACLES.stream().skip(OBSTACLES.size() - 1).findFirst().orElse(null);
  }

  public List<Obstacle> setObstacles(List<Obstacle> obstacles) {
    OBSTACLES.addAll(obstacles);
    return OBSTACLES;
  }
}
