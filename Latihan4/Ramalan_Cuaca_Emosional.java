
import java.util.ArrayList;

public class Ramalan_Cuaca_Emosional {
    
    interface EmotionalWeather {
        String currentWeather();
        String emotionalState();
    }

    static abstract class Being {
        String name;
        int energyLevel;

        public Being(String name, int energyLevel) {
            this.name = name;
            this.energyLevel = energyLevel;
        }

        public String getName() {
            return name;
        }

        public int getEnergyLevel() {
            return energyLevel;
        }
    }

    static class RobotForecaster extends Being implements EmotionalWeather {

        public RobotForecaster() {
            super("Optimus", 96);
        }

        @Override
        public String currentWeather() {
            return "Cerah";
        }

        @Override
        public String emotionalState() {
            return "Kesel sama Megatron";
        }
    }

    static class HumanForecaster extends Being implements EmotionalWeather {

        public HumanForecaster() {
            super("Aldino", 100);
        }

        @Override
        public String currentWeather() {
            return "Mendung";
        }

        @Override
        public String emotionalState() {
            return "Lagi belajar oop sama ChatGPT";
        }
    }

    static class CatForecaster extends Being implements EmotionalWeather {

        public CatForecaster() {
            super("Meow", 89);
        }

        @Override
        public String currentWeather() {
            return "Meow meow";
        }

        @Override
        public String emotionalState() {
            return "Meow meoew meow";
        }
    }

    static class WeatherStation {
        ArrayList<EmotionalWeather> members = new ArrayList<>();

        public void addSource(EmotionalWeather e) {
            members.add(e);
        }

        public void generateReport() {
            for(EmotionalWeather e: members) {
                if(e instanceof Being) {
                    Being b = (Being) e;
                    System.out.println(b.getName() + ": " + e.currentWeather() + " | " + e.emotionalState());
                }
            }
        }
    }

    public static void main(String[] args) {
        RobotForecaster robotForecaster = new RobotForecaster();
        HumanForecaster humanForecaster = new HumanForecaster();
        CatForecaster catForecaster = new CatForecaster();
        WeatherStation weatherStation = new WeatherStation();

        weatherStation.addSource(catForecaster);
        weatherStation.addSource(humanForecaster);
        weatherStation.addSource(robotForecaster);

        weatherStation.generateReport();
    }
}
