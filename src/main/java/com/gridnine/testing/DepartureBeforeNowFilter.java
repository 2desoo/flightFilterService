package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс DepartureBeforeNowFilter реализует интерфейс FlightFilter.
 * Фильтр исключает перелеты по трем правилам:
 * 1. Вылет до текущего момента времени: если хотя бы один сегмент начинается в прошлом, то перелет исключается.
 * 2. Нереальная длительность сегмента: если хотя бы один сегмент длится более 24 часов, перелет исключается.
 * 3. Время прилета раньше времени вылета: исключаются сегменты, где время прилета раньше времени вылета.
 */
public class DepartureBeforeNowFilter implements FlightFilter {

    /**
     * Метод filter получает список перелетов и возвращает новый список, отфильтрованный
     * по следующим правилам:
     * 1. Перелеты, где хотя бы один сегмент начинается до текущего момента времени,
     *    исключаются.
     * 2. Перелеты, где хотя бы один сегмент длится более 24 часов, исключаются.
     * 3. Перелеты, где хотя бы один сегмент имеет время прилета раньше времени вылета,
     *    исключаются.
     *
     * @param flights Список перелетов для фильтрации.
     * @return Отфильтрованный список перелетов.
     */
    @Override
    public List<Flight> filter(List<Flight> flights) {
        LocalDateTime now = LocalDateTime.now();  // Получаем текущее время
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            // Проверяем, чтобы все сегменты вылетали после текущего времени
            boolean allSegmentsInFuture = flight.getSegments().stream()
                    .allMatch(segment -> segment.getDepartureDate().isAfter(now));

            // Проверяем, чтобы каждый сегмент не превышал 24 часов
            boolean reasonableDuration = flight.getSegments().stream()
                    .allMatch(segment -> Duration.between(segment.getDepartureDate(), segment.getArrivalDate()).toHours() <= 24);

            // Проверяем, чтобы время прилета не было раньше времени вылета
            boolean arrivalAfterDeparture = flight.getSegments().stream()
                    .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()));

            // Если все условия выполнены, добавляем перелет в результат
            if (allSegmentsInFuture && reasonableDuration && arrivalAfterDeparture) {
                result.add(flight);
            }
        }

        return result;
    }
}