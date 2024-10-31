public class Singel_Inheritance {
    static class Cat{
        public String eat;
        public String color;
        public int age;

        public Cat(String eat, String color, int age) {
            this.eat = eat;
            this.color = color;
            this.age = age;
        }

        public void makan() {
            System.out.println("Kucing makan:" + this.eat);
        }

        public void warna() {
            System.out.println("Kucing warna:" + this.color);
        }

        public void umur() {
            System.out.println("Kucing umur:" + this.age);
        }
    }
    
    static class Anggora extends Cat{
        public String name;

        public Anggora(String eat, String color, int age, String name) {
            super(eat, color, age);
            this.name = name;
        }

        public void nama() {
            System.out.println("Kucing nama:" + this.name);
        }

        public String cetak() {
            return ("Nama Kucing:" + name +
                    "Umur Kucing:" + age +
                    "Warna Kucing:" + color +
                    "Makanan Kucing:" + eat);
        }
    }
    public static  void main(String[] args) {
        // Singel_Inheritance ins = new Singel_Inheritance();
        Anggora rara = new Anggora("Wishkas", "Hitam Putih", 2, "Rara");
        System.out.println(rara.cetak());
    }
    
}
