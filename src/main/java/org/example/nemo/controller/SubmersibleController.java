package org.example.nemo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import org.example.nemo.exceptions.PositionNotAvailableException;
import org.example.nemo.model.Obstacle;
import org.example.nemo.model.Submersible;
import org.example.nemo.model.SubmersibleDirection;
import org.example.nemo.service.SubmersibleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submersible")
public class SubmersibleController {

  private final SubmersibleService submersibleService;

  public SubmersibleController(SubmersibleService submersibleService) {
    this.submersibleService = submersibleService;
  }

  @GetMapping("/position/all")
  public ResponseEntity<LinkedHashMap<Integer, Submersible>> getAllObstacles() {
    return ResponseEntity.ok(submersibleService.getSubmersibleTrajectory());
  }

  @GetMapping("/position")
  public ResponseEntity<Submersible> getPosition() throws PositionNotAvailableException {
    return ResponseEntity.ok(submersibleService.getSubmersiblePosition());
  }

  @PostMapping("/position/absolute")
  public ResponseEntity<Submersible> moveSubmersible(@RequestBody Submersible submersible) {
    return ResponseEntity.ok(submersibleService.moveSubmersible(submersible));
  }

  @PostMapping("/position/relative")
  public ResponseEntity<Submersible> moveSubmersibleDirection(@RequestBody SubmersibleDirection submersibleDirection)
      throws PositionNotAvailableException {
    return ResponseEntity.ok(submersibleService.moveSubmersibleByDirection(submersibleDirection));
  }

  @PutMapping("/position/{index}")
  public ResponseEntity updatePositionByIndex(@PathVariable Integer index, @RequestBody Submersible submersible)
      throws PositionNotAvailableException {
    submersibleService.updatePositionByIndex(index, submersible);
    return ResponseEntity.ok().build();
  }
}
