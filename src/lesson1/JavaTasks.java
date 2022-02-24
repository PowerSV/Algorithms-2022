package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;


@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */



    // Трудоемкость O(n * log n)
    // Ресурсоемкость O(n)
    static public void sortTimes(String inputName, String outputName) throws IOException {
        // создаем массив, в котором будем хранить время в секундах
        ArrayList<Integer> secondsArr = new ArrayList<>();      // R = O(n)
        File inputFile = new File(inputName);
        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        String line = br.readLine();    // в этой строке хранится очередная строка из файла

        // заполняем массив
        // T = O(n), где n - кол-во строк в файле
        // проходим по всем строка: переводим в секунды -> добавляем в массив -> читаем следующую строку
        while (line != null) {
            if (!line.matches("^\\d{2}:\\d{2}:\\d{2}\\s([AP])M$")) {
                throw new IllegalArgumentException("Неверный формат данных");
            } else {
                secondsArr.add(stringToSec(line));
            }
            line = br.readLine();
        }
        br.close();

        // сортируем массив, используется алгоритм сортировки слияниями.
                                            // T = O (n  * log n)
        Collections.sort(secondsArr);       // R = O(n)

        // создаем объект - выходной файл
        File outputFile = new File(outputName);
        if (!outputFile.exists())           // если такой файл не найден, создаем его
            outputFile.createNewFile();

        // T = O(n)
        PrintWriter writer = new PrintWriter(outputFile);
        for (int time : secondsArr) {
            writer.println(secToString(time));  // печатаем в выходной файл время, преобразованное в строку
        }
        writer.close();
    }

    // Трудоемкость Т = O(1)
    // Перевод строки в секунды
    static private int stringToSec(String str) {
        // парсинг строки
        int hours = Integer.parseInt(str.substring(0, 2));
        int minutes = Integer.parseInt(str.substring(3,5));
        int seconds = Integer.parseInt(str.substring(6,8));

        // если время после полудня, то прибавляем к часам 12
        if (str.contains("PM") && hours < 12) hours += 12;
        // если 12 АМ => 00.00
        if (str.contains("AM") && hours == 12) hours = 0;
        // проверка входных данных
        if (hours > 23 || minutes > 59 || seconds > 59)
            throw new IllegalArgumentException("Неверный формат данных");
        return hours * 3600 + minutes * 60 + seconds;
    }
    // Трудоемкость Т = O(1)
    // Перевод секунд в строку
    static private String secToString(Integer time) {
        int hours = time / 3600;
        int minutes = time % 3600 / 60;
        int seconds = time % 3600 % 60;
        String ampm;

        if (hours >= 12) {
            if (hours > 12) hours -= 12; // 12 PM == 12.00
            ampm = "PM";
        } else {
            if (hours == 0) hours = 12;  // 12 АМ == 00.00
            ampm = "AM";
        }
        // используется DecimalFormat для вывода типа: 01 или 03 и т.п.
        DecimalFormat df = new DecimalFormat("00");
        return df.format(hours) +
                ":" +
                df.format(minutes) +
                ":" +
                df.format(seconds) +
                " " +
                ampm;
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */

    // Трудоемкость O(n * log n)
    // Ресурсоемкость O(n)
    static public void sortSequence(String inputName, String outputName) throws IOException {
        File inputFile = new File(inputName);
        ArrayList<Integer> numbers = new ArrayList<>(); // сюда запишем все числа
        // Считываем файл, заполняем массив числами
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line = br.readLine();    // в этой строке хранится очередная строка из файла
        // T = O(n), n - кол-во строк в файле
        while (line != null) {
            if (!line.isEmpty()) {
                numbers.add(Integer.parseInt(line));
            }
            line = br.readLine();
        }
        br.close();

        // создаем объект - выходной файл
        File outputFile = new File(outputName);
        if (!outputFile.exists())           // если такой файл не найден, создаем его
            outputFile.createNewFile();

        if (!numbers.isEmpty()) {
            ArrayList<Integer> outArray = new ArrayList<>(numbers); // копируем массив для вывода

            // сортируем массив, используется алгоритм сортировки слияниями.
            // T = O (n  * log n)
            Collections.sort(numbers);      // R = O(n)

            // находим число, которое встречается в этой последовательности наибольшее количество раз
            // T = O(n)
            int max = 1;
            int count = 1;
            int element = numbers.get(0);
            for (int i = 0; i < numbers.size() - 1; i++) {
                if (numbers.get(i).equals(numbers.get(i + 1))) {
                    count++;
                } else count = 1;
                if (count > max) {
                    element = numbers.get(i);
                    max = count;
                }
            }

            // T = O(n)
            PrintWriter writer = new PrintWriter(outputFile);
            for (int number : outArray) {
                if (number != element) writer.println(number);
            }
            for (int i = 0; i < max; i++) {
                writer.println(element);
            }
            writer.close();
        }
    }


    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
