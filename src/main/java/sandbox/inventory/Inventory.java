package sandbox.inventory;

public class Inventory {
    private int eggs;
    private int wood;
    private int flowers;

    public int getEggs() {
        return eggs;
    }

    public int getWood() {
        return wood;
    }

    public int getFlowers() {
        return flowers;
    }

    public void addEggs(int amount) {
        eggs = Math.max(0, eggs + amount);
    }

    public void addWood(int amount) {
        wood = Math.max(0, wood + amount);
    }

    public void addFlowers(int amount) {
        flowers = Math.max(0, flowers + amount);
    }
}
