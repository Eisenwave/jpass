package de.janschultke.jpass.pixel;

import eisenwave.torrens.img.Texture;

@SuppressWarnings("Duplicates")
public final class PixelSSEagle implements PixelSupersampler {
    
    @Override
    public int getFactor() {
        return 2;
    }
    
    @Override
    public int[] apply(int[] in, int[] out, int w, int h) {
        final int
            outW = w * 2,
            outH = h * 2;
        
        if (out == null)
            out = Texture.alloc(outW, outH).getData();
    
        final Texture
            inWrap = Texture.wrap(in, w, h),
            outWrap = Texture.wrap(out, outW, outH);
        
        Texture.wrap(in, w, h).forEdge((x,y) -> {
            final int
                x2 = x*2, y2 = y*2,
                rgb = inWrap.get(x, y);
    
            outWrap.set(  x2,   y2, rgb);
            outWrap.set(x2+1,   y2, rgb);
            outWrap.set(  x2, y2+1, rgb);
            outWrap.set(x2+1, y2+1, rgb);
        });
        
        /*
        first:        |Then
        . . . --\ CC  |S T U  --\ 1 2
        . C . --/ CC  |V C W  --/ 3 4
        . . .         |X Y Z
              | IF V==S==T => 1=S
              | IF T==U==W => 2=U
              | IF V==X==Y => 3=X
              | IF W==Z==Y => 4=Z
        */
        for (int x = 1; x < w-1; x++) for (int y = 1; y < h-1; y++) {
            final int
                x2 = x*2, y2 = y*2,
                S = inWrap.get(x-1, y-1), T = inWrap.get(x, y-1), U = inWrap.get(x+1, y-1),
                V = inWrap.get(x-1, y),   C = inWrap.get(x, y),   W = inWrap.get(x+1, y),
                X = inWrap.get(x-1, y+1), Y = inWrap.get(x, y+1), Z = inWrap.get(x+1, y+1);
    
            outWrap.set(x2,   y2,   S==V && S==T? S : C);
            outWrap.set(x2+1, y2,   U==T && U==W? U : C);
            outWrap.set(x2, y2+1,   X==V && X==Y? X : C);
            outWrap.set(x2+1, y2+1, Z==W && Z==Y? Z : C);
        }
        
        return out;
    }
    
    public Texture apply(Texture in, int times) {
        Texture out = in;
        for (int i = 0; i < times; i++)
            out = apply(out);
        return out;
    }
    
}
