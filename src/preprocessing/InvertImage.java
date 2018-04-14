/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import java.awt.image.BufferedImage;

/**
 *
 * @author DHUMRA
 
 */
 
public class InvertImage {
    
    int hh,ww;
   // int outPixels[][];
    BufferedImage biOut;
 int outPixels[][];
    
     public BufferedImage conversion(BufferedImage biOut)
     {
         hh=biOut.getHeight();
         ww=biOut.getWidth();
         outPixels=new int[hh][ww];
          for(int i=0;i<hh;i++)
         {
           for(int j=0;j<ww;j++)
           {
                outPixels[i][j]=biOut.getRGB(j,i);//* 0xffffff;         //using RGB to get the pixel values
           }

         }
          int col, r, g, b, gs;
          for(int y=0;y<hh;y++) 
          {
                for(int x=0;x<ww;x++)
                {
                    outPixels[y][x] = (outPixels[y][x]^1) * 0xFFFFFF;
                    biOut.setRGB(x,y,outPixels[y][x]);
                }
            }
        
      
      
       for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                col = outPixels[y][x];
                b = col & 0xff;
                g = (col >> 8) & 0xff;
                r = (col >> 16) & 0xff;
                // in case grayscale not performed
                gs=(int) (r * 0.33 + g * 0.56 + b * 0.11);
                // temporarily save grayscale value too
                outPixels[y][x] = gs;
               
            }
        }
       
       
         for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                gs = outPixels[y][x];
                if (gs < 128) {
                    gs = 0;
                } else {
                    gs = 0xffffff;
                }
                outPixels[y][x] = gs;
                biOut.setRGB(x, y, outPixels[y][x]);
            }
        }
         //ImageInfo.buff_invert=biOut;
         return biOut;
     }
    
}
