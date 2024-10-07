package com.gridnine.testing;

import java.util.List;

/**
 * Класс для управления процессом фильтрации перелетов с использованием одного или нескольких фильтров.
 * Фильтры могут быть переданы динамически.
 */
class FlightFilterProcessor {
    private final List<FlightFilter> filters;

    FlightFilterProcessor(List<FlightFilter> filters) {
        this.filters = filters;
    }

    /**
     * Применяет все переданные фильтры к списку перелетов последовательно.
     *
     * @param flights список перелетов для фильтрации
     * @return отфильтрованный список перелетов
     */
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> filteredFlights = flights;
        for (FlightFilter filter : filters) {
            filteredFlights = filter.filter(filteredFlights);  // Применение каждого фильтра по порядку
        }
        return filteredFlights;
    }
}
