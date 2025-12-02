
import java.util.ArrayList;

public class Konflik_Antar_DImensi {
    

    interface DimensionalBeing {
        String dimensionName();
        String powerSignature();
    }

    static abstract class Entity {
        String name;
        int threatLevel;

        public Entity(String name, int threatLevel) {
            this.name = name;
            this.threatLevel = threatLevel;
        }

        public String getName() {
            return name;
        }

        public int getThreatLevel() {
            return threatLevel;
        }
    }

    static class GhostEntity extends Entity implements DimensionalBeing {

        public GhostEntity() {
            super("it", 86);
        }

        @Override
        public String dimensionName() {
            return "Dimensi ke-10";
        }

        @Override
        public String powerSignature() {
            return "Mengeluarkan gas beracun";
        }
    }

    static class AlienEntity extends Entity implements DimensionalBeing {

        public AlienEntity() {
            super("Preketek", 94);
        }
        

        @Override
        public String dimensionName() {
            return "Luar tata surya";
        }

        @Override
        public String powerSignature() {
            return "Kecerdasan yang tak terbatas";
        }
    }

    static class TimeTravelerEntity extends Entity implements DimensionalBeing {

        public TimeTravelerEntity() {
            super("Kipli", 100);
        }
        
        @Override
        public String dimensionName() {
            return "Bumi";
        }

        @Override
        public String powerSignature() {
            return "Penuh tipu daya";
        }
    }

    static class DimensionalCouncil {
        ArrayList<DimensionalBeing> entities = new ArrayList<>();

        public void addEntity(DimensionalBeing d) {
            entities.add(d);
        }

        public void resolveConflicts() {
            for(DimensionalBeing d : entities) {
                if(d instanceof Entity) {
                    Entity e = (Entity) d;
                    if(e.getThreatLevel() < 90) {
                        System.out.println(e.getName() + ": " + d.dimensionName() + " | " + d.powerSignature() + " Threat: " + e.getThreatLevel());
                    } else if(e.getThreatLevel() < 95) {
                        System.out.println(e.getName() + ": " + d.dimensionName() + " | " + d.powerSignature() + " Threat: " + e.getThreatLevel() + " Status: BERBAHAYA");
                    } else {
                        System.out.println(e.getName() + ": " + d.dimensionName() + " | " + d.powerSignature() + " Threat: " + e.getThreatLevel() + " Status: MENGANCAM MAKHLUK HIDUP");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        GhostEntity ghostEntity = new GhostEntity();
        AlienEntity alienEntity = new AlienEntity();
        TimeTravelerEntity timeTravelerEntity = new TimeTravelerEntity();
        DimensionalCouncil dimensionalCouncil = new DimensionalCouncil();

        dimensionalCouncil.addEntity(alienEntity);
        dimensionalCouncil.addEntity(ghostEntity);
        dimensionalCouncil.addEntity(timeTravelerEntity);

        dimensionalCouncil.resolveConflicts();
    }
}
