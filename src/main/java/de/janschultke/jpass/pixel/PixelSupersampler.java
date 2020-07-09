package de.janschultke.jpass.pixel;

import eisenwave.torrens.img.Texture;
import org.jetbrains.annotations.*;

import java.awt.image.BufferedImage;

public interface PixelSupersampler {
    
    public final static PixelSupersampler
        IDENTITY = new PixelSSIdentity(),
        NEAREST_NEIGHBOR_2 = new PixelSSNearest(2),
        NEAREST_NEIGHBOR_3 = new PixelSSNearest(3),
        NEAREST_NEIGHBOR_4 = new PixelSSNearest(4),
        NEAREST_NEIGHBOR_5 = new PixelSSNearest(5),
        XBRZ_2 = new PixelSSxBRZ(2),
        XBRZ_3 = new PixelSSxBRZ(3),
        XBRZ_4 = new PixelSSxBRZ(4),
        XBRZ_5 = new PixelSSxBRZ(5),
        ADVMAME_2 = new PixelSSAdvMAME(2),
        ADVMAME_3 = new PixelSSAdvMAME(3),
        EAGLE = new PixelSSEagle(),
        HQX_2 = new PixelSSHQx(2),
        HQX_3 = new PixelSSHQx(3),
        HQX_4 = new PixelSSHQx(4);
    
    
    @NotNull
    public static PixelSupersampler getByName(String name) {
        switch (name.toLowerCase()) {
            case "none":
            case "identity": return IDENTITY;
            
            case "2xnn":
            case "2xnearest":
            case "nearest2":
            case "nearest_2":
            case "nearest_neighbor_2": return NEAREST_NEIGHBOR_2;
    
            case "3xnn":
            case "3xnearest":
            case "nearest3":
            case "nearest_3":
            case "nearest_neighbor_3": return NEAREST_NEIGHBOR_3;
    
            case "4xnn":
            case "4xnearest":
            case "nearest4":
            case "nearest_4":
            case "nearest_neighbor_4": return NEAREST_NEIGHBOR_4;
    
            case "5xnn":
            case "5xnearest":
            case "nearest5":
            case "nearest_5":
            case "nearest_neighbor_5": return NEAREST_NEIGHBOR_5;
    
            case "xbrz2":
            case "xbrz_2":
            case "2xbrz": return XBRZ_2;
    
            case "xbrz3":
            case "xbrz_3":
            case "3xbrz": return XBRZ_3;
    
            case "xbrz4":
            case "xbrz_4":
            case "4xbrz": return XBRZ_4;
    
            case "xbrz5":
            case "xbrz_5":
            case "5xbrz": return XBRZ_5;
            
            case "2xbrzp": return new PixelSS2xBRZP();
            
            case "advmame2":
            case "advmame_2":
            case "advmame2x": return ADVMAME_2;
            
            case "advmame3":
            case "advmame_3":
            case "advmame3x": return ADVMAME_3;
            
            case "eagle": return EAGLE;
            
            case "hqx2":
            case "hqx_2":
            case "hqx2x": return HQX_2;
    
            case "hqx3":
            case "hqx_3":
            case "hqx3x": return HQX_3;
    
            case "hqx4":
            case "hqx_4":
            case "hqx4x": return HQX_4;
            
            default: throw new IllegalArgumentException("unknown sampler: " + name);
        }
    }
    
    /**
     * Applies the the supersampling to given image data.
     *
     * @param in the input data array
     * @param out the optionally pre-allocated output data array
     * @param w the image width
     * @param h the image height
     * @return the output data
     */
    abstract int[] apply(@NotNull int[] in, @Nullable int[] out, int w, int h);
    
    /**
     * Returns the factor by which this supersampler scales up the original image.
     *
     * @return the scaling factor
     */
    abstract int getFactor();
    
    default Texture apply(Texture in) {
        final int
            inW = in.getWidth(),
            inH = in.getHeight(),
            outW = inW * getFactor(),
            outH = inH * getFactor();
        
        return Texture.wrap(apply(in.getData(), null, inW, inH), outW, outH);
    }
    
    default BufferedImage apply(BufferedImage in) {
        return apply(Texture.wrapOrCopy(in)).getImageWrapper();
    }
    
}
