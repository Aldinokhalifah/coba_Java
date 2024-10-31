public class interface_class {
    interface Animal{
        void makeSound();
    }

    static class Dog implements Animal{
        @Override
        public void makeSound() {
            System.out.println("Bark");
        }
    }

    static class Cat implements Animal{
        @Override
        public void makeSound() {
            System.out.println("Meow");
        }
    }

    public static void main(String[] args) {
        Animal dog = new Dog();
        Animal cat = new Cat();

        dog.makeSound();
        cat.makeSound();
    }
}
