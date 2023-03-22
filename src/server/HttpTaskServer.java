package server;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.*;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager = Manager.getFileBacked();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .serializeNulls()
            .create();


    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks", new PrioritizedTasksHandler());
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (stringPath.equals("/tasks/history/")) {
                            List<Task> history = taskManager.getHistory();
                            String response = gson.toJson(history);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у history");
                }
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (stringPath.equals("/tasks/task/")) {
                            List<Task> tasksMap = taskManager.getTasks();
                            String response = gson.toJson(tasksMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/task/?id=")) {
                            String[] id = stringPath.split("=");
                            Task task = taskManager.getTaskById(Integer.parseInt(id[1]));
                            String response = gson.toJson(task);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Task task = gson.fromJson(jsonObject, Task.class);
                        List<Task> tasks = taskManager.getTasks();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                            if (tasks.contains(task)) {
                                taskManager.updateTask(task);
                            } else {
                                taskManager.addTask(task);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case "DELETE":
                        if ("/tasks/task/".equals(stringPath)) {
                            taskManager.removeAllTasks();
                        } else {
                            if (stringPath.startsWith("/tasks/task/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeTaskById(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у task");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (stringPath.equals("/tasks/epic/")) {
                            List<Epic> epicsMap = taskManager.getEpics();
                            String response = gson.toJson(epicsMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/epic/?id=")) {
                            String[] id = stringPath.split("=");
                            Epic epic = taskManager.getEpicById(Integer.parseInt(id[1]));
                            String response = gson.toJson(epic);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Epic epic = gson.fromJson(jsonObject, Epic.class);
                        List<Epic> epicMap = taskManager.getEpics();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                            if (epicMap.contains(epic)) {
                                taskManager.updateTask(epic);
                            } else {
                                taskManager.addTask(epic);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case "DELETE":
                        if ("/tasks/epic/".equals(stringPath)) {
                            taskManager.removeAllEpics();
                        } else {
                            if (stringPath.startsWith("/tasks/epic/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeEpicById(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у epic");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (stringPath.equals("/tasks/subtask/")) {
                            List<Subtask> subTaskMap = taskManager.getSubtasks();
                            String response = gson.toJson(subTaskMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/subtask/?id=")) {
                            String[] id = stringPath.split("=");
                            Subtask subTask = taskManager.getSubtaskById(Integer.parseInt(id[1]));
                            String response = gson.toJson(subTask);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/subtask/epic?id=")) {
                            String[] id = stringPath.split("=");
                            List<Subtask> listSubTasks = taskManager.getSubtasksOfEpicById(Integer.parseInt(id[1]));
                            String response = gson.toJson(listSubTasks);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Subtask subTask = gson.fromJson(jsonObject, Subtask.class);
                        List<Subtask> subTaskMap = taskManager.getSubtasks();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/subtask/")) {
                            if (subTaskMap.contains(subTask)) {
                                taskManager.updateSubtask(subTask);
                            } else {
                                taskManager.addSubtask(subTask);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case "DELETE":
                        if ("/tasks/subtask/".equals(stringPath)) {
                            taskManager.removeAllSubtasks();
                        } else {
                            if (stringPath.startsWith("/tasks/subtask/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeSubtaskById(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у epic");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (stringPath.equals("/tasks/")) {
                            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                            String response = gson.toJson(prioritizedTasks);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у task");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    public void startServer() {
        httpServer.start();
    }

    public void stopServer(int delay) {
        httpServer.stop(delay);
    }

    public TaskManager getFileBackedTasksManager() {
        return taskManager;
    }
}