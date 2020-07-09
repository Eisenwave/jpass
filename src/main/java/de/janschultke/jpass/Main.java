package de.janschultke.jpass;

import de.janschultke.jpass.pixel.*;
import eisenwave.torrens.img.*;
import eisenwave.torrens.img.gif.*;
import eisenwave.torrens.util.ConcurrentArrays;
import org.jetbrains.annotations.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    
    private final static String[] ALGORITHMS = {
        "Identity (1x):            identity",
        "Nearest Neighbor (2..5x): ?xnn|?xnearest|nearest?|nearest_neighbor_?",
        "xBRZ (2..5x):             xbrz?|?xbrz",
        "AdvMAME (2|3x):           advmame2|advmame2x|advmame3|advmame3x",
        "Eagle (2x):               eagle",
        "HQX (2..4x):              hqx?|hqx_?|hqx?x"
    };
    
    public static void main(String... args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: jpass ALGORITHM SOURCE [TARGET]\n\nAlgorithms:");
            for (String algo : ALGORITHMS)
                System.err.println("  " + algo);
            System.exit(1);
        }
        
        File inFile = new File(args[1]).getAbsoluteFile();
        if (!inFile.canRead())
            throw new IOException("can't read from: "+inFile);
        
        String suffix = JPASSUtil.getSuffix(inFile);
        if (suffix == null)
            throw new IllegalArgumentException("input path must have valid suffix");
        
        String samplerName = args[0];
        PixelSupersampler sampler = PixelSupersampler.getByName(samplerName);
    
        String outName = JPASSUtil.noSuffix(inFile.getName()) + "_" + samplerName + "." + suffix;
        
        File outFile = args.length > 2?
            new File(args[2]) :
            new File(inFile.getParentFile(), outName);
    
        System.err.println("reading input image ...");
    
        Object in = read(inFile, suffix);
    
        System.err.println("read "+in);
        
        ConcurrentArrays.setMaxThreads(4);
    
        System.err.println("writing output image ...");
    
        applyAndWrite(sampler, in, outFile, suffix);
    }
    
    @NotNull
    private static Object read(File file, @NotNull String suffix) throws IOException {
        if (suffix.equalsIgnoreCase("gif"))
            return new GIFDecoder(file);
        else
            return Texture.wrapOrCopy(new DeserializerImage().fromFile(file));
    }
    
    /*
    private static void write(File file, String format, Object obj) throws IOException {
        if (obj instanceof GIFAnimation)
            new SerializerGIFAnimation().toFile((GIFAnimation) obj, file);
        
        else if (obj instanceof BufferedImage)
            ImageIO.write((BufferedImage) obj, format, file);
        
        else
            ImageIO.write(((Texture) obj).getImageWrapper(), format, file);
    }
    */
    
    private static void applyAndWrite(PixelSupersampler sampler, Object obj, File file, String format)
        throws IOException {
        
        if (obj instanceof GIFDecoder) {
            final int factor = sampler.getFactor();
            GIFDecoder dec = (GIFDecoder) obj;
            GIFEncoder enc = new GIFEncoder(file);
            
            GIFHeader header = dec.getHeader().clone();
            header.setWidth(header.getWidth()*factor);
            header.setHeight(header.getHeight()*factor);
            
            enc.writeHeader(header);
    
            while (dec.hasNext()) {
                GIFFrame frame = dec.next();
    
                Texture t = sampler.apply(dec.getCurrentData());
                enc.write(new GIFFrame(frame, t));
            }
            
            dec.close();
            enc.close();
        }
        
        else if (obj instanceof BufferedImage) {
            BufferedImage out = sampler.apply((BufferedImage) obj);
            ImageIO.write(out, format, file);
        }
        
        else {
            Texture out = sampler.apply((Texture) obj);
            BufferedImage image = format.equals("jpg") || format.equals("jpeg")?
                out.toImage(false) :
                out.getImageWrapper();
    
            Pair<ImageWriter, ImageWriteParam> writer = getImageWriterByFormat(format);
            writer.getKey().setOutput(ImageIO.createImageOutputStream(file));
            writer.getKey().write(null, new IIOImage(image, null, null), writer.getValue());
        }
    }
    
    private static Pair<ImageWriter, ImageWriteParam> getImageWriterByFormat(String format) {
        switch (format.toLowerCase()) {
            case "image/jpeg":
            case "jpg":
            case "jpeg": {
                ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                //param.unsetCompression();
                param.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
                //param.setCompressionQuality(1);
                return new Pair<>(writer, param);
            }
    
            case "image/png":
            case "png": {
                ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/png").next();
                return new Pair<>(writer, null);
            }
            
            default: return new Pair<>(ImageIO.getImageWritersByFormatName(format).next(), null);
        }
    }
    
    private static class Pair<K, V> {
        
        private final K key;
        private final V value;
        
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    
        public K getKey() {
            return key;
        }
    
        public V getValue() {
            return value;
        }
        
    }
    
}
