package org.group5;

public class HistogramData {

    private final int[] data;
    private final float interval;
    private final float min;
    private final float max;

    public HistogramData(int[] data, float interval, float min, float max) {
        this.data = data;
        this.interval = interval;
        this.min = min;
        this.max = max;
    }

    public int[] getData() {
        return data;
    }

    public float getInterval() {
        return interval;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

}
