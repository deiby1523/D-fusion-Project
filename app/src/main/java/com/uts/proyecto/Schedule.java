package com.uts.proyecto;

import java.util.List;

public class Schedule {
    private List<ScheduleDay> days;

    public Schedule() {}

    public Schedule(List<ScheduleDay> days) {
        this.days = days;
    }

    public List<ScheduleDay> getDays() {
        return days;
    }

    public void setDays(List<ScheduleDay> days) {
        this.days = days;
    }
}

