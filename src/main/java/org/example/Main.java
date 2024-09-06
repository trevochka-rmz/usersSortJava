package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class Main {
    public static void main(String[] args) {

        String CSV_FILE = "users.csv";
        String url = "https://eltex-co.ru/test/users.php";

        List<User> users = fetchUsers(url);
        List<User> filteredUsers = filterAndSortUsers(users);
        writeUsersToCSV(filteredUsers, CSV_FILE);

        System.out.println("CSV файл успешно создан: " + CSV_FILE);
    }

    private static List<User> fetchUsers(String url) {
        List<User> users = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Получаем id и проверяем, является ли он пустым
                String id = jsonObject.has("id") && !jsonObject.getString("id").isEmpty()
                        ? jsonObject.getString("id")
                        : "Пустая строка";

                // Получаем name и проверяем, является ли он пустым
                String name = jsonObject.has("name") && !jsonObject.getString("name").isEmpty()
                        ? jsonObject.getString("name")
                        : "Пустая строка";

                // Получаем email и проверяем, является ли он пустым
                String email = jsonObject.has("email") && !jsonObject.getString("email").isEmpty()
                        ? jsonObject.getString("email")
                        : "Пустая строка";

                // Получаем salary с проверкой, если он не существует, устанавливаем в значение по умолчанию, например 0
                int salary = jsonObject.has("salary") ? jsonObject.getInt("salary") : 0;

                users.add(new User(id, name, email, salary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private static List<User> filterAndSortUsers(List<User> users) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getSalary() > 3500) {
                filteredUsers.add(user);
            }
        }
        filteredUsers.sort(Comparator.comparing(User::getName));
        return filteredUsers;
    }

    private static void writeUsersToCSV(List<User> users, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("ID,Name,Email,Salary\n");
            for (User user : users) {
                writer.append(user.getId()).append(",").append(user.getName()).append(",").append(user.getEmail()).append(",").append(String.valueOf(user.getSalary())).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}