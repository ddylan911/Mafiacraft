/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.task;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * The task schedule. Let's hope it doesn't use up much ram.
 */
public class TaskSchedule {

    private static final Pattern generalPattern = Pattern.compile("");

    /**
     * Regex pattern that fits minutes.
     */
    private final Pattern minutes;

    /**
     * Regex pattern that fits hours.
     */
    private final Pattern hours;

    /**
     * Regex pattern that fits days.
     */
    private final Pattern days;

    /**
     * Constructor that uses regex strings.
     * 
     * @param minutes The minute pattern.
     * @param hours The hour pattern.
     * @param days The day pattern.
     */
    public TaskSchedule(String minutes, String hours, String days) {
        this(Pattern.compile(minutes), Pattern.compile(hours), Pattern.compile(
                days));
    }

    /**
     * Constructor that uses patterns.
     * 
     * @param minutes The minute pattern.
     * @param hours The hour pattern.
     * @param days The day pattern.
     */
    public TaskSchedule(Pattern minutes, Pattern hours, Pattern days) {
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
    }

    /**
     * Returns true if the value fits the minute.
     * 
     * @param minute The minute to check.
     * @return True if the value fits the minute.
     */
    public boolean fitsMinute(int minute) {
        return minutes.matcher(Integer.toString(minute)).matches();
    }

    /**
     * Returns true if the value fits the hour.
     * 
     * @param minute The hour to check.
     * @return True if the value fits the hour.
     */
    public boolean fitsHours(int hour) {
        return hours.matcher(Integer.toString(hour)).matches();
    }

    /**
     * Returns true if the value fits the day.
     * 
     * @param minute The day to check.
     * @return True if the value fits the day.
     */
    public boolean fitsDays(int day) {
        return days.matcher(Integer.toString(day)).matches();
    }
    
    public boolean fitsTime() {
        //TODO get JODA time!
        return false;
    }
    
    /**
     * Gets a schedule from its cron string.
     * 
     * <p>Cron strings as defined for our purposes are formatted as follows:</p>
     * <pre>#minute #hour #day</pre>
     * 
     * <p>Here is an example of a cron string that is scheduled to run once per 15 minutes:</p>
     * <pre>0,15,30,45 * *</pre>
     * 
     * <p>The stars mean that it doesn't matter what those are. As long as the 
     * minutes are at 0, 15, 30, or 45 at the time, then the script will run.
     * It would suck if you reloaded at that time... Now another one:</p>
     * <pre>0-15,30,45-90 * 2-6</pre>
     * 
     * <p>That cron string would run every time from 0 to 15 minutes (16 times),
     * run at 30, then run at 45-59. There is no '60th' or '90th' minute or
     * anything in between, so those are to be ignored. The string would also 
     * run every day from Monday to Friday, because there are 7 days in a week.
     * Here's another example, because I love writing specifications:</p>
     * <pre>* 0 *</pre>
     * 
     * <p>This string would run on the first hour of each day at midnight.
     * There are 24 hours in a day. So that's it, pretty simple eh?</p>
     * 
     * @param schedule The string schedule as specified above.
     * @return The TaskSchedule parsed from this.
     */
    public static TaskSchedule fromCronString(String schedule) {
        //TODO: do this awesome thing!
        return null;
    }

}
