package main;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TasksController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(value = "/tasks/")
    public List<Task> list() {
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskIterable) {
            tasks.add(task);
        }
        return tasks;
    }

    @PostMapping(value = "/tasks/")
    public int add(@Valid Task task) {
        Task newTask = taskRepository.save(task);
        return newTask.getId();
    }

    @DeleteMapping(value = "tasks/{id}")
    public void remove(@PathVariable int id) {
        taskRepository.deleteById(id);
    }

    @PutMapping(value = "tasks/{id}")
    public void put(Task task) {
        taskRepository.save(task);
    }

    @GetMapping(value = "tasks/{id}")
    public ResponseEntity getTask(@PathVariable int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(optionalTask.get(), HttpStatus.OK);
    }
}
