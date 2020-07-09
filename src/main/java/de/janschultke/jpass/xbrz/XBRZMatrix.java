package de.janschultke.jpass.xbrz;

//access matrix area, top-left at position "out" for image with given width
final class XBRZMatrix {
    private final XBRZ.IntPtr out;
    private int outi;
    private final int outWidth;
    private final int n;
    private int nr;
    
    public XBRZMatrix(final int scale, final int[] out, final int outWidth) {
        this.n = (scale - 2) * (XBRZ.maxRots * XBRZ.maxScaleSq);
        this.out = new XBRZ.IntPtr(out);
        this.outWidth = outWidth;
    }
    
    public void move(final int rotDeg, final int outi) {
        this.nr = n + rotDeg * XBRZ.maxScaleSq;
        this.outi = outi;
    }
    
    public final XBRZ.IntPtr ref(final int i, final int j) {
        final XBRZ.IntPair rot = XBRZ.matrixRotation[nr + i * XBRZ.maxScale + j];
        out.position(outi + rot.J + rot.I * outWidth);
        return out;
    }
    
}
