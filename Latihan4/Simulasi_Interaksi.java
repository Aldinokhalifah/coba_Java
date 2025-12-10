
import java.util.ArrayList;

public class Simulasi_Interaksi {
    
    interface BehaviorModule {
        String act();
        int energyCost();
    }

    static class AggressiveBehavior implements BehaviorModule {
        
        @Override
        public String act() { 
            return "Serang Serang Serang"; 
        }
        
        @Override
        public int energyCost() { 
            return 30; 
        }
    }

    static class WiseBehavior implements BehaviorModule {
        
        @Override
        public String act() { 
            return "Berpikir sebelum bertindak"; 
        }
        
        @Override
        public int energyCost() { 
            return 25; 
        }
    }

    static class ExplorerBehavior implements BehaviorModule {
        
        @Override
        public String act() { 
            return "menjelajah tempat baru"; 
        }
        
        @Override
        public int energyCost() {
            return 20; 
        }
    }

    static abstract class Creature {
        String name;
        int energy;
        BehaviorModule behavior;

        public Creature(String name, int energy, BehaviorModule behavior) {
            this.name = name;
            this.energy = energy;
            this.behavior = behavior;
        }

        public String getName() {
            return name;
        }

        public int getEnergy() {
            return energy;
        }

        public BehaviorModule getBehavior() {
            return behavior;
        }

        public void setBehavior(BehaviorModule behavior) {
            this.behavior = behavior;
        }

        public String performAction() {
            int cost = behavior.energyCost();
            if (energy < cost) return name + " terlalu lelah untuk bertindak";
            energy -= cost;
            return name + " melakukan: " + behavior.act() + " | Energi tersisa: " + energy;
        }
    }

    static class Elf extends Creature {

        public Elf(String name, BehaviorModule behavior) {
            super(name, 85, behavior);
        }
    }

    static class Goblin extends Creature {

        public Goblin(String name, BehaviorModule behavior) {
            super(name, 77, behavior);
        }
    }

    static class Human extends Creature {

        public Human(String name, BehaviorModule behavior) {
            super(name, 81, behavior);
        }
    }

    static class WorldSimulator {
        ArrayList<Creature> listCreatures = new ArrayList<>();

        public void addCreature(Creature creature) {
            listCreatures.add(creature);
            System.out.println("Menambahkan: " + creature.getName());
        }

        public void simulate(int rounds) {
            for (int r = 1; r <= rounds; r++) {
                System.out.println("=== Ronde " + r + " ===");

                for (Creature c : listCreatures) {
                    if (c.getEnergy() >= c.getBehavior().energyCost()) {
                        System.out.println(c.performAction());
                    } else {
                        System.out.println(c.getName() + " kehabisan tenaga");
                    }
                }

                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Elf elf = new Elf("Ronan", new WiseBehavior());
        Goblin goblin = new Goblin("Koko", new AggressiveBehavior());
        Human human = new Human("Aldino", new ExplorerBehavior());
        WorldSimulator worldSimulator = new WorldSimulator();

        worldSimulator.addCreature(human);
        worldSimulator.addCreature(goblin);
        worldSimulator.addCreature(elf);

        worldSimulator.simulate(3);

    }
}
