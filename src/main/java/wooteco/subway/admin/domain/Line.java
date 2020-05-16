package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Line {
    @Id
    private Long id;
    private String name;
    private String backgroundColor;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<LineStation> stations = new HashSet<>();

    public Line() {
    }

    public Line(Long id, String name, String backgroundColor, LocalTime startTime,
                LocalTime endTime, int intervalTime) {
        this.id = id;
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Line of(String name, String backgroundColor, LocalTime startTime, LocalTime endTime,
                     int intervalTime) {
        return new Line(null, name, backgroundColor, startTime, endTime, intervalTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public Set<LineStation> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }

        if (line.getBackgroundColor() != null) {
            this.intervalTime = line.getIntervalTime();
        }

        if (line.getStartTime() != null) {
            this.startTime = line.getStartTime();
        }
        if (line.getEndTime() != null) {
            this.endTime = line.getEndTime();
        }
        if (line.getIntervalTime() != 0) {
            this.intervalTime = line.getIntervalTime();
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void addLineStation(LineStation lineStation) {
        stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
                .findAny()
                .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

        stations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        LineStation targetLineStation = stations.stream()
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), stationId))
                .findFirst()
                .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        stations.remove(targetLineStation);
    }

    public List<Long> getLineStationsId() {
        if (stations.isEmpty()) {
            return new ArrayList<>();
        }

        LineStation firstLineStation = stations.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstLineStation.getStationId());

        while (true) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            Optional<LineStation> nextLineStation = stations.stream()
                    .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                    .findFirst();

            if (!nextLineStation.isPresent()) {
                break;
            }

            stationIds.add(nextLineStation.get().getStationId());
        }

        return stationIds;
    }
}
