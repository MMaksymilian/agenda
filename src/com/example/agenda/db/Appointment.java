package com.example.agenda.db;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Maksymilian
 * Date: 22.01.13
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
public class Appointment implements Serializable {

    public enum AppointmenType {
        MEETING, SPORT_EVENT, MUSIC_EVENT, TREATMENT
    }

    public static final String KEY_ID = "appointment_id";
    public static final String KEY_DATE = "appointment_date";
    public static final String KEY_HOUR = "appointment_hour";
    public static final String KEY_APPOINTMENT_TYPE = "appointment_type";
    public static final String KEY_APPOINTMENT_AIM = "appointment_aim";

    long _id;
    long date;
    long hour;
    long appointmentType;
    String aim;

    public Appointment(long date, long hour, long appointment, String aim) {
        this._id = _id;
        this.date = date;
        this.hour = hour;
        this.appointmentType = appointment;
        this.aim = aim;
    }

    public Appointment(long _id, long date, long hour, long appointment, String aim) {
        this._id = _id;
        this.date = date;
        this.hour = hour;
        this.appointmentType = appointment;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getAppointment() {
        return appointmentType;
    }

    public void setAppointment(long appointment) {
        this.appointmentType = appointment;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }
}

