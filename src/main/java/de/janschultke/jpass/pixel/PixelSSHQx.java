package de.janschultke.jpass.pixel;

import eisenwave.torrens.img.Texture;
import de.janschultke.jpass.hqx.Hqx_2x;
import de.janschultke.jpass.hqx.Hqx_3x;
import de.janschultke.jpass.hqx.Hqx_4x;
import de.janschultke.jpass.hqx.RgbYuv;

public class PixelSSHQx implements PixelSupersampler {
    
    private final HQxMethod method;
    private final int factor;
    
    public PixelSSHQx(int factor) {
        this.method = HQxMethod.valueOf(factor);
        this.factor = factor;
        RgbYuv.init();
    }
    
    @Override
    public int getFactor() {
        return factor;
    }
    
    @Override
    public int[] apply(int[] in, int[] out, int w, int h) {
        if (out == null)
            out = Texture.alloc(w*factor, h*factor).getData();
        
        method.invoke(in, out, w, h);
        
        return out;
    }
    
    @Override
    protected void finalize() throws Throwable {
        RgbYuv.free();
    }
    
    private static interface HQxMethod {
        abstract void invoke(int[] in, int[] out, int w, int h);
        
        static HQxMethod valueOf(int factor) {
            switch (factor) {
                case 2: return Hqx_2x::hq2x_32_rb;
                case 3: return Hqx_3x::hq3x_32_rb;
                case 4: return Hqx_4x::hq4x_32_rb;
                default: throw new IllegalArgumentException("unknown factor: "+2);
            }
        }
        
    }
    
}
