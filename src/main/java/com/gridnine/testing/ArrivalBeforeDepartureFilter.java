package com.gridnine.testing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Фильтр, который исключает перелеты, содержащие сегменты с датой прилета раньше даты вылета.
 */
class ArrivalBeforeDepartureFilter implements FlightFilter {
    /**
     * Фильтрует перелеты, исключая те, у которых хотя бы один сегмент имеет дату прилета раньше даты вылета.
     *
     * @param flights список перелетов для фильтрации
     * @return отфильтрованный список перелетов
     */
    @Override
    public List<Flight> filter(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())))  // Прилет после вылета
                .collect(Collectors.toList());
    }
}
