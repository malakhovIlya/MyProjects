package main;

import main.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ListTasks {

    private static ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>();
    private static AtomicInteger idTasks = new AtomicInteger(1);

    public static List<Task> getAllTasks() {
        List<Task> listTasks = new ArrayList<>(tasks.values());
        return listTasks;
    }

    public static Task getTask(Task task) {
        return tasks.get(task.getId());
    }


    public static int addTask(Task task) {
        task.setId(idTasks.getAndIncrement());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public static void deleteTask(Task task) {
        tasks.remove(task.getId());
    }

    public static String putTask(Task task) {

        if (tasks.computeIfPresent(task.getId(), (a, b) -> task) == null) {
            return "false";
        } else {
            return "true";
        }


    }
}
