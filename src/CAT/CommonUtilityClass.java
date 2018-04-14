package CAT;
import CharacterRecognition.SymbolSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
public class CommonUtilityClass 
{
   public void fileWriter(BufferedImage targetImgToWrite,String path,String fileName)
    {
        if (targetImgToWrite!=null)
        {
            String fullPath=path+fileName;
            try 
            { 
             ImageIO.write(targetImgToWrite, "jpg",new File(fullPath));//Replace this with variable fullPath
            } 
            catch (IOException ex)
            {
              System.out.println("File cannot be written");      
            }
        }
    }
    
    void fileWriter(BufferedImage targetImgToWrite[],String path,String fileName)
    {
        String fullPath=path+fileName;
        if(targetImgToWrite!=null)
        {
            for(int i=0;i<targetImgToWrite.length;i++)
            {
                try 
                { 
                 ImageIO.write(targetImgToWrite[i], "jpg",new File(fullPath+i+".jpg"));//Replace this with variable fullPath
                } 
                catch (IOException ex)
                {
                  System.out.println("File cannot be written");      
                }
            }
        }
    }
    public int colorToRGB(int alpha, int red, int green, int blue)
    {
        int newPixel = alpha;
        newPixel  = newPixel << 8;
        newPixel += red; 
        newPixel  = newPixel << 8;
        newPixel += green;
        newPixel  = newPixel << 8;
        newPixel += blue;
        
        return newPixel;
    }
    //printBufferedImage is a temporary function on temporary basis to check the output
    public void printBufferedImg(BufferedImage img)
   {
        int h=img.getHeight();
        int w=img.getWidth();
        for(int i=0;i<h;i++)
        {
            for(int j=0;j<w;j++)
            {
                if(255==new Color(img.getRGB(j,i)).getGreen())
                  System.out.print(" ");
                else 
                  System.out.print("1");
            }
           System.out.println();
        } 
   }
    
     public  BufferedImage[] scale(BufferedImage sbi[],int w ,int h)
    {
        BufferedImage dbi[] = new BufferedImage[sbi.length];
        for(int i=0;i<sbi.length;i++)
        {
            if(sbi[i]!=null)
            {
            double fWidth=(double)w/sbi[i].getWidth();//Scaling factor
            double fHeight=(double)h/sbi[i].getHeight();
            dbi[i] = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);//20x20 image
            Graphics2D g = dbi[i].createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi[i], at);
            }
        }
        return dbi;
        
    }
      public BufferedImage addExtra(BufferedImage img, int f, int b) // add extra pixels to image 
    {
        int h = img.getHeight();
        int w = img.getWidth();
        BufferedImage out = new BufferedImage(w + f + b, h, BufferedImage.TYPE_INT_RGB);
        out = wightBackground(out);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                out.setRGB(i + f, j, img.getRGB(i, j));
            }
        }
        return out;
    }
     
     BufferedImage wightBackground(BufferedImage img) //
    {
        int i, j;
        int h = img.getHeight(), w = img.getWidth();
        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                img.setRGB(i, j, 0xffffff);
            }
        }
        return img;
    }
     
     public BufferedImage rotateCharacter(int degrees, BufferedImage img) // rotate the captcha
    {
        int width = img.getWidth(), height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double rad = Math.toRadians(degrees);
        wightBackground(out);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int y = j;
                int x = (int) (i - (height - j) * Math.tan(rad));

                if (x >= 0 && x < width) {
                    out.setRGB(x, y, img.getRGB(i, j));
                }
            }
        }
        return out;
    }
   //To scale the image into 20x20
    public  BufferedImage[] scale(BufferedImage sbi[])
    {
        BufferedImage dbi[] = new BufferedImage[sbi.length];
        for(int i=0;i<sbi.length;i++)
        {
            if(sbi[i]!=null)
            {
            double fWidth=(double)20/sbi[i].getWidth();//Scaling factor
            double fHeight=(double)20/sbi[i].getHeight();
            dbi[i] = new BufferedImage(20, 20, BufferedImage.TYPE_BYTE_GRAY);//20x20 image
            Graphics2D g = dbi[i].createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi[i], at);
            }
        }
        return dbi;
    }
    public void readDATFile(String filename) throws IOException
    {
        
       
        System.out.println("File: "+filename);
      FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(filename);
            ObjectInputStream charObjIn = new ObjectInputStream(fileIn);
           
            for(int i=0;i<62;i++)
           {
               Main.ss[i]=new SymbolSet();
                Main.ss[i].wt = (int[][]) charObjIn.readObject();
           
           }  
         //  JOptionPane.showMessageDialog(this,"DAT FILE LOADED");
        } catch (Exception ex) {
           //System.out.println("Error while loading dat file\nFile may be not present");
           //System.exit(0);
            JOptionPane.showMessageDialog(null, "can't load dat file", null, JOptionPane.ERROR_MESSAGE);
         

        } finally {
            try {
                if(fileIn!=null)
                    fileIn.close();
//                   System.exit(0);
            } catch (IOException ex) {
               // Logger.getLogger(class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
   
}
