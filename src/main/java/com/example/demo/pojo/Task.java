package com.example.demo.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    private Workflow workflow;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "task_dependencies",
            joinColumns = @JoinColumn(name = "next_task_id"),
            inverseJoinColumns = @JoinColumn(name = "prev_task_id"))
    private List<Task> prevTasks;

    @ManyToMany(mappedBy = "prevTasks")
    private List<Task> nextTasks;
}
