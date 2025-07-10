package ru.netology;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        // Обработка CSV
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName); // Чтение CSV
        String json = listToJson(list); // Конвертация в JSON
        writeString(json, "data.json"); // Запись JSON в файл

        // Обработка XML
        String xmlFileName = "data.xml";
        List<Employee> xmlList = parseXML(xmlFileName); // Чтение XML
        String xmlJson = listToJson(xmlList); // Конвертация в JSON
        writeString(xmlJson, "data2.json"); // Запись JSON в файл

        // Чтение и парсинг JSON
        String new_json = readString("data.json");
        List<Employee> employees = jsonToList(new_json);

        // Вывод сотрудников в консоль
        for (Employee employee: employees) {
            System.out.println(employee);
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            // Настройка стратегии маппинга столбцов
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class); // Указываем класс для преобразования
            strategy.setColumnMapping(columnMapping); // Указываем маппинг столбцов

            // Создаем парсер CSV
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse(); // Парсим и возвращаем список
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            // Создаем фабрику и строитель DOM документов
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileName); // Парсим XML файл

            Node root = doc.getDocumentElement(); // Получаем корневой элемент
            NodeList nodeList = root.getChildNodes(); // Получаем список дочерних элементов

            // Перебираем все узлы
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                // Проверяем, что узел является элементом
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Извлекаем данные из XML
                    long id = Long.parseLong(getElementValue(element, "id"));
                    String firstName = getElementValue(element, "firstName");
                    String lastName = getElementValue(element, "lastName");
                    String country = getElementValue(element, "country");
                    int age = Integer.parseInt(getElementValue(element, "age"));

                    // Создаем объект Employee и добавляем в список
                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName); // Находим элементы по тегу
        Node node = nodeList.item(0); // Берем первый элемент
        return node.getTextContent(); // Возвращаем текстовое содержимое
    }

    // Конвертация в Json
    private static <T> String listToJson(List<T> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Создаем Gson с красивым выводом
        Type listType = new TypeToken<List<T>>() {}.getType(); // Определяем тип списка
        return gson.toJson(list, listType); // Конвертируем в JSON
    }

    // Создаем файл и записываем в него Json
    private static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json); // Записываем JSON в файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Читаем данные из файла Json
    private static String readString(String fileName) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    // Преобразуем строку Json в Employee
    private static List<Employee> jsonToList(String json) {
        Gson gson = new GsonBuilder().create();
        Type employeeListType = new TypeToken<List<Employee>>() {}.getType();
        return gson.fromJson(json, employeeListType);
    }
}