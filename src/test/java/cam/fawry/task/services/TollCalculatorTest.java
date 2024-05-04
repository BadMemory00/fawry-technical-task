package cam.fawry.task.services;

import cam.fawry.task.entities.Car;
import cam.fawry.task.entities.Vehicle;
import cam.fawry.task.entities.VehicleTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TollCalculatorTest {

    private final static TollCalculator tollCalculator = new TollCalculator();

    @Test()
    @DisplayName("calculating fees for all holidays in the year 2013, and making sure all of them are 0")
    public void testCalculatingFeesForHolidays_ShouldReturn0() {
        Stream<ZonedDateTime> holidays = Stream.of(
                ZonedDateTime.of(2013, 1, 1, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 3, 28, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 3, 29, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 4, 1, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 4, 30, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 5, 1, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 5, 8, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 5, 9, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 6, 5, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 6, 6, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 6, 21, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 7, 9, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 11, 1, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 12, 24, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 12, 25, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 12, 26, 15, 0, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2013, 12, 31, 15, 0, 0, 0, ZoneId.of("Z"))
        );

        Vehicle car = new Car();

        holidays
                .map(holiday -> tollCalculator.getTollFee(car, holiday))
                .forEach(fee -> assertEquals(0, fee));
    }

    @Test()
    @DisplayName("calculating fees for weekends, and making sure all of them are 0")
    public void testCalculatingFeesForWeekEnds_ShouldReturn0() {
        Stream<ZonedDateTime> weekEnds = Stream.of(
                ZonedDateTime.of(2024, 5, 4, 15, 30, 0, 0, ZoneId.of("Z")),
                ZonedDateTime.of(2024, 5, 5, 15, 30, 0, 0, ZoneId.of("Z"))
        );

        Vehicle car = new Car();

        weekEnds
                .map(holiday -> tollCalculator.getTollFee(car, holiday))
                .forEach(fee -> assertEquals(0, fee));
    }

    @Test()
    @DisplayName("calculating fees for all fee-free vehicles types, and making sure all of them are 0")
    public void testCalculatingFeesForFeeFreeVehicle_ShouldReturn0() {
        Stream<? extends Vehicle> fee_free_vehicles = TollCalculator.TollFreeVehicles
                .stream()
                .map(TollFreeVehicle::new);

        Clock notTollFreeDate = Clock.fixed(Instant.parse("2024-05-03T09:30:13.915279200Z"), ZoneId.of("Z"));

        ZonedDateTime date = ZonedDateTime.now(notTollFreeDate);

        fee_free_vehicles
                .map(vehicle -> tollCalculator.getTollFee(vehicle, date))
                .forEach(fee -> assertEquals(0, fee));
    }

    @Test()
    @DisplayName("calculating fees for all pre-determined timeslots with different fees")
    public void testCalculatingFeesForPreDeterminedTimeSlots() {
        Map<ZonedDateTime, Integer> preDeterminedTimeslotsAndFees = new HashMap<>() {{
            put(ZonedDateTime.of(2024, 1, 2, 6, 29, 0, 0, ZoneId.of("Z")), 8);
            put(ZonedDateTime.of(2024, 1, 2, 6, 30, 0, 0, ZoneId.of("Z")), 13);
            put(ZonedDateTime.of(2024, 1, 2, 7, 29, 0, 0, ZoneId.of("Z")), 18);
            put(ZonedDateTime.of(2024, 1, 2, 8, 29, 0, 0, ZoneId.of("Z")), 13);
            put(ZonedDateTime.of(2024, 1, 2, 8, 30, 0, 0, ZoneId.of("Z")), 8);
            put(ZonedDateTime.of(2024, 1, 2, 9, 29, 0, 0, ZoneId.of("Z")), 0);
            put(ZonedDateTime.of(2024, 1, 2, 9, 30, 0, 0, ZoneId.of("Z")), 8);
            put(ZonedDateTime.of(2024, 1, 2, 15, 29, 0, 0, ZoneId.of("Z")), 13);
            put(ZonedDateTime.of(2024, 1, 2, 15, 30, 0, 0, ZoneId.of("Z")), 18);
            put(ZonedDateTime.of(2024, 1, 2, 16, 29, 0, 0, ZoneId.of("Z")), 18);
            put(ZonedDateTime.of(2024, 1, 2, 17, 29, 0, 0, ZoneId.of("Z")), 13);
            put(ZonedDateTime.of(2024, 1, 2, 18, 29, 0, 0, ZoneId.of("Z")), 8);
            put(ZonedDateTime.of(2024, 1, 2, 18, 30, 0, 0, ZoneId.of("Z")), 0);
        }};

        Vehicle car = new Car();

        preDeterminedTimeslotsAndFees
                .forEach((date, fee) -> assertEquals(tollCalculator.getTollFee(car, date), fee));
    }

    @Test()
    @DisplayName("calculating fees for two dates with less than an hour in between them, to show it returns the bigger fair")
    public void testCalculatingFeesForTwoDatesWithLessThanHourInBetween_ShouldReturnBiggerFee() {
        ZonedDateTime[] dates = new ZonedDateTime[]{
                ZonedDateTime.of(2024, 1, 2, 6, 29, 0, 0, ZoneId.of("Z")), // 8
                ZonedDateTime.of(2024, 1, 2, 6, 30, 0, 0, ZoneId.of("Z")) // 13
        };

        Vehicle car = new Car();

        int expectedFee = tollCalculator.getTollFee(car, dates);
        int actualFee = 13;

        assertEquals(expectedFee, actualFee);
    }

    public record TollFreeVehicle(VehicleTypes type) implements Vehicle {
    }
}