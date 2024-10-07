package com.gridnine.testing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс Flight описывает перелет. В нем хранится список сегментов перелета.
 */
class Flight {

    private final List<Segment> segments; // Список сегментов перелета

    Flight(final List<Segment> segs) {
        segments = segs;
    }

    List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
