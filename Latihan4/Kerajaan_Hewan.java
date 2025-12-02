import java.util.ArrayList;

public class Kerajaan_Hewan {
    interface Politician {
        public String speak();
        public String proposePolicy();
    }

    static abstract class Pet {
        String name;
        int age;
        String species;

        public Pet(String name, int age, String species) {
            this.name = name;
            this.age = age;
            this.species = species;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getSpecies() {
            return species;
        }
    }

    static class CatMinister extends Pet implements Politician {

        public CatMinister() {
            super("Kitty", 40, "British Short Hair");
        }

        @Override
        public String speak() {
            return "Hello, Cats";
        }

        @Override
        public String proposePolicy() {
            return "Bring Jerry to me";
        }
        
    }

    static class DogGovernor extends Pet implements Politician {

        public DogGovernor() {
            super("Doggy", 55, "Dobermann");
        }
        
        @Override
        public String speak() {
            return "Hello, Dogs";
        }

        @Override
        public String proposePolicy() {
            return "Protect your Master";
        }
    }

    static class BirdSenator extends Pet implements Politician {

        public BirdSenator() {
            super("Kako", 49, "Parrot");
        }
        
        @Override
        public String speak() {
            return "Hello, Birds";
        }

        @Override
        public String proposePolicy() {
            return "Eat fruits";
        }
    }

    static class Council {
        ArrayList<Politician> members = new ArrayList<>();

        public void addMember(Politician p) {
            members.add(p);
            System.out.println("added to council: " + p);
        }

        public void startCouncilMeeting() {
            for(Politician p : members) {
                System.out.println("Speak: " + p.speak());
                System.out.println("Policy: " + p.proposePolicy());
            }
        }
    }

    public static void main(String[] args) {
        CatMinister catMinister = new CatMinister();
        DogGovernor dogGovernor = new DogGovernor();
        BirdSenator birdSenator = new BirdSenator();
        Council council = new Council();

        council.addMember(birdSenator);
        council.addMember(catMinister);
        council.addMember(dogGovernor);

        council.startCouncilMeeting();
    }
}