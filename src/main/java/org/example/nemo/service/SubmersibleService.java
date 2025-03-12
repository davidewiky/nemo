package org.example.nemo.service;

import java.util.LinkedHashMap;
import java.util.List;
import org.example.nemo.exceptions.PositionNotAvailableException;
import org.example.nemo.model.DirectionEnum;
import org.example.nemo.model.Submersible;
import org.example.nemo.model.SubmersibleDirection;
import org.springframework.stereotype.Service;

@Service
public class SubmersibleService {

  private static final LinkedHashMap<Integer, Submersible> submersibleTrajectory = new LinkedHashMap<>();
  static {
    submersibleTrajectory.put(0, new Submersible(0, 0)); // start position
  }

  public Submersible getSubmersiblePosition() throws PositionNotAvailableException {
     return getLastSubmersiblePosition();
  }

  public LinkedHashMap<Integer, Submersible> getSubmersibleTrajectory() {
    return submersibleTrajectory;
  }

  public Submersible moveSubmersible(Submersible submersible) {
    if (submersibleTrajectory.entrySet().stream().skip(submersibleTrajectory.size() - 1).findFirst().isPresent()) {
      Integer moveIndex = submersibleTrajectory.entrySet().stream().skip(submersibleTrajectory.size() - 1).findFirst().get().getKey();
      submersibleTrajectory.put(++moveIndex, submersible);
    }
    submersibleTrajectory.put(0, submersible);
    return submersible;
  }

  public Submersible moveSubmersibleByDirection(SubmersibleDirection submersibleDirection)
      throws PositionNotAvailableException {
    submersibleTrajectory.entrySet().stream().skip(submersibleTrajectory.size() - 1).findFirst()
        .ifPresent(entry -> {
          Integer moveIndex = entry.getKey();
          Submersible submersibleCurrent = entry.getValue();
          if (submersibleDirection.quantity() != null) {
            Submersible submersible = manageCoordinatesBasedOnDirection(submersibleCurrent,
                submersibleDirection.direction(),
                submersibleDirection.quantity());
            submersibleTrajectory.put(++moveIndex, submersible);
          }
        });
    return getLastSubmersiblePosition();
  }

  public void updatePositionByIndex(Integer index, Submersible submersible)
      throws PositionNotAvailableException {
    if (submersibleTrajectory.containsKey(index)) {
      submersibleTrajectory.put(index, submersible);
    } else {
      throw new PositionNotAvailableException("Requested movement index is not available");
    }
  }

  private Submersible getLastSubmersiblePosition() throws PositionNotAvailableException {
    if (submersibleTrajectory.entrySet().stream().skip(submersibleTrajectory.size() - 1).findFirst().isPresent()) {
      return submersibleTrajectory.entrySet().stream().skip(submersibleTrajectory.size() - 1).findFirst().get().getValue();
    }
    throw new PositionNotAvailableException("Position not available");
  }

  private Submersible manageCoordinatesBasedOnDirection(Submersible submersible, DirectionEnum direction, Double movementAmount) {
    return switch (direction) {
      case UP ->
          new Submersible(submersible.getX(), submersible.getY() + movementAmount);
      case DOWN ->
          new Submersible(submersible.getX(), submersible.getY() - movementAmount);
      case LEFT ->
          new Submersible(submersible.getX() - movementAmount, submersible.getY());
      case RIGHT ->
          new Submersible(submersible.getX() + movementAmount, submersible.getY());
    };
  }
}
