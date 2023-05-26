package com.example.demo;

import com.example.demo.pojo.Project;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.Workflow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
class DemoApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testDeleteProject() {
        // create a new project with associated workflows and tasks
        Project project = new Project();
        project.setName("Test Project");

        Workflow workflow = new Workflow();
        workflow.setName("Test Workflow");
        workflow.setProject(project);
        project.getWorkflows().add(workflow);

        Task task = new Task();
        task.setName("Test Task");
        task.setWorkflow(workflow);
        workflow.getTasks().add(task);

        Task nextTask = new Task();
        nextTask.setName("Next Task");
        nextTask.setWorkflow(workflow);
        workflow.getTasks().add(nextTask);
        nextTask.getPrevTasks().add(task);
        task.getNextTasks().add(nextTask);

        // persist the project and its associated workflows and tasks
        entityManager.persist(project);
        entityManager.flush();
        entityManager.clear();

        // verify that the project and its workflows and tasks have been created
        assertNotNull(entityManager.find(Project.class, project.getId()));
        assertNotNull(entityManager.find(Workflow.class, workflow.getId()));
        assertNotNull(entityManager.find(Task.class, task.getId()));
        assertNotNull(entityManager.find(Task.class, nextTask.getId()));

        // delete the project
        Project managedProject = entityManager.find(Project.class, project.getId());
        entityManager.remove(managedProject);
        entityManager.flush();
        entityManager.clear();

        // verify that the project and its workflows and tasks have been deleted
        assertNull(entityManager.find(Project.class, project.getId()));
        assertNull(entityManager.find(Workflow.class, workflow.getId()));
        assertNull(entityManager.find(Task.class, task.getId()));
        assertNull(entityManager.find(Task.class, nextTask.getId()));
    }

    @Test
    void testDeleteWorkflow() {
        // create a new workflow with associated tasks
        Workflow workflow = new Workflow();
        workflow.setName("Test Workflow");

        Task task = new Task();
        task.setName("Test Task");
        task.setWorkflow(workflow);
        workflow.getTasks().add(task);

        Task nextTask = new Task();
        nextTask.setName("Next Task");
        nextTask.setWorkflow(workflow);
        workflow.getTasks().add(nextTask);
        nextTask.getPrevTasks().add(task);
        task.getNextTasks().add(nextTask);

        // persist the workflow and its associated tasks
        entityManager.persist(workflow);
        entityManager.flush();
        entityManager.clear();

        // verify that the workflow and its tasks have been created
        assertNotNull(entityManager.find(Workflow.class, workflow.getId()));
        assertNotNull(entityManager.find(Task.class, task.getId()));
        assertNotNull(entityManager.find(Task.class, nextTask.getId()));

        // delete the workflow
        Workflow managedWorkflow = entityManager.find(Workflow.class, workflow.getId());
        entityManager.remove(managedWorkflow);
        entityManager.flush();
        entityManager.clear();

        // verify that the workflow and its tasks have been deleted
        assertNull(entityManager.find(Workflow.class, workflow.getId()));
        assertNull(entityManager.find(Task.class, task.getId()));
        assertNull(entityManager.find(Task.class, nextTask.getId()));
    }

    @Test
    void testDeleteTask() {
        // create a new task with associated prevTasks and nextTasks
        Task task = new Task();
        task.setName("Test Task");

        Task prevTask = new Task();
        prevTask.setName("Prev Task");
        prevTask.getNextTasks().add(task);
        task.getPrevTasks().add(prevTask);

        Task nextTask = new Task();
        nextTask.setName("Next Task");
        nextTask.getPrevTasks().add(task);
        task.getNextTasks().add(nextTask);

        // persist the task and its associated prevTasks and nextTasks
        entityManager.persist(task);
        entityManager.flush();
        entityManager.clear();

        // verify that the task and its prevTasks and nextTasks have been created
        assertNotNull(entityManager.find(Task.class, task.getId()));
        assertNotNull(entityManager.find(Task.class, prevTask.getId()));
        assertNotNull(entityManager.find(Task.class, nextTask.getId()));

        // delete the task
        Task managedTask = entityManager.find(Task.class, task.getId());
        entityManager.remove(managedTask);
        entityManager.flush();
        entityManager.clear();

        // verify that the task and its nextTasks have been deleted,
        // but its prevTasks have not been deleted
        assertNull(entityManager.find(Task.class, task.getId()));
        assertNotNull(entityManager.find(Task.class, prevTask.getId()));
        assertNull(entityManager.find(Task.class, nextTask.getId()));
    }
}
