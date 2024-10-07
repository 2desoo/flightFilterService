package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightFilterTest {

    private List<Flight> testFlights;

    // Инициализация тестовых данных перед каждым тестом
    @BeforeEach
    void setUp() {
        // Генерация тестовых перелетов из фабрики FlightBuilder
        testFlights = FlightBuilder.createFlights();
    }

    // Тест для фильтра, который исключает перелеты с вылетом до текущего момента
    @Test
    void testDepartureBeforeNowFilter() {
        FlightFilter filter = new DepartureBeforeNowFilter();
        List<Flight> result = filter.filter(testFlights);

        // Ожидаем, что перелеты с прошлым вылетом исключены (в тестовом наборе 2 перелета с прошлым вылетом)
        assertEquals(4, result.size(), "Ожидается 4 перелета после фильтрации");

        // Проверяем, что все оставшиеся перелеты имеют вылет в будущем
        result.forEach(flight -> flight.getSegments().forEach(segment ->
                assertTrue(segment.getDepartureDate().isAfter(LocalDateTime.now()),
                        "Дата вылета должна быть в будущем")
        ));
    }

    // Тест для фильтра, который исключает перелеты с прилетом раньше вылета
    @Test
    void testArrivalBeforeDepartureFilter() {
        FlightFilter filter = new ArrivalBeforeDepartureFilter();
        List<Flight> result = filter.filter(testFlights);

        // Ожидаем, что перелеты с некорректными сегментами (прилет раньше вылета) исключены (один перелет некорректен)
        assertEquals(5, result.size(), "Ожидается 5 перелетов после фильтрации");

        // Проверяем, что все сегменты имеют корректные даты (прилет позже вылета)
        result.forEach(flight -> flight.getSegments().forEach(segment ->
                assertTrue(segment.getArrivalDate().isAfter(segment.getDepartureDate()),
                        "Дата прилета должна быть позже даты вылета")
        ));
    }

    // Тест для фильтра, который исключает перелеты с временем на земле более 2 часов
    @Test
    void testGroundTimeExceedsTwoHoursFilter() {
        FlightFilter filter = new GroundTimeExceedsTwoHoursFilter();
        List<Flight> result = filter.filter(testFlights);

        // Ожидаем, что перелеты с временем на земле более 2 часов исключены (два перелета превышают 2 часа на земле)
        assertEquals(4, result.size(), "Ожидается 4 перелета после фильтрации");

        // Проверяем, что время на земле между сегментами каждого перелета не превышает 2 часов
        result.forEach(flight -> {
            List<Segment> segments = flight.getSegments();
            for (int i = 1; i < segments.size(); i++) {
                Segment prev = segments.get(i - 1);
                Segment next = segments.get(i);
                long groundTimeMinutes = java.time.Duration.between(prev.getArrivalDate(), next.getDepartureDate()).toMinutes();
                assertTrue(groundTimeMinutes <= 120, "Время на земле не должно превышать 2 часа");
            }
        });
    }

    // Тест для комбинации всех фильтров
    @Test
    void testCombinedFilters() {
        // Создаем процессор фильтров, который объединяет все три фильтра
        FlightFilterProcessor processor = new FlightFilterProcessor(
                List.of(new DepartureBeforeNowFilter(), new ArrivalBeforeDepartureFilter(), new GroundTimeExceedsTwoHoursFilter())
        );

        // Применяем все фильтры к тестовым перелетам
        List<Flight> result = processor.filter(testFlights);

        // Ожидаем, что после применения всех фильтров останется 2 перелета
        assertEquals(2, result.size(), "Ожидается 2 перелета после применения всех фильтров");
    }
}