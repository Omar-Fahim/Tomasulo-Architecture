package micro;

public class Pair<X, Y> {
   public String  tagB;
   public double valueB;

    public Pair(String first, double second) {
        this.tagB = first;
        this.valueB = second;
    }

    public String getFirst() {
        return tagB;
    }

    public double getSecond() {
        return valueB;
    }

    @Override
    public String toString() {
        return "(" + tagB + ", " + valueB + ")";
    }
}

