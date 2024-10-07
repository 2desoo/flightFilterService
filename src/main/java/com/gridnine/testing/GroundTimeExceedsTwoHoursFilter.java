package com.gridnine.testing;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Фильтр, который исключает перелеты с временем на земле между сегментами более двух часов.
 * Время на земле считается как интервал между прилетом одного сегмента и вылетом следующего.
 */
class GroundTimeExceedsTwoHoursFilter implements FlightFilter {
    /**
     * Фильтрует перелеты, исключая те, у которых общее время на земле между сегментами превышает два часа.
     *
     * @param flights список перелетов для фильтрации
     * @return отфильтрованный список перелетов
     */
    @Override
    public List<Flight> filter(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    long groundTimeInMinutes = 0;
                    for (int i = 1; i < segments.size(); i++) {
                        Segment prev = segments.get(i - 1);
                        Segment next = segments.get(i);
                        groundTimeInMinutes += Duration.between(prev.getArrivalDate(), next.getDepartureDate()).toMinutes();
                    }
                    return groundTimeInMinutes <= 120;  // Время на земле не должно превышать 120 минут (2 часа)
                })
                .collect(Collectors.toList());
    }
}
