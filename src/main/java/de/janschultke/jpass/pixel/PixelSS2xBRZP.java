package de.janschultke.jpass.pixel;

import de.janschultke.jpass.xbrz.XBRZ;
import de.janschultke.jpass.xbrz.XBRZConfig;
import de.janschultke.jpass.xbrz.XBRZScaleSize;
import eisenwave.torrens.img.Texture;
import eisenwave.torrens.img.scale.ScaleBox;

public class PixelSS2xBRZP implements PixelSupersampler {
    
    private final static XBRZConfig CONFIG = new XBRZConfig();
    
    @Override
    public int getFactor() {
        return 2;
    }
    
    @Override
    public int[] apply(int[] in, int[] out, int w, int h) {
        XBRZScaleSize scaleSize = XBRZScaleSize.valueOf(4);
        
        final int[] result;
        {
            Texture brz = Texture.alloc(w * 4, h * 4);
            new XBRZ().scaleImage(scaleSize, in, brz.getData(), w, h, CONFIG);
            
            result = new ScaleBox().apply(brz, w * 2, w * 2).getData();
        }
        
        if (out == null) return result;
        
        System.arraycopy(result, 0, out, 0, result.length);
        return out;
    }
    
}
