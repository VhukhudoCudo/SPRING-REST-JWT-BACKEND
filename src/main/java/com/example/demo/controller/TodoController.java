package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepo;

    @GetMapping
    public List<Todo> getTodos() {
        List<Todo> todos = todoRepo.findAll();
        return todos != null ? todos : new ArrayList<>();
    }


    @PostMapping
    public Todo addTodo(@RequestBody Todo todo) {
        return todoRepo.save(todo);
    }

    @PutMapping("/{id}")
    public Todo editTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existing = todoRepo.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
        existing.setTitle(todo.getTitle());
        return todoRepo.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoRepo.deleteById(id);
        return "Deleted successfully";
    }
}
