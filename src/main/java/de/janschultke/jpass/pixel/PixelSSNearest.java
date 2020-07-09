package de.janschultke.jpass.pixel;

import eisenwave.torrens.img.Texture;

public class PixelSSNearest implements PixelSupersampler {
    
    private final int factor;
    
    public PixelSSNearest(int factor) {
        if (factor < 1) throw new IllegalArgumentException("factor must be at least 1");
        this.factor = factor;
    }
    
    
    @Override
    public int getFactor() {
        return factor;
    }
    
    @Override
    public int[] apply(int[] in, int[] out, int w, int h) {
        final int
            outW = w * factor,
            outH = h * factor;
        
        if (out == null)
            out = Texture.alloc(outW, outH).getData();
        
        final Texture
            inWrap = Texture.wrap(in, w, h),
            outWrap = Texture.wrap(out, outW, outH);
    
        for (int x = 0; x < outW; x++) for (int y = 0; y < outH; y++) {
            final int rgb = inWrap.get(
                x * w / outW,
                y * h / outH);
            outWrap.set(x, y, rgb);
        }
    
        return out;
    }
    
}
