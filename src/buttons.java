public class buttons {
    private static int[] size;
    private static int[] switches;
    private int[] pos;
    private int sizeOfButton;

    public static void boot(int[] psize, int[] pswitches) {
        size = psize;
        switches = pswitches;
    }

    public buttons(int[] ppos) {
        pos = size;
    }
}
