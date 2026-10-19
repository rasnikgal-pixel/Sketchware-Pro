package dev.aldi.sayuti.block;

public class MoneyToWordsHelper {

    private static final String[] ED = {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
    private static final String[] EDF = {"", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
    private static final String[] DES = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
    private static final String[] SOT = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
    private static final String[] TEENS = {"десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
    
    private static final String[] RUB_FORMS = {"рубль", "рубля", "рублей"};
    private static final String[] KOP_FORMS = {"копейка", "копейки", "копеек"};
    private static final String[] THOUSAND_FORMS = {"тысяча", "тысячи", "тысяч"};
    private static final String[] MILLION_FORMS = {"миллион", "миллиона", "миллионов"};
    private static final String[] BILLION_FORMS = {"миллиард", "миллиарда", "миллиардов"};

    public static String toWords(double sum) {
        try {
            long rubles = (long) Math.floor(Math.abs(sum));
            int kopecks = (int) Math.round((Math.abs(sum) - rubles) * 100);
            if (kopecks >= 100) {
                rubles++;
                kopecks = 0;
            }
            StringBuilder sb = new StringBuilder();
            if (rubles == 0) {
                sb.append("ноль рублей ");
            } else {
                long r = rubles;
                if (r / 1000000000 > 0) {
                    sb.append(formatTriad(r / 1000000000, BILLION_FORMS, false)).append(" ");
                    r %= 1000000000;
                }
                if (r / 1000000 > 0) {
                    sb.append(formatTriad(r / 1000000, MILLION_FORMS, false)).append(" ");
                    r %= 1000000;
                }
                if (r / 1000 > 0) {
                    sb.append(formatTriad(r / 1000, THOUSAND_FORMS, true)).append(" ");
                    r %= 1000;
                }
                if (r > 0) {
                    sb.append(formatTriad(r, RUB_FORMS, false)).append(" ");
                }
            }
            sb.append(", ");
            if (kopecks == 0) {
                sb.append("ноль копеек");
            } else {
                sb.append(formatTriad(kopecks, KOP_FORMS, true));
            }
            // Убираем лишние пробелы и дублирующие запятые
            return sb.toString().replace(", ,", ",").replace("  ", " ").trim();
        } catch (Throwable t) {
            return "";
        }
    }

    private static String formatTriad(long n, String[] forms, boolean female) {
        StringBuilder sb = new StringBuilder();
        long n100 = n % 100;
        long n10 = n % 10;
        
        int hundreds = (int) (n / 100);
        if (hundreds > 0) {
            sb.append(SOT[hundreds]).append(" ");
        }
        
        if (n100 >= 11 && n100 <= 19) {
            sb.append(TEENS[(int) n100 - 10]).append(" ");
        } else {
            int tens = (int) (n100 / 10);
            if (tens > 0) {
                sb.append(DES[tens]).append(" ");
            }
            if (n10 > 0) {
                sb.append(female ? EDF[(int) n10] : ED[(int) n10]).append(" ");
            }
        }
        
        // Склонение
        if (n100 >= 11 && n100 <= 19) {
            sb.append(forms[2]);
        } else if (n10 == 1) {
            sb.append(forms[0]);
        } else if (n10 >= 2 && n10 <= 4) {
            sb.append(forms[1]);
        } else {
            sb.append(forms[2]);
        }
        return sb.toString();
    }
}
