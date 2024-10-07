package com.gridnine.testing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Получение тестовых данных
        List<Flight> flights = FlightBuilder.createFlights();

        // Показать все перелеты перед фильтрацией
        System.out.println("Все перелеты:");
        flights.forEach(System.out::println);

        // 1. Исключить перелеты с вылетом до текущего момента
        System.out.println("\nПерелеты с вылетом до текущего момента:");
        List<Flight> flightsAfterNow = new DepartureBeforeNowFilter().filter(flights);
        flightsAfterNow.forEach(System.out::println);

        // 2. Исключить сегменты с датой прилета раньше даты вылета
        System.out.println("\nПерелеты без сегментов с прилетом раньше вылета:");
        List<Flight> flightsWithoutArrivalBeforeDeparture = new ArrivalBeforeDepartureFilter().filter(flights);
        flightsWithoutArrivalBeforeDeparture.forEach(System.out::println);

        // 3. Исключить перелеты с временем на земле больше 2 часов
        System.out.println("\nПерелеты с временем на земле не больше 2 часов:");
        List<Flight> flightsWithLimitedGroundTime = new GroundTimeExceedsTwoHoursFilter().filter(flights);
        flightsWithLimitedGroundTime.forEach(System.out::println);

        // 4. Комбинировать все фильтры
        System.out.println("\nКомбинация всех фильтров:");
        List<Flight> filteredFlights = new FlightFilterProcessor(List.of(new ArrivalBeforeDepartureFilter(),
                new GroundTimeExceedsTwoHoursFilter(),
                new DepartureBeforeNowFilter())).filter(flights);
        filteredFlights.forEach(System.out::println);
    }
}