/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Segmentation;

/**
 *
 * @author ShreeGanesh
 */

//import Recognition.Recognition;
//import Recognition.Scale;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import CAT.*;

/**
 *
 * @author AJINKYA
 */
public class Thinning {
    BufferedImage biIn, biOut;
    Graphics2D g2dIn, g2dOut;
    int w, h;
    int inPixels[][];
   // BufferedImage br_arr[]=new BufferedImage[20];
    int i;
 //  Scale s;
 //    public Recognition recog=new Recognition();
    /** Creates new form MainMenu */
    public Thinning() {
        biIn=new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
      
        i=0;
        biIn = null;        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    

    
    public BufferedImage thin(BufferedImage br)
    {
        
            w = br.getWidth(null);
            h = br.getHeight(null);
    try{
            biIn = br;
            biOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //    g2dIn = biIn.createGraphics();
            g2dOut = biOut.createGraphics();
            //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
          //  g2dIn.drawImage(image, 0, 0, w, h, null);
            //int th=128;
            inPixels = new int[h][w];
            for (int yy = 0; yy < h; yy++) {
                for (int xx = 0; xx < w; xx++) {
                   // inPixels[yy][xx] = biIn.getRGB(xx, yy);
                    if( biIn.getRGB(xx, yy)==-1) {
                    inPixels[yy][xx] = 0;
                }else {
                    inPixels[yy][xx] = 1;
                }
                   // System.out.print(inPixels[yy][xx]);
                 //   biIn.setRGB(xx, yy, inPixels[yy][xx]);
                }
               // System.out.println();
            }
          
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        
        //int col, r, g, b, gs, th;
        
        
        
      //  th = 128; // inversion (since data will be in black and image background in white) and threshold...
        // rgb to grayscale to binary conversion
       /* for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                col = inPixels[y][x];
                b = col & 0xff;
                g = (col >> 8) & 0xff;
                r = (col >> 16) & 0xff;
                gs = (r+g+b)/3;
                // temporarily save grayscale value too
                if(gs < th) {
                    inPixels[y][x] = 1;
                }else {
                    inPixels[y][x] = 0;
                }
            }
        }*/
        
        int markErasable[][] = new int[h][w];
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                markErasable[y][x] = 0;
            }
        }
        
        boolean dirty;
        // thinning...
        do{
            // dirty marks when there is no change in image. i.e. end of iterations.
            dirty = false;
            
            // check for all 4 templates...
            for(int t=0;t<4;t++) {

                // scan image...
                for(int y=1;y<h-1;y++) {
                    for(int x=1;x<w-1;x++) {
                        if(checkForTemplate(x, y, t, inPixels)) {
                            if(!isEndPoint(x, y, inPixels) && getConnectivityCount(x, y, inPixels)==1) {
                                markErasable[y][x] = 1;
                                dirty = true;
                            }
                        }
                    }
                }
                
                // now erase marked pixels
                for(int y=1;y<h-1;y++) {
                    for(int x=1;x<w-1;x++) {
                        if(markErasable[y][x]==1) {
                            markErasable[y][x] = 0;
                            inPixels[y][x] = 0;
                        }
                    }
                }
                
            }
        }while(dirty); 
                
        // paint output image with inverted colors ...
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                inPixels[y][x] = (inPixels[y][x]^1) * 0xFFFFFF;
                biOut.setRGB(x,y,inPixels[y][x]);
            }
        }
       
      /* s=new Scale(biOut);
       s.remove_space();
       biOut=s.scale();*/
        
        
      //  br_arr[i]=biOut;
       // i++;
//        try { 
//            ImageIO.write(biOut,"png",new File("d:\\thinning\\"+Math.random()+".png"));
//            System.out.println("Image File Written Successfully!");
//        }catch(Exception e) {
//            System.out.println("Error Writing File: " + e);
//        }
        return biOut;
      // recog.recogize(biOut);
    }
    
    public boolean checkForTemplate(int x, int y, int templateType, int pixMatrix[][]) {
        // check if center pixel has foreground color
        if(neighbour(x, y, 0, pixMatrix)==0) {
            return false;
        }
        switch(templateType) {
            case 0: // template t1
                if(neighbour(x, y, 7, pixMatrix)==1 && neighbour(x, y, 3, pixMatrix)==0) {
                    return true;
                }
                break;
            case 1: // template t2
                if(neighbour(x, y, 1, pixMatrix)==1 && neighbour(x, y, 5, pixMatrix)==0) {
                    return true;
                }
                break;
            case 2: // template t3
                if(neighbour(x, y, 3, pixMatrix)==1 && neighbour(x, y, 7, pixMatrix)==0) {
                    return true;
                }
                break;
            case 3: // template t4
                if(neighbour(x, y, 5, pixMatrix)==1 && neighbour(x, y, 1, pixMatrix)==0) {
                    return true;
                }
                break;
        }
        return false;
    }
    
    
    public int getConnectivityCount(int x, int y, int pixMatrix[][]) {
        int n[] = new int[9];
        int connectivity = 0;

        // n[0] is not required since it points to center pixel and we only need neighbours.
        for(int k=1;k<=8;k++) {
            n[k] = neighbour(x, y, k, pixMatrix);
        }
        
        connectivity += n[1] * (1 - n[2] * n[3]); // this is equivalent to n1 - (n1.n2.n3)
        connectivity += n[3] * (1 - n[4] * n[5]); // this is equivalent to n1 - (n1.n2.n3)
        connectivity += n[5] * (1 - n[6] * n[7]); // this is equivalent to n1 - (n1.n2.n3)
        connectivity += n[7] * (1 - n[8] * n[1]); // this is equivalent to n1 - (n1.n2.n3)

        return connectivity;
    }
    
    // checks if current center pixel is an end point..
    public boolean isEndPoint(int x, int y, int pixMatrix[][]) {
        int count = 0;
        for(int i=1;i<=8;i++) {
            if(neighbour(x, y, i, pixMatrix)==1) {
                count++;
            }
        }
        return count==1;
    }
    
    // Return the k indexed neighbour of a given pixel (x, y).
    // The k indexed neighbour table is given as below:
    // 4 3 2
    // 5 0 1
    // 6 7 8
    private int neighbour(int x, int y, int k, int pixMatrix[][]) {
        switch (k) {
            case 0:
                break;
            case 1:
                x++;
                break;
            case 2:
                x++;
                y--;
                break;
            case 3:
                y--;
                break;
            case 4:
                x--;
                y--;
                break;
            case 5:
                x--;
                break;
            case 6:
                x--;
                y++;
                break;
            case 7:
                y++;
                break;
            case 8:
                x++;
                y++;
                break;

        }
        if (x < 0) {
            x = 0;
        } else if (x >= w) {
            x = w - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y >= h) {
            y = h - 1;
        }
        return pixMatrix[y][x];
    }
    
 
    
    public BufferedImage[] thinSegments(BufferedImage[] seg)
    {
               
                BufferedImage[] thinSegments=new BufferedImage [seg.length];
                for(int i=0;i<seg.length;i++)
                {
                    thinSegments[i]=thin(seg[i]);
  
                }
                return thinSegments;
    }
    
}
