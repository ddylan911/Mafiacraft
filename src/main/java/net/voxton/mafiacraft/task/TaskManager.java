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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.voxton.mafiacraft.MafiacraftCore;

/**
 *
 * @author ianschool
 */
public abstract class TaskManager {
    /**
     * The tasks.
     */
    private Map<String, RegisteredTask> tasks =
            new HashMap<String, RegisteredTask>();

    /**
     * An unnecessary but line-number increasing reference to the task checker.
     */
    protected TaskChecker taskChecker;

    public TaskManager() {
        setupTaskChecker();
    }
    
    protected abstract void setupTaskChecker();

    /**
     * Gets a list of all tasks that are due.
     *
     * @return The list of due tasks.
     */
    public List<RegisteredTask> getDueTasks() {
        List<RegisteredTask> taskList =
                new ArrayList<RegisteredTask>();
        for (RegisteredTask rt : tasks.values()) {
            if (rt.shouldRun(null)) {
                taskList.add(rt);
            }
        }
        return taskList;
    }

    /**
     * Registers a task with the task manager.
     *
     * @param name The name of the task.
     * @param task The task.
     * @param schedule The schedule in which the task should be completed.
     *      See {@link TaskSchedule.fromCron} for more info.
     * @return This TaskManager.
     */
    public TaskManager registerTask(String name, Task task, String schedule) {
        return registerTask(name, task, TaskSchedule.fromCronString(schedule));
    }

    /**
     * Registers a task with the task manager.
     *
     * @param name The name of the task.
     * @param task The task.
     * @param schedule The schedule in which the task should be completed.
     * @return This TaskManager.
     */
    public TaskManager registerTask(String name, Task task,
            TaskSchedule schedule) {
        RegisteredTask rt = new RegisteredTask(name, task, schedule);
        tasks.put(rt.getName(), rt);
        return this;
    }

}
