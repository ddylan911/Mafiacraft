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
package net.voxton.mafiacraft.core.task;

import java.util.Date;

/**
 * Represents a registered task.
 */
public class RegisteredTask {

    private final String name;

    private final Task task;

    private final TaskSchedule schedule;

    /**
     * Creates a new registered task.
     *
     * @param name The name of the task.
     * @param task The task to register.
     * @param schedule The schedule of the task.
     */
    public RegisteredTask(String name, Task task, TaskSchedule schedule) {
        this.name = name;
        this.task = task;
        this.schedule = schedule;
    }

    /**
     * Gets the name of the task.
     *
     * @return The name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the schedule of the task.
     *
     * @return The schedule of the task.
     */
    public TaskSchedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns true if this should run at the given date..
     *
     * @return True if this should run.
     */
    public boolean shouldRun(Date date) {
//        return schedule.pertainsTo()
        return false;
    }

}
