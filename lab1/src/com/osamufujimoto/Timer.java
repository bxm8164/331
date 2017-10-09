package com.osamufujimoto;

/**
 * Timer. Measure how much time a program takes
 * @author Osamu Fujimoto
 */
public class Timer {

    /**
     * The starting time
     */
    long startTime;

    /**
     * The elapsed time
     */
    long elapsedTime;

    /**
     * Start the time
     * @return an instance of the Timer class
     */
    public Timer start() {
        startTime = System.currentTimeMillis();
        return this;
    }

    /**
     * Stop the timer
     * @return an instance of the Timer class
     */
    public Timer stop() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return this;
    }

    /**
     * Print the elapsed time
     */
    public void print() {
        System.out.println("Elapsed time: " + elapsedTime / 1000 + " s");
    }
}
