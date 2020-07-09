package de.janschultke.jpass.xbrz;

public interface XBRZScaler {
    
    int scale();
    
    void blendLineSteep(int col, XBRZMatrix out);
    
    void blendLineSteepAndShallow(int col, XBRZMatrix out);
    
    void blendLineShallow(int col, XBRZMatrix out);
    
    void blendLineDiagonal(int col, XBRZMatrix out);
    
    void blendCorner(int col, XBRZMatrix out);
    
    public static final XBRZScaler X2 = new XBRZScaler() {
        private static final int scale = 2;
        
        public int scale() {
            return scale;
        }
        
        public final void blendLineShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 1, 0), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 1, 1), col);
        }
        
        public final void blendLineSteep(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(0, scale - 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, scale - 1), col);
        }
        
        public final void blendLineSteepAndShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(1, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(0, 1), col);
            XBRZUtil.alphaBlend(5, 6, out.ref(1, 1), col); //[!] fixes 7/8 used in xBR
        }
        
        public final void blendLineDiagonal(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 2, out.ref(1, 1), col);
        }
        
        public final void blendCorner(int col, XBRZMatrix out) {
            //model a round corner
            XBRZUtil.alphaBlend(21, 100, out.ref(1, 1), col); //exact: 1 - pi/4 = 0.2146018366
        }
    };
    
    public static final XBRZScaler X3 = new XBRZScaler() {
        private static final int scale = 3;
        
        public int scale() {
            return scale;
        }
        
        public final void blendLineShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 1, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 2, 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 1, 1), col);
            out.ref(scale - 1, 2).set(col);
        }
        
        public final void blendLineSteep(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(0, scale - 1), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(2, scale - 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, scale - 1), col);
            out.ref(2, scale - 1).set(col);
        }
        
        public final void blendLineSteepAndShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(2, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(0, 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(2, 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, 2), col);
            
            out.ref(2, 2).set(col);
        }
        
        public final void blendLineDiagonal(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 8, out.ref(1, 2), col);
            XBRZUtil.alphaBlend(1, 8, out.ref(2, 1), col);
            XBRZUtil.alphaBlend(7, 8, out.ref(2, 2), col);
        }
        
        public final void blendCorner(int col, XBRZMatrix out) {
            //model a round corner
            XBRZUtil.alphaBlend(45, 100, out.ref(2, 2), col); //exact: 0.4545939598
            //alphaBlend(14, 1000, out.ref(2, 1), col); //0.01413008627 -> negligable
            //alphaBlend(14, 1000, out.ref(1, 2), col); //0.01413008627
        }
    };
    
    public static final XBRZScaler X4 = new XBRZScaler() {
        private static final int scale = 4;
        
        public int scale() {
            return scale;
        }
        
        public final void blendLineShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 1, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 2, 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 1, 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 2, 3), col);
            out.ref(scale - 1, 2).set(col);
            out.ref(scale - 1, 3).set(col);
        }
        
        public final void blendLineSteep(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(0, scale - 1), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(2, scale - 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, scale - 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(3, scale - 2), col);
            out.ref(2, scale - 1).set(col);
            out.ref(3, scale - 1).set(col);
        }
        
        public final void blendLineSteepAndShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(3, 4, out.ref(3, 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, 3), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(3, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(0, 3), col);
            XBRZUtil.alphaBlend(1, 3, out.ref(2, 2), col); //[!] fixes 1/4 used in xBR
            out.ref(3, 3).set(col);
            out.ref(3, 2).set(col);
            out.ref(2, 3).set(col);
        }
        
        public final void blendLineDiagonal(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 2, out.ref(scale - 1, scale / 2), col);
            XBRZUtil.alphaBlend(1, 2, out.ref(scale - 2, scale / 2 + 1), col);
            out.ref(scale - 1, scale - 1).set(col);
        }
        
        public final void blendCorner(int col, XBRZMatrix out) {
            //model a round corner
            XBRZUtil.alphaBlend(68, 100, out.ref(3, 3), col); //exact: 0.6848532563
            XBRZUtil.alphaBlend(9, 100, out.ref(3, 2), col); //0.08677704501
            XBRZUtil.alphaBlend(9, 100, out.ref(2, 3), col); //0.08677704501
        }
    };
    
    public static final XBRZScaler X5 = new XBRZScaler() {
        private static final int scale = 5;
        
        public int scale() {
            return scale;
        }
        
        public final void blendLineShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 1, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 2, 2), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 3, 4), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 1, 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 2, 3), col);
            out.ref(scale - 1, 2).set(col);
            out.ref(scale - 1, 3).set(col);
            out.ref(scale - 1, 4).set(col);
            out.ref(scale - 2, 4).set(col);
        }
        
        public final void blendLineSteep(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(0, scale - 1), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(2, scale - 2), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(4, scale - 3), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, scale - 1), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(3, scale - 2), col);
            out.ref(2, scale - 1).set(col);
            out.ref(3, scale - 1).set(col);
            out.ref(4, scale - 1).set(col);
            out.ref(4, scale - 2).set(col);
        }
        
        public final void blendLineSteepAndShallow(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 4, out.ref(0, scale - 1), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(2, scale - 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(1, scale - 1), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 1, 0), col);
            XBRZUtil.alphaBlend(1, 4, out.ref(scale - 2, 2), col);
            XBRZUtil.alphaBlend(3, 4, out.ref(scale - 1, 1), col);
            out.ref(2, scale - 1).set(col);
            out.ref(3, scale - 1).set(col);
            out.ref(scale - 1, 2).set(col);
            out.ref(scale - 1, 3).set(col);
            out.ref(4, scale - 1).set(col);
            XBRZUtil.alphaBlend(2, 3, out.ref(3, 3), col);
        }
        
        public final void blendLineDiagonal(int col, XBRZMatrix out) {
            XBRZUtil.alphaBlend(1, 8, out.ref(scale - 1, scale / 2), col);
            XBRZUtil.alphaBlend(1, 8, out.ref(scale - 2, scale / 2 + 1), col);
            XBRZUtil.alphaBlend(1, 8, out.ref(scale - 3, scale / 2 + 2), col);
            XBRZUtil.alphaBlend(7, 8, out.ref(4, 3), col);
            XBRZUtil.alphaBlend(7, 8, out.ref(3, 4), col);
            out.ref(4, 4).set(col);
        }
        
        public final void blendCorner(int col, XBRZMatrix out) {
            //model a round corner
            XBRZUtil.alphaBlend(86, 100, out.ref(4, 4), col); //exact: 0.8631434088
            XBRZUtil.alphaBlend(23, 100, out.ref(4, 3), col); //0.2306749731
            XBRZUtil.alphaBlend(23, 100, out.ref(3, 4), col); //0.2306749731
            //alphaBlend(8, 1000, out.ref(4, 2), col); //0.008384061834 -> negligable
            //alphaBlend(8, 1000, out.ref(2, 4), col); //0.008384061834
        }
    };
    
}
