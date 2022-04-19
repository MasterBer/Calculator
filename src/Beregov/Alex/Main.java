package Beregov.Alex;

// Ваша Честь! В соответствии с п.3 поставленной задачи, к обработке будут допускаться ТОЛЬКО
// арабские и римские числа от 1 до 10. Например, исходя из данного пункта, число 0 - не допустимо.
// привожу весь пункт целиком:
// "3.Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более.
//  На выходе числа не ограничиваются по величине и могут быть любыми."

// Список обрабатываемых исключений (перечень того, в каких случаях программа должна остановиться и заявить,
// что это именно этот случай))):
//1. В римской системе получился отрицательный результат.
//2. Используются разные системы счисления.
//3. Строка не является математической операцией.
//4. Формат математической операции не удовлетворяет заданию - два операнда и один оператор (более одного оператора в строке)



import javax.swing.*;
import java.io.*;
import java.lang.NumberFormatException;
import java.util.*;
public class Main {

    public static void main(String[] args) throws IOException {

        // Весь следующий абзац реализует ввод строки с клавиатуры в переменную name
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String name = bufferedReader.readLine(); //читаем строку с клавиатуры
        // закончили считывать строку с клавиатуры)))

        String r = calc(name); //Содзаём переменную, в которую получим конечное значение нашего метода
        System.out.println(r);
    }

    public static String calc(String input) throws EOFException {

        int index = 0;
        int FinalResult = 0;
        String inputInUpperCase = input.toUpperCase();

        inputInUpperCase = inputInUpperCase.replace(" ","");

          index = OperandPlaceNumber(inputInUpperCase); //Получаем порядковый номер операнда в строке

        // Если ни один из операндов не найден в строке, вызываем исключение
        if (index == -1) throw new EOFException("\n" +
                "Attention!\n" +
                "Exception #3: \"String is not a mathematical operation.\"");

        String a = inputInUpperCase.substring(0, index);
        String b = inputInUpperCase.substring(index + 1);
        String OperAND = inputInUpperCase.substring(index,index+1);

        //Сразу проверим, - а не содержится ли в оставшейся строчке после операнда - ещё какой-либо операнд?
        // По условиям ТЗ, мы должны этот момент отследить и выдать исключение.

        index = OperandPlaceNumber(b);

        if (index != -1) throw new EOFException("\n" +
                "Attention!\n" +
                "Exception #4: \"The format of the mathematical operation does not satisfy the task - two operands and one operator (more than one operator per line)\"");


        int numberA, numberB = -1;
        boolean isRim = false;

        // Cначала проверяем числa на принадлежность к Риму, в случае успеха - сразу получаем число в int

        numberA = TranslateFromRomulus (a);
        numberB = TranslateFromRomulus (b);
        if (numberA != -1) isRim = true;

        //Если одно число арабское, а другое - римское, мы должны это засечь и выдать исключение. Есть идея....
        if (numberA * numberB < 0) {
            throw new EOFException("\n" +
                    "Attention!\n" +
                    "Exception #2: \"Different number systems are used.\"");
        }


        //Теперь, если числа оказались не римскими. Нужно проверить, что они целые, и что они попадают в диапазон
        // от 1 до 10. Ноль, к сожалению, не входит.

        if (isRim == false) {
            numberA = TranslateToInteger(a);
            numberB = TranslateToInteger(b);
        }

        if (OperAND.equals("+")) {
            FinalResult = numberA + numberB;
        }
        if (OperAND.equals("-")) {
            FinalResult = numberA - numberB;
            if (isRim == true & FinalResult < 1) {
                throw new EOFException("\n" +
                        "Attention!\n" +
                        "Exception #1: \"Roman numbers can only be in the positive range.\"");
            }
        }

        if (OperAND.equals("*")) {
            FinalResult = numberA * numberB;
        }

        if (OperAND.equals("/")) {
            FinalResult = numberA / numberB;
            if (isRim == true & FinalResult < 1) {
                throw new EOFException("\n" +
                        "Attention!\n" +
                        "Exception #1: \"Roman numbers can only be in the positive range.\"");
            }
        }

        String temp;

        if (isRim) {
            temp = Translate2Romulus(FinalResult);
                  }
        else {
            temp = Integer. toString(FinalResult);
        }

        return temp;
    }

    // Метод получает строку, а возвращает уже число из неё, ну или исключение, если с ней что-то не так))
        static int TranslateToInteger (String a) throws EOFException {

        int numberX = 0;

            try {
                numberX = Integer.parseInt(a); //преобразуем строку в число наконец))
            } catch (NumberFormatException e) {
                System.out.println("Attention! The expression string contains invalid characters. Please try again.");
                System.exit(0); //Если строка преобразуется в число с ошибками, значит у строки проблема, отказать)
            }
        if (numberX < 1 || numberX > 10) {
            throw new EOFException("\n" +
                    "\n" +
                    "Attention!\n" +
                    "The number must be between 1 and 10 inclusive.");
        }
        return numberX;
    }

    //Метод получает арабское число от 1 до 100 включительно, переводит его в Рим и возвращает в строке
    static String Translate2Romulus (int ArabNumber){
        String dozens[] = {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC","C"};
        String units[] = {"","I","II","III","IV","V","VI","VII","VIII","IX","X"};

        if (ArabNumber == 100) return dozens[10];

        String ResultInRome = dozens[ArabNumber/10]+units[ArabNumber%10];
      return ResultInRome;
          }


    // Метод получает строку, а возвращает арабскую цифру от 1 до 10, или -1 если в строке не Рим.
    static int TranslateFromRomulus(String a) {
        String[] rim = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        int numberХ = -1;

        for (int i = 0; i < 10; i++) {
            if (a.equals(rim[i])) {
                numberХ = i + 1;
            }
        }
        return numberХ;
    }



    // Метод получает строку, а возвращает позицию операнда в строке
    static int OperandPlaceNumber(String input) {
        String[] operand = {"*", "/", "+", "-"};
        int index = -1;

        //Проверяем на наличие операнда, а также сразу запоминаем его позицию в строке
        for (int i = 0; i < 4; i++) {
            index = input.indexOf(operand[i]);
            if (index!=-1) break;
        }
        return index;
    }
}

