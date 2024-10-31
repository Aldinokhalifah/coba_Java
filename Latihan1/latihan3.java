public class latihan3 {
    public static void main(String[] args) {
        int age = 10;
        

        if(age < 20) {
            System.out.println("Age is not old enough");
        } else{
            System.out.println("Age is old enough");
        }

        for(int i = 0; i <= 10; i++) {
            System.out.println("Angka ke-" + i);
        }

        int day = 4;
        switch (day) {
            case 1 -> System.out.println("Monday");
            case 2 -> System.out.println("Tuesday");
            case 3 -> System.out.println("Wednesday");
            case 4 -> System.out.println("Thursday");
            case 5 -> System.out.println("Friday");
            case 6 -> System.out.println("Saturday");
            case 7 -> System.out.println("Sunday");
        }
    }
}
