package imageeditor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Viewer extends Canvas {

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage[] images;

    public Viewer() {
        this.loadImage();
    }

    //Public Methods
    public int getImageHeight() {
        return this.img1.getHeight();
    }

    public int getImageWidth() {
        return this.img1.getWidth();
    }

    @Override
    public Dimension getPreferredSize() {
        return img1 == null ? new Dimension(200, 200) : new Dimension(img1.getWidth(), img1.getHeight());
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.img1 != null) {
            g.drawImage(this.img1, 0, 0, this);
        }
    }

    public void paintImg(BufferedImage img, Graphics g) {
        super.paint(g);
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }

    //Private Methods
    private void loadImage() {
        try {
            this.img1 = ImageIO.read(new File("src/img/1.png"));
            this.img2 = ImageIO.read(new File("src/img/2.png"));
        } catch (IOException e) {
            System.out.println("No se puede encontrar la imagen");
        }
    }

    private void alter() {

        byte[] data = ((DataBufferByte) this.img1.getData().getDataBuffer()).getData();

        //Dimensiones rectangulo
        int rowI = 0;
        int rowF = this.img1.getHeight();
        int colI = 0;
        int colF = this.img1.getWidth();

        for (int row = rowI; row < rowF; row++) {
            for (int col = colI; col < colF; col++) {

            }
        }

        //Setear imagen 2
        //this.img2.setData(Raster.createRaster(this.img.getSampleModel(), new DataBufferByte(data, data.length), new Point()));
    }

    private void toNegative(byte[] data, int row, int col) {
        for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo
            data[this.getPixel(row, col) + inc] = ((byte) (255 - (data[this.getPixel(row, col) + inc])));
        }
    }

    private void increaseSaturation(byte[] data, int row, int col, int shy) {
        for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo

            if (Byte.toUnsignedInt(data[this.getPixel(row, col) + inc]) + shy < 255) {
                data[this.getPixel(row, col) + inc] = ((byte) ((Byte.toUnsignedInt(data[this.getPixel(row, col) + inc])) + shy));
            } else {
                data[this.getPixel(row, col) + inc] = ((byte) (255));
            }

        }
    }

    private void toBlackWhite(byte[] data, int row, int col) {
        int media = 0;
        //Hacer media de BGR
        for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo
            media += Byte.toUnsignedInt(data[(this.getPixel(row, col)) + inc]);
        }
        media = media / 3;
        //Poner media en BGR
        for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo
            data[(this.getPixel(row, col)) + inc] = ((byte) media);
        }
    }

    private void invertX(byte[] data, int row, int col) {
        if (col < this.img1.getWidth() / 2) {
            for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo
                byte bnc = data[this.getPixel(row, col) + inc];
                data[this.getPixel(row, col) + inc] = data[this.getPixel(row, (this.img1.getWidth() - col) - 1) + inc];
                data[this.getPixel(row, (this.img1.getWidth() - col) - 1) + inc] = bnc;
            }
        }
    }

    private void invertY(byte[] data, int row, int col) {
        if (row < this.img1.getHeight() / 2) {
            for (int inc = 0; inc < 3; inc++) { // 0 = Azul, 1 = Verde, 2 = Rojo
                byte bnc = data[this.getPixel(row, col) + inc];
                data[this.getPixel(row, col) + inc] = data[this.getPixel(((this.img1.getHeight() - row) - 1), col) + inc];
                data[this.getPixel(((this.img1.getHeight() - row) - 1), col) + inc] = bnc;
            }
        }
    }

    private int getPixel(int row, int col) {
        int pixel = ((row * this.img1.getWidth()) + col) * 3;
        return pixel;
    }

    public void melt(int frames, int vel, Graphics g) {

        loadImages(frames);

        byte[] base1 = ((DataBufferByte) this.img1.getData().getDataBuffer()).getData();
        byte[] base2 = ((DataBufferByte) this.img2.getData().getDataBuffer()).getData();
        
        for (int inc = 0; inc < frames; inc++) {
            
            byte[] data = ((DataBufferByte) this.images[inc].getData().getDataBuffer()).getData();
            
            for (int pix = 0; pix < data.length; pix++) {
                int p1 = Byte.toUnsignedInt(base1[pix]);
                int p2 = Byte.toUnsignedInt(base2[pix]);
                int dif = p2 - p1;
                double incC = dif/frames;
                data[pix] = (byte) (p1 + (incC * inc));
            }
            
            this.images[inc].setData(Raster.createRaster(this.img1.getSampleModel(), new DataBufferByte(data, data.length), new Point()));
            
        }

        this.showMelt(vel, g);

    }

    private void loadImages(int frames) {
        this.images = new BufferedImage[frames];
        for (int inc = 0; inc < this.images.length; inc++) {
            try {
                this.images[inc] = ImageIO.read(new File("src/img/1.png"));
            } catch (IOException e) {
                System.out.println("No se puede encontrar la imagen");
            }
        }
    }

    public void showMelt(int vel, Graphics g) {
        for (int inc = 0; inc < this.images.length; inc++) {
            try {
                this.paintImg(this.images[inc], g);
                Thread.sleep(vel * 100);
            } catch (InterruptedException e) {

            }
        }
        try {
            this.paintImg(this.img2, g);
            Thread.sleep(vel * 100);
        } catch (InterruptedException e) {

        }
    }

}
