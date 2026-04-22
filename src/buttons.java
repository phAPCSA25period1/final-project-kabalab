public class buttons {
    private static int[] size;
    private static int[] switches;
    private int[] pos;
    private int sizeOfButton;

    public static void boot(int[] psize, int[] pswitches) {
        size = psize;// size of screan
        switches = pswitches;// number of buttons
    }

    public buttons(int[] ppos) {// ppos 1 will be first butotn if its a 3x3 4 will be 1,2
        pos = size;

    }
}
