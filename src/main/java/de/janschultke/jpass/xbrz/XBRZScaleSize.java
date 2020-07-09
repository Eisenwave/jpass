package de.janschultke.jpass.xbrz;

public enum XBRZScaleSize {
    Times2(XBRZScaler.X2),
    Times3(XBRZScaler.X3),
    Times4(XBRZScaler.X4),
    Times5(XBRZScaler.X5);
    
    public final XBRZScaler scaler;
    public final int size;
    
    private XBRZScaleSize(XBRZScaler scaler) {
        this.scaler = scaler;
        this.size = scaler.scale();
    }
    
    public static XBRZScaleSize cast(final int ordinal) {
        final int ord1 = Math.max(ordinal, 0);
        final int ord2 = Math.min(ord1, values().length - 1);
        return values()[ord2];
    }
    
    public static XBRZScaleSize valueOf(int value) {
        for (XBRZScaleSize size : values())
            if (size.size == value) return size;
        throw new IllegalArgumentException("unsupported scale size: " + value);
    }
    
}
