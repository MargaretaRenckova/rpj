package sk.upjs.ics.android.koncovyprojekt2;

public class Ockovania {            // objekt vytvoreny pre lepsiu pracu s datami
    String date;
    String vaccine;
    String next;

    public Ockovania (String string) {
        String []x = string.split("#");
        date = x[0];
        vaccine = x[1];
        next = x[2];
    }
}
