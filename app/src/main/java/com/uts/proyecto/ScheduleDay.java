package com.uts.proyecto;

public class ScheduleDay {
    private String day;
    private String StartHour;
    private String EndHour;

    public ScheduleDay() {}

    public ScheduleDay(String day, String startHour, String endHour) {
        this.day = day;
        this.StartHour = startHour;
        this.EndHour = endHour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartHour() {
        return StartHour;
    }

    public void setStartHour(String startHour) {
        StartHour = startHour;
    }

    public String getEndHour() {
        return EndHour;
    }

    public void setEndHour(String endHour) {
        EndHour = endHour;
    }
}
