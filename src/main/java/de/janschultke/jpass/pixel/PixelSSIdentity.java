package de.janschultke.jpass.pixel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PixelSSIdentity implements PixelSupersampler {
    
    @Override
    public int[] apply(@NotNull int[] in, @Nullable int[] out, int w, int h) {
        if (out == null)
            return Arrays.copyOf(in, in.length);
        System.arraycopy(in, 0, out, 0, in.length);
        return out;
    }
    
    @Override
    public int getFactor() {
        return 1;
    }
    
}
