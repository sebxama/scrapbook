package fcalib.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility Interface, which can be used to set
 * a time stamp, get the execution time of methods
 * and convert nanoseconds to a given Format.
 * @author Leon Geis
 */
public interface Performance {

    /**
     * Set a time stamp.
     * @return Current Value of the System Timer in nanoseconds.
     */
    static long setTimeStamp(){
        return System.nanoTime();
    }

    /**
     * Computes the Execution Time of two timestamps.
     * @param firstTimeStamp Nanoseconds of the first Time Stamp.
     * @param secondTimeStamp Nanoseconds of the second Time Stamp.
     * @return If the second Time Stamp is bigger than the first 0, else
     * the time difference in nanosecond range.
     */
    static long getExecutionTime(long firstTimeStamp, long secondTimeStamp){
        //First check if firstTimeStamp is greater or equal than secondTimeStamp
        if(firstTimeStamp>=secondTimeStamp){
            //If it is return 0
            return 0;
        }
        else{
            //Return Execution Time
            return secondTimeStamp-firstTimeStamp;
        }
    }

    /**
     * Converts the given nanoseconds in Minutes, Seconds and
     * Milliseconds (mm:ss:SSS).
     * @param elapsedTime The nanoseconds, which should be converted.
     * @return String in the format (mm:ss:SSS).
     */
    static String convertToFormat(long elapsedTime){
        //Get the milli seconds
        long millis = (long) (elapsedTime/1e+6);
        //Return formatted String mm:ss:SSS
        return (new SimpleDateFormat("mm:ss:SSS").format(new Date(millis)));
    }

}
