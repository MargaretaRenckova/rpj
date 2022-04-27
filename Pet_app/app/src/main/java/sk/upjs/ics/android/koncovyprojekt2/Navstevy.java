package sk.upjs.ics.android.koncovyprojekt2;

public class Navstevy {
    String date;
    String reason;
    String next;

    public Navstevy (String string) {
        String []x = string.split("#");
        date = x[0];
        reason = x[1];
        next = x[2];
    }
}
