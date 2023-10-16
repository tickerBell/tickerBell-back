package com.tickerBell.domain.map.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapApiResultPath {
    private int code;
    private String message;
    private String currentDateTime;
    private Route route;

    @Getter @Setter
    public static class Route {
        private List<Tracomfort> tracomfort;
    }

    @Getter @Setter
    public static class Tracomfort {
        private Summary summary;
        private List<List<Double>> path;
        private List<Section> section;
        private List<Guide> guide;
    }

    @Getter @Setter
    public static class Summary {
        private Start start;
        private Goal goal;
        private int distance;
        private int duration;
        private int etaServiceType;
        private String departureTime;
        private List<List<Double>> bbox;
        private int tollFare;
        private int taxiFare;
        private int fuelPrice;
    }

    @Getter @Setter
    public static class Start {
        private List<Double> location;

    }

    @Getter @Setter
    public static class Goal {
        private List<Double> location;
        private int dir;
    }

    @Getter @Setter
    public static class Section {
        private int pointIndex;
        private int pointCount;
        private int distance;
        private String name;
        private int congestion;
        private int speed;
    }

    @Getter @Setter
    public static class Guide {
        private int pointIndex;
        private int type;
        private String instructions;
        private int distance;
        private int duration;
    }
}

