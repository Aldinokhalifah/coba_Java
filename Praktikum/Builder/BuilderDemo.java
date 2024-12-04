public class BuilderDemo {
    public static void main(String[] args) {
        CDBuilder cdBuilder = new CDBuilder();

        CDType sonyCD = cdBuilder.buildSonyCD();
        CDType samsungCD = cdBuilder.buildSamsungCD();
        CDType polytronCD = cdBuilder.buildPolytronCD();

        System.out.println("Sony CD:");
        sonyCD.showItems();

        System.out.println("\nSamsung CD:");
        samsungCD.showItems();

        System.out.println("\nPolytron CD:");
        polytronCD.showItems();
    }
}

