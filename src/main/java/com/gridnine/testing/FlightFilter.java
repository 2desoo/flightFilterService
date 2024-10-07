package com.gridnine.testing;

import java.util.List;

/**
 * Интерфейс для фильтров перелетов.
 * Каждый фильтр должен реализовывать метод filter(), который принимает список перелетов и возвращает отфильтрованный список.
 */
interface FlightFilter {
    /**
     * Фильтрует список перелетов на основе заданных критериев.
     *
     * @param flights список перелетов для фильтрации
     * @return отфильтрованный список перелетов
     */
    List<Flight> filter(List<Flight> flights);
}
