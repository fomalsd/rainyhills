package ltd.foma.rainyhills.data;

/**
 * Gap start-end index data, including gap borders
 */
public class GapData {
    public final int start, end;

    public GapData(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[" + start + "-" + end + "]";
    }
}
