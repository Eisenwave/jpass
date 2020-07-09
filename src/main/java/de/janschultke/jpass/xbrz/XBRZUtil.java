package de.janschultke.jpass.xbrz;

import org.jetbrains.annotations.Contract;

public final class XBRZUtil {
    
    public static final int
        RED_MASK = 0xff0000,
        GREEN_MASK = 0x00ff00,
        BLUE_MASK = 0x0000ff;
    
    public static void alphaBlend(final int n, final int m, final XBRZ.IntPtr dstPtr, final int col) {
        assert n < 256 : "possible overflow of (col & RED_MASK) * N";
        assert m < 256 : "possible overflow of (col & RED_MASK) * N + (dst & RED_MASK) * (M - N)";
        assert 0 < n && n < m : "0 < N && N < M";
        //this works because 8 upper bits are free
        final int dst = dstPtr.get();
        final int redComponent = blendComponent(RED_MASK, n, m, dst, col);
        final int greenComponent = blendComponent(GREEN_MASK, n, m, dst, col);
        final int blueComponent = blendComponent(BLUE_MASK, n, m, dst, col);
        final int blend = (redComponent | greenComponent | blueComponent);
        dstPtr.set(blend | 0xff000000);
    }
    
    @Contract(pure = true)
    private static int blendComponent(final int mask,
                                      final int n, final int m,
                                      final int inPixel, final int setPixel) {
        final int inChan = inPixel & mask;
        final int setChan = setPixel & mask;
        final int blend = setChan * n + inChan * (m - n);
        return mask & (blend / m);
    }
    
    @Contract(pure = true)
    public static double sqr(final double value) {
        return value * value;
    }
    
    public static double colorDist(final int pix1, final int pix2, final double luminanceWeight) {
        return pix1 == pix2? 0 : distYCbCr(pix1, pix2, luminanceWeight);
    }
    
    private static double distYCbCr(final int pix1, final int pix2, final double lumaWeight) {
        //http://en.wikipedia.org/wiki/YCbCr#ITU-R_BT.601_conversion
        //YCbCr conversion is a matrix multiplication => take advantage of linearity by subtracting first!
        final int r_diff = ((pix1 & RED_MASK) - (pix2 & RED_MASK)) >> 16;
        final int g_diff = ((pix1 & GREEN_MASK) - (pix2 & GREEN_MASK)) >> 8;
        final int b_diff = (pix1 & BLUE_MASK) - (pix2 & BLUE_MASK);
        
        final double k_b = 0.0722; //ITU-R BT.709 conversion
        final double k_r = 0.2126; //
        final double k_g = 1 - k_b - k_r;
        
        final double scale_b = 0.5 / (1 - k_b);
        final double scale_r = 0.5 / (1 - k_r);
        
        final double y = k_r * r_diff + k_g * g_diff + k_b * b_diff; //[!], analog YCbCr!
        final double c_b = scale_b * (b_diff - y);
        final double c_r = scale_r * (r_diff - y);
        
        // Skip division by 255.
        // Also skip square root here by pre-squaring the
        // config option equalColorTolerance.
        //return Math.sqrt(square(lumaWeight * y) + square(c_b) + square(c_r));
        return sqr(lumaWeight * y) + sqr(c_b) + sqr(c_r);
    }
    
    private static double distNonLinearRGB(final int pix1, final int pix2) {
        //non-linear rgb: http://www.compuphase.com/cmetric.htm
        final double r_diff = ((pix1 & RED_MASK) - (pix2 & RED_MASK)) >> 16; //we may delay division by 255 to after matrix multiplication
        final double g_diff = ((pix1 & GREEN_MASK) - (pix2 & GREEN_MASK)) >> 8; //
        final double b_diff = (pix1 & BLUE_MASK) - (pix2 & BLUE_MASK); //subtraction for int is noticeable faster than for double
        
        final double r_avg = (double) (((pix1 & RED_MASK) + (pix2 & RED_MASK)) >> 16) / 2;
        return ((2 + r_avg / 255) * sqr(r_diff) + 4 * sqr(g_diff) + (2 + (255 - r_avg) / 255) * sqr(b_diff));
    }
    
}
