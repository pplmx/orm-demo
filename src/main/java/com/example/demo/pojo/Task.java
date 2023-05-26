package com.example.demo.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    @ToString.Exclude
    private Workflow workflow;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "task_dependencies",
        joinColumns = @JoinColumn(name = "next_task_id"),
        inverseJoinColumns = @JoinColumn(name = "prev_task_id"))
    @ToString.Exclude
    private List<Task> prevTasks = new ArrayList<>();

    @ManyToMany(mappedBy = "prevTasks", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ToString.Exclude
    private List<Task> nextTasks = new ArrayList<>();
}
