public class TestPDPL {
    public static void main(String[] args) {
        enkapsulasi obj = new enkapsulasi();

        obj.setName("Nisah");
        obj.setAge(19);
        obj.setRoll(10);


        System.out.println("Geek's name: " + obj.getName());
        System.out.println("Geek's age: " + obj.getAge());
        System.out.println("Geek's roll: " + obj.getRoll());
    }
}