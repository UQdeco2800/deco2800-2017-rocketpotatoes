package com.deco2800.potatoes.time;

public class Time {
	
	// instance variables
	private int hour;
	private double minute;
	private double time = hour + minute;
	
	/*
	 * invariant:
	 * 
	 * h  >= 0 && h < 24 && m >= .00 && m < 60
	 */
	
	/**
	 * Create a new Time object
	 * @param h hour
	 * @param m minute
	 */
	public Time(int h, double m) {
		setHour(h);
		setMinute(m);
	}
	
	/**
	 * Return the hour
	 * @return hour
	 */
	public int getHour() {
		return hour;
	}
	
	/**
	 * Return the minute
	 * @return minute
	 */
	public double getMinute() {
		return minute;
	}
	
	/**
	 * Return the time (hour + minute)
	 * @return time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Set the hour
	 * @param h hour
	 */
	public void setHour(int h) {
		if(!(h >= 0 && h < 24)) {
			throw new IllegalArgumentException(
					"int must be between 0-23");
		}
		hour = h;
	}
	
	/**
	 * Set the minute
	 * @param m minute
	 */
	public void setMinute(double m) {
		if (!(m >= .00 && m < 60)) {
			throw new IllegalArgumentException(
					"double must be between .00-.59");
		}
		minute = m;
	}
	
	/**
	 * Set the time (hour + minute)
	 * @param h hour
	 * @param m minute
	 */
	public void setTime(int h, double m) {
		setHour(h);
		setMinute(m);
	}
	
	/**
	 * Reset the time
	 * @ensures hour = 0
	 * @ensures minute = .00
	 */
	public void resetTime() {
		hour = 0;
		minute = .00;
	}
}