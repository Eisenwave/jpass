package de.janschultke.jpass.pixel;

import de.janschultke.jpass.xbrz.XBRZ;
import de.janschultke.jpass.xbrz.XBRZConfig;
import de.janschultke.jpass.xbrz.XBRZScaleSize;
import eisenwave.torrens.img.Texture;

public class PixelSSxBRZ implements PixelSupersampler {
    
    private final static XBRZConfig CONFIG = new XBRZConfig();
    
    private final XBRZScaleSize scaleSize;
    
    public PixelSSxBRZ(int factor) {
        this.scaleSize = XBRZScaleSize.valueOf(factor);
    }
    
    @Override
    public int getFactor() {
        return scaleSize.size;
    }
    
    @Override
    public int[] apply(int[] in, int[] out, int w, int h) {
        if (out == null)
            out = Texture.alloc(w*scaleSize.size, h*scaleSize.size).getData();
        
        new XBRZ().scaleImage(scaleSize, in, out, w, h, CONFIG);
        
        return out;
    }
    
}
