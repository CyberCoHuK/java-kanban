package server;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;

    HttpClient client = HttpClient.newHttpClient();
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();
    TaskManager manager;


    @BeforeEach
    void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
        manager = httpTaskServer.getFileBackedTasksManager();
        Task task1 = new Task("Задача 1", "Старое описание",
                LocalDateTime.of(2001, 1, 2, 0, 0),
                Duration.ofDays(3));
        Task task2 = new Task("Задача 1", "Старое описание",
                LocalDateTime.of(2000, 2, 2, 0, 0),
                Duration.ofDays(3));
        Epic epic1 = new Epic("Epic1", "Epic description1");
        Epic epic2 = new Epic("Epic2", "Epic description1");
        Subtask subtask1 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                3,
                LocalDateTime.of(2000, 1, 6, 0, 0),
                Duration.ofDays(1));
        Subtask subtask2 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                3,
                LocalDateTime.of(2000, 1, 8, 0, 0),
                Duration.ofDays(1));
        Subtask subtask3 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                3,
                LocalDateTime.of(2000, 1, 12, 0, 0),
                Duration.ofDays(1));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }


    @AfterEach
    void afterEach() {
        httpTaskServer.stopServer(0);
    }

    @Test
    void deleteTaskAll() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(0, manager.getTasks().size(), "Получен не верный Json с задачами");
    }

    @Test
    void deleteTaskByIdCheck() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(1, manager.getTasks().size(), "Получен не верный Json с задачами");
    }

    @Test
    void getTaskByIdCheck() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getTaskById(1));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getEpicByIdCheck() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getEpicById(3));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getSubtaskByIdCheck() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtaskById(5));
        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getAllTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getTasks());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getEpics());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getAllSubTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtasks());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getPriorList() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getPrioritizedTasks());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getHistoryList() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getHistory());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void getSubtasksOfEpicList() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtasksOfEpicById(3));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void postTaskNew() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task newTask = new Task("Задача 3", "проверка",
                LocalDateTime.of(2023, 1, 2, 0, 0),
                Duration.ofDays(3));
        newTask.setId(8);
        String json = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String actual = gson.toJson(manager.getTaskById(8));

        assertEquals(json, actual, "Получен не верный Json с задачами");
    }

    @Test
    void postTaskUpdate() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task newTask = new Task("Задача 1", "Старое описание",
                LocalDateTime.of(2000, 2, 2, 0, 0),
                Duration.ofDays(3));
        newTask.setId(2);
        newTask.setStatus(Status.IN_PROGRESS);
        String json = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String actual = gson.toJson(manager.getTaskById(2));

        assertEquals(json, actual, "Получен не верный Json с задачами");
    }
}