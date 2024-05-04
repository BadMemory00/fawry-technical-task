package cam.fawry.task.services;

import cam.fawry.task.entities.Vehicle;
import cam.fawry.task.entities.VehicleTypes;
import cam.fawry.task.logging.Logger;
import cam.fawry.task.logging.Loggers;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TollCalculator {
    private final Logger logger;

    public TollCalculator() {
        this(Loggers.consoleLogger);
    }

    public TollCalculator(Logger logger) {
        this.logger = logger;
    }

    /**
     * Calculate the total toll fee for one day
     *
     * @param vehicle - the vehicle
     * @param dates   - date and time of all passes on one day
     * @return - the total toll fee for that day
     */
    public int getTollFee(Vehicle vehicle, ZonedDateTime... dates) {
        StringBuilder logsText = new StringBuilder();
        logsText.append(String.format("Vehicle: %s came in with dates: %s\n", vehicle, Arrays.toString(dates)));

        final ZonedDateTime intervalStart = dates[0];
        int intervalStartFee = getTollFee(intervalStart, vehicle);

        AtomicInteger totalFee = new AtomicInteger(intervalStartFee);

        logsText.append(String.format("Date: %s for vehicle: %s, has fees: %s, making total fees equals: %s \n", intervalStart, vehicle, intervalStartFee, totalFee.get()));

        Arrays.stream(dates)
                .skip(1)
                .forEach(currentDate -> {
                    int currentFee = getTollFee(currentDate, vehicle);
                    if ((currentDate.toEpochSecond() - intervalStart.toEpochSecond()) / 60 <= 60) {
                        if (totalFee.get() > 0) totalFee.getAndUpdate(r -> r - intervalStartFee);
                        totalFee.addAndGet(Math.max(currentFee, intervalStartFee));
                        logsText.append(String.format("Date: %s for vehicle: %s, is less than an hour after the previous date, has fees: %s, making total fees equals (after retaining only the highest date's fee): %s \n", currentDate, vehicle, currentFee, totalFee.get()));
                    } else {
                        totalFee.addAndGet(currentFee);
                        logsText.append(String.format("Date: %s for vehicle: %s, has fees: %s, making total fees equals: %s \n", currentDate, vehicle, currentFee, totalFee.get()));
                    }
                });

        logger.log(logsText.toString());

        return Math.min(totalFee.get(), 60);
    }


    private int getTollFee(final ZonedDateTime date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14 && minute >= 30) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18;
        else if (hour == 17) return 13;
        else if (hour == 18 && minute <= 29) return 8;
        else return 0;
    }

    private Boolean isTollFreeDate(ZonedDateTime date) {
        int year = date.getYear();
        Month month = date.getMonth();
        int day = date.getDayOfMonth();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) return true;

        if (year == 2013) {
            return month == Month.JANUARY && day == 1 ||
                    month == Month.MARCH && (day == 28 || day == 29) ||
                    month == Month.APRIL && (day == 1 || day == 30) ||
                    month == Month.MAY && (day == 1 || day == 8 || day == 9) ||
                    month == Month.JUNE && (day == 5 || day == 6 || day == 21) ||
                    month == Month.JULY ||
                    month == Month.NOVEMBER && day == 1 ||
                    month == Month.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31);
        }
        return false;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;

        return TollFreeVehicles
                .contains(vehicle.type());
    }

    final static List<VehicleTypes> TollFreeVehicles = new ArrayList<>() {{
        add(VehicleTypes.MOTORBIKE);
        add(VehicleTypes.TRACTOR);
        add(VehicleTypes.EMERGENCY);
        add(VehicleTypes.DIPLOMAT);
        add(VehicleTypes.FOREIGN);
        add(VehicleTypes.MILITARY);
    }};
}