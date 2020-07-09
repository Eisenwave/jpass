package de.janschultke.jpass.pixel;

import eisenwave.torrens.img.Texture;

public class PixelSSAdvMAME implements PixelSupersampler {
    
    private final int factor;
    
    public PixelSSAdvMAME(int factor) {
        if (factor != 2 && factor != 3)
            throw new IllegalArgumentException("unknown factor: "+factor);
        this.factor = factor;
    }
    
    @Override
    public int getFactor() {
        return factor;
    }
    
    public int[] apply(int[] in, int[] out, int w, int h) {
        if (factor == 2) return advMAME2x(in, out, w, h);
        else return advMAME3x(in, out, w, h);
    }
    
    @SuppressWarnings("Duplicates")
    private int[] advMAME2x(int[] in, int[] out, int w, int h) {
        final int
            outW = w * factor,
            outH = h * factor;
    
        if (out == null)
            out = Texture.alloc(outW, outH).getData();
    
        final Texture
            inWrap = Texture.wrap(in, w, h),
            outWrap = Texture.wrap(out, outW, outH);
    
        advMAMEEdge(inWrap, outWrap);
        
        /*
          A    --\ 1 2
        C P B  --/ 3 4
          D
        1=P; 2=P; 3=P; 4=P;
        IF C==A AND C!=D AND A!=B => 1=A
        IF A==B AND A!=C AND B!=D => 2=B
        IF D==C AND D!=B AND C!=A => 3=C
        IF B==D AND B!=A AND D!=C => 4=D
        */
        for (int x = 1; x < w-1; x++) for (int y = 1; y < h-1; y++) {
            final int
                x2 = x*factor, y2 = y*factor,
                A = inWrap.get(x, y-1),
                B = inWrap.get(x+1, y),
                C = inWrap.get(x-1, y),
                D = inWrap.get(x, y+1),
                P = inWrap.get(x, y);
    
            outWrap.set(x2,   y2,   C==A && C!=D && A!=B? A : P);
            outWrap.set(x2+1, y2,   A==B && A!=C && B!=D? B : P);
            outWrap.set(x2, y2+1,   D==C && D!=B && C!=A? C : P);
            outWrap.set(x2+1, y2+1, B==D && B!=A && D!=C? D : P);
        }
        
        return out;
    }
    
    @SuppressWarnings("Duplicates")
    private int[] advMAME3x(int[] in, int[] out, int w, int h) {
        final int
            outW = w * factor,
            outH = h * factor;
        
        if (out == null)
            out = Texture.alloc(outW, outH).getData();
        
        final Texture
            inWrap = Texture.wrap(in, w, h),
            outWrap = Texture.wrap(out, outW, outH);
    
        advMAMEEdge(inWrap, outWrap);
    
        /*
        A B C --\  1 2 3
        D E F    > 4 5 6
        G H I --/  7 8 9
        1=E; 2=E; 3=E; 4=E; 5=E; 6=E; 7=E; 8=E; 9=E;
        IF D==B AND D!=H AND B!=F => 1=D
        IF (D==B AND D!=H AND B!=F AND E!=C) OR (B==F AND B!=D AND F!=H AND E!=A) => 2=B
        IF B==F AND B!=D AND F!=H => 3=F
        IF (H==D AND H!=F AND D!=B AND E!=A) OR (D==B AND D!=H AND B!=F AND E!=G) => 4=D
        5=E
        IF (B==F AND B!=D AND F!=H AND E!=I) OR (F==H AND F!=B AND H!=D AND E!=C) => 6=F
        IF H==D AND H!=F AND D!=B => 7=D
        IF (F==H AND F!=B AND H!=D AND E!=G) OR (H==D AND H!=F AND D!=B AND E!=I) => 8=H
        IF F==H AND F!=B AND H!=D => 9=F
        */
        for (int x = 1; x < w-1; x++) for (int y = 1; y < h-1; y++) {
            final int
                x2 = x*factor, y2 = y*factor,
                A = inWrap.get(x-1, y-1), B = inWrap.get(x, y-1), C = inWrap.get(x+1, y-1),
                D = inWrap.get(x-1, y),   E = inWrap.get(x, y),   F = inWrap.get(x+1, y),
                G = inWrap.get(x-1, y+1), H = inWrap.get(x, y+1), I = inWrap.get(x+1, y+1);
            
            outWrap.set(  x2,   y2, D==B && D!=H && B!=F? D : E);
            outWrap.set(x2+1,   y2, (D==B && D!=H && B!=F && E!=C) || (B==F && B!=D && F!=H && E!=A)? B : E);
            outWrap.set(x2+2,   y2, B==F && B!=D && F!=H? F : E);
            
            outWrap.set(  x2, y2+1, (H==D && H!=F && D!=B && E!=A) || (D==B && D!=H && B!=F && E!=G)? D : E);
            outWrap.set(x2+1, y2+1, E);
            outWrap.set(x2+2, y2+1, (B==F && B!=D && F!=H && E!=I) || (F==H && F!=B && H!=D && E!=C)? F : E);
    
            outWrap.set(  x2, y2+2, H==D && H!=F && D!=B? D : E);
            outWrap.set(x2+1, y2+2, (F==H && F!=B && H!=D && E!=G) || (H==D && H!=F && D!=B && E!=I)? H : E);
            outWrap.set(x2+2, y2+2, F==H && F!=B && H!=D? F : E);
        }
        
        return out;
    }
    
    private void advMAMEEdge(Texture in, Texture out) {
        in.forEdge((x,y) -> {
            final int
                x2 = x*factor, y2 = y*factor,
                rgb = in.get(x, y);
        
            for (int i = 0; i < factor; i++)
                for (int j = 0; j < factor; j++)
                    out.set(x2+i, y2+j, rgb);
        });
    }
    
    public Texture apply(Texture in, int times) {
        Texture out = in;
        for (int i = 0; i < times; i++)
            out = apply(out);
        return out;
    }

}
