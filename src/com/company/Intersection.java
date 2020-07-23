package com.company;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Intersection {

    private final Interval mFirst;
    private final Interval mSecond;

    public Intersection(Interval first, Interval second) {
        mFirst = first;
        mSecond = second;
    }

    public Interval getFirst() {
        return mFirst;
    }

    public Interval getSecond() {
        return mSecond;
    }

    /**
     * Проверяет пересечение периодов и выводит его, если есть. Входные параметры - НачПериода1, НачПериода2, КонПериода1, КонПериода2.
     * Если не заполнена начальная дата периода, то это означает "-бесконечность";
     * Если не заполнена конечная дата, то это означает "+бесконечность"
     */
    public static Intersection getIntersection(String from1, String to1, String from2, String to2) {
        Interval interval1 = new Interval(from1, to1);
        Interval interval2 = new Interval(from2, to2);

        if (interval1.hasIntersection(interval2)) {
            return new Intersection(interval1, interval2);
        } else return null;
    }

    public static List<Intersection> getIntersections(List<Interval> intervals) {
        List<Intersection> intersections = new ArrayList<>();

        // находим для каждого интервала пересечения с другими интервалами
        for (Interval curInterval : intervals) {
            for (Interval interval : intervals) {
                // пропускаем сравнение с самим собой
                if (curInterval == interval)
                    continue;

                Intersection inter = new Intersection(curInterval, interval);
                // пропускаем интервалы от повторого добавления
                if (curInterval.hasIntersection(interval) && intersections.stream().noneMatch(
                        intersection -> intersection.equals(inter))) {
                    intersections.add(inter);
                }
            }
        }

        return intersections;
    }

    /**
     * Получить интервалы пересечений
     */
    public static Interval getIntersectionInterval(Intersection intersection) {
        return calculateIntersection(intersection);
    }

    public static List<Interval> getIntersectionIntervals(List<Intersection> intersections) {
        List<Interval> intersectionIntervals = new ArrayList<>();
        for (Intersection intersection : intersections) {
            calculateIntersection(intersection);
        }

        return intersectionIntervals;
    }

    private static Interval calculateIntersection(Intersection intersection) {
        ZonedDateTime mFrom;
        ZonedDateTime mTo;
        ZonedDateTime fTmFrom = intersection.mFirst.getmFrom();
        ZonedDateTime secTmFrom = intersection.mSecond.getmFrom();

        if (fTmFrom.isBefore(secTmFrom)) {
            mFrom = secTmFrom;
        } else {
            mFrom = fTmFrom;
        }

        ZonedDateTime fTmTo = intersection.mFirst.getmTo();
        ZonedDateTime secTmTo = intersection.mSecond.getmTo();

        if (fTmTo.isAfter(secTmTo)) {
            mTo = secTmTo;
        } else {
            mTo = fTmTo;
        }

        return new Interval(mFrom, mTo);
    }

    public boolean equals(Intersection another) {
        return (mFirst.equals(another.mFirst) && mSecond.equals(another.mSecond))
                || (mSecond.equals(another.mFirst) && mFirst.equals(another.mSecond));
    }

    public String toString() {
        return "[" + mFirst.toString() + " => " + mSecond.toString() + "]";
    }
}

class Interval {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");

    private final ZonedDateTime mFrom;

    private final ZonedDateTime mTo;

    public Interval(ZonedDateTime mFrom, ZonedDateTime mTo) {
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    public ZonedDateTime getmFrom() {
        return mFrom;
    }

    public ZonedDateTime getmTo() {
        return mTo;
    }

    public Interval(String from, String to) {
        if (from == null || from.strip().equals("")) {
            mFrom = LocalDateTime.MIN.atZone(ZoneId.systemDefault());
        } else {
            mFrom = ZonedDateTime.parse(from, DATE_FORMAT);
        }

        if (to == null || to.strip().equals("")) {
            mTo = LocalDateTime.MAX.atZone(ZoneId.systemDefault());
        } else {
            mTo = ZonedDateTime.parse(to, DATE_FORMAT);
        }
    }

    public boolean equals(Interval another) {
        return mFrom.equals(another.mFrom) && mTo.equals(another.mTo);
    }

    /**
     * проверка на то, пересекаются ли интервалы
     */
    public boolean hasIntersection(Interval interval) {
        return mFrom.equals(interval.mFrom) ||
                (mFrom.isBefore(interval.mFrom) && mTo.isAfter(interval.mFrom)) ||
                (mFrom.isAfter(interval.mFrom) && mFrom.isBefore(interval.mTo));
    }

    public String toString() {
        return "{" + mFrom.format(DATE_FORMAT) + " -- " + mTo.format(DATE_FORMAT) + "}";
    }
}
