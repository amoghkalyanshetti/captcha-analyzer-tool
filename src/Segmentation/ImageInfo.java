/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Segmentation;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author DHUMRA
 */
public class ImageInfo {
    
    public int ww,hh;
    int inPixels[][],outPixels[][];
    Image image;
    public static BufferedImage biIn,OriginalImage,buff_grey,buff_bin,buff_ver,buff_hor,buff_dot,buff_chess,buff_invert,buff_hollow,buff_filter;
    BufferedImage biOut,temp;
    File Original_file;
    String filename;
    
    public ImageInfo()
    {
        
    }
    public ImageInfo(String filename)
    {
        this.filename=filename;
        image=Toolkit.getDefaultToolkit().getImage(filename); 
        Original_file=new File(filename);
        try {
            biIn=ImageIO.read(Original_file);    // convert image into BufferedImage object
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        OriginalImage=biIn;
        ww=biIn.getWidth();
        hh=biIn.getHeight();
        initialize();
     //   System.out.println("ww ="+ww+" hh= "+hh);
        try {
            boolean write = ImageIO.write(biIn, "png", new File("e:\\binarization\\Original.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  public void initialize()
  {
       buff_grey=buff_bin=buff_ver=buff_hor=buff_dot=buff_chess=buff_invert=buff_hollow=buff_filter=null;
  }
    
   public BufferedImage getBufferedImage(BufferedImage br)      //copy one bufferimage to another bufferedimage oject like coping one object to another  
   {
       hh=br.getHeight();
       ww=br.getWidth();
       inPixels=new int[hh][ww];
       temp=new BufferedImage(ww,hh,BufferedImage.TYPE_INT_RGB);
       for (int yy = 0; yy < hh; yy++) 
         {
                for (int xx = 0; xx < ww; xx++)
                {
                    inPixels[yy][xx] = br.getRGB(xx, yy);
                    temp.setRGB(xx, yy, inPixels[yy][xx]);
                }
            }
       return temp;
   }
   
   
   
   public void getInpixels(BufferedImage biIn)    //Create 2 dimensional array of image in RGB values
    {
       
       ww=biIn.getWidth();
        hh=biIn.getHeight();
        {
        inPixels=new int[hh][ww];
         for(int i=0;i<hh;i++)
         {
           for(int j=0;j<ww;j++)
           {
                inPixels[i][j]=biIn.getRGB(j,i) ;//* 0xffffff;         //using RGB to get the pixel values
           }

         }
      }
          
        
    }
    
    public void display()
    {
        for(int i=0;i<hh;i++)
         {
           for(int j=0;j<ww;j++)
           {
                System.out.print(inPixels[i][j]);      
           }
            System.out.println();
         }
    }
    
    public BufferedImage toGray(BufferedImage original)     //Conversion of RGB to Grey
    {
 
        int alpha, red, green, blue;
        int newPixel;
        //System.out.println("TYPE of IMAGE:  "+original.);
        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
 
                // Write pixels into image
                lum.setRGB(i, j, newPixel);
 
            }
        }
       
        buff_grey=lum;
        return lum;
 
    }
   
    public int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
     
      public int otsuTreshold(BufferedImage original)       // Calculates Threshold values (dynamically using Otsu)
      {
 
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
 
        return threshold;
 
    }
 
      public int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
 
    public BufferedImage binarize(BufferedImage original,int threshold)  //Conversion of grey to Binary depending upon threshold value passed
    {
 
        int red;
        int newPixel;
 
      //  int threshold = otsuTreshold(original);
 
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
		//System.out.println(newPixel);
                binarized.setRGB(i, j, newPixel);
 
            }
        }
        buff_bin=binarized;
        return binarized;
 
    }
     
    
     public BufferedImage rmvNoise_horLine_1Pixel(BufferedImage br){
	int i,j;
	Boolean p1=false,p11=false,p2=false,p22=false; 	//data structure to scan status of Neighbouring Pixels
        BufferedImage binarized=getBufferedImage(br);
	
	for(j=1; j<binarized.getHeight()-1; j++) //start from leaving 1 pixel boundary
	{			//start search excluding the boundry lines
		for (i=1; i<binarized.getWidth()-1; i++)
		{
			p1=false;p11=false;p2=false;p22=false; 
			if(binarized.getRGB(i, j)==-16777216)//Black pixel found=TRUE
			{
				//find color status of all neighbouring pixels
 				//TRUE=BLACK & FALSE=WHITE
				if(binarized.getRGB(i, j-1)==-16777216) p1=true;
				if(binarized.getRGB(i-1, j)==-16777216) p11=true;
				if(binarized.getRGB(i, j+1)==-16777216) p2=true;
				if(binarized.getRGB(i+1, j)==-16777216) p22=true;
				
				//conditions to progress with changing BLACK->WHITE
				if((p11&&!p1&&!p2&&p22)||(!p11&&!p1&&!p2&&!p22)||(p11&&!p1&&!p2&&!p22)||(!p11&&!p1&&!p2&&p22)/*extreme condition*/||(p11&&!p1&&!p2&&p22))				
				binarized.setRGB(i, j,-1);//set current to WHITE

				//else leave it as it is & move to next pixel
			}
		}
	}
        buff_hor=binarized;
        return binarized;
    }//func. ends
    
    public BufferedImage rmvNoise_verLine_1Pixel(BufferedImage br){
	int i,j;
	Boolean p1=false,p11=false,p2=false,p22=false; 	//data structure to scan status of Neighbouring Pixels
	 BufferedImage binarized=getBufferedImage(br);
	for(i=0; i<binarized.getWidth()-1; i++) 
	{			//start search excluding the boundry lines
		for(j=0; j<binarized.getHeight()-1; j++) 
		{
			p1=false;p11=false;p2=false;p22=false; 
			if(binarized.getRGB(i, j)==-16777216)//Black pixel found=TRUE
			{
				//find color status of all neighbouring pixels
 				//TRUE=BLACK & FALSE=WHITE
				if(binarized.getRGB(i-1, j)==-16777216) p1=true;
				if(binarized.getRGB(i, j-1)==-16777216) p11=true;
				if(binarized.getRGB(i+1, j)==-16777216) p2=true;
				if(binarized.getRGB(i, j+1)==-16777216) p22=true;
				
				//conditions to progress with changing BLACK->WHITE
				if((!p1&&!p11&&!p2&&!p22)||(!p1&&p11&&!p2&&!p22)||(!p1&&!p11&&!p2&&p22)/*extreme condition*/||(p11&&!p1&&!p2&&p22))				
				binarized.setRGB(i, j,-1);//set current to WHITE

				//else leave it as it is & move to next pixel
			}
		}
	}
        buff_ver=binarized;
         return binarized;
    }//func. ends
    
    public BufferedImage clrBndry(BufferedImage br){		//func. Used to remove all 4 BLACK boundary of image
	
	int i,newPixel,alpha;
         BufferedImage binarized=getBufferedImage(br);
	for(i=0; i<binarized.getHeight(); i++) {	
							
		//1st left boundry
                binarized.setRGB(0, i,-1);										
		//2nd right boundry
                binarized.setRGB((binarized.getWidth()-1), i,-1);
	}

	for(i=0; i<binarized.getWidth(); i++) {	

		//3rd upper boundry
                binarized.setRGB(i, 0,-1);	
		//4th lower boundry
                binarized.setRGB(i,(binarized.getHeight()-1),-1);
	}
        return binarized;
    }
     public BufferedImage connectivityFiller(BufferedImage br){
	int i,j;
	Boolean p1=false,p11=false,p2=false,p22=false; 	//data structure to scan status of Neighbouring Pixels
	 BufferedImage binarized=getBufferedImage(br);
	for(i=1; i<binarized.getWidth()-1; i++) 
	{			//start search excluding the boundry lines
		for(j=1; j<binarized.getHeight()-1; j++) 
		{
			p1=false;p11=false;p2=false;p22=false; 
			if(binarized.getRGB(i, j)==-1)//WHITE pixel found=TRUE
			{
				//find color status of all neighbouring pixels
 				//TRUE=BLACK & FALSE=WHITE
				if(binarized.getRGB(i-1, j)==-16777216) p1=true;
				if(binarized.getRGB(i, j-1)==-16777216) p11=true;
				if(binarized.getRGB(i+1, j)==-16777216) p2=true;
				if(binarized.getRGB(i, j+1)==-16777216) p22=true;
				
				//conditions to progress with changing WHITE->BLACK
				if((p1&&p11&&p2&&p22)||(p1&&!p11&&p2&&!p22)||(!p1&&p11&&!p2&&p22))				
				binarized.setRGB(i, j,-16777216);//set current to BLACK

				//else leave it as it is & move to next pixel
                        }
                }
        }
        
        buff_filter=binarized;
        return binarized;
    }
     
     public BufferedImage dot_removal(BufferedImage br,int pixcnt)
   {
         int pixelcnt=0;
         Object coor;
         //System.out.println("wid"+width+" ht : "+height);
         int height=br.getHeight();
         int width=br.getWidth();
         BufferedImage binarized=getBufferedImage(br);
         int pixels[]=new int[height*width];
         
         for( int i=0;i<height;i++)
            {
                for(int j=0;j<width;j++)
                {
                    pixels[i*width+j]=binarized.getRGB(j,i);
                }
            }
         for(int i=1;i<height-1;i++)
         {
            for(int j=1;j<width-1;j++)
			{
					if(pixels[i*width+j]==-16777216)  // check black pixel...
					{
                                            pixelcnt=0;  // counts total no of continuous black pixels..
					  Stack stk=new Stack();
						//Info bk=new Info(i*width+j,pixels[i*width+j]);
						stk.push(i*width+j);  // push  1D array index corresponding to black pixel on stack...
						while(true)
						{
							//Info ref=stk.pop();
							//System.out.println("A");
                            try
                            {
							    coor=stk.pop();  // generates an exception if stack is empty...
                            }
                            catch(Exception e)
                            {
                                break;
                            }
                          //  System.out.println("coor"+coor);
							pixels[(Integer)coor]=-8000000;  //make popped pixel green and check its 8 neighbors..
							pixelcnt++;
                            if((Integer)coor+1 < (height*width))
                            {
							if(pixels[(Integer)coor+1]==-16777216)   // right side pixel
							{
								stk.push((Integer)coor+1);
							}
                            }
                            if((Integer)coor-1 > 0)
                            {
							if(pixels[(Integer)coor-1]==-16777216)  //left side pixel
							{
								stk.push((Integer)coor-1);
							}
                            }
                            if((((Integer)coor+width)<(height*width)))
                            {
							if(pixels[(Integer)coor+width]==-16777216)  // bottom pixel
							{
								
                                stk.push((Integer)coor+width);
							}
                            }
                            if((((Integer)coor-width)>(0)))
                            {
							if(pixels[(Integer)coor-width]==-16777216)  // top pixel
							{
								
                                stk.push((Integer)coor-width);
							}
                            }
                            if((((Integer)coor+width+1)<(height*width)))
                            {
							if(pixels[(Integer)coor+width+1]==-16777216) // bottom right pizel
							{
								
                                stk.push((Integer)coor+width+1);
							}
                            }
                            if((((Integer)coor+width-1)<(height*width)))
                            {
							if(pixels[(Integer)coor+width-1]==-16777216) // bottom left pixel
							{
							
                                stk.push((Integer)coor+width-1);
							}
                            }
                            if((((Integer)coor-width+1)>(0)))
                            {
							if(pixels[(Integer)coor-width+1]==-16777216) // top right pixel
							{
                               
                                    stk.push((Integer)coor-width+1);
							}
                            }
                            if((((Integer)coor-width-1)>(0)))
                            {
							if(pixels[(Integer)coor-width-1]==-16777216) // top left pixel....
							{
						
                                stk.push((Integer)coor-width-1);
							}
                            }

						}

     /* if the total number of continuous black pixels encountered are less than 250
        then all the pixels that are green should be turned to white as they form a dot....
      * otherwise all of them should be turned black as they are a part of character... */

						for(int ii=1;ii<height-1;ii++)
						{
							for(int jj=1;jj<width-1;jj++)
							{
								if(pixels[ii*width+jj]==-8000000)
								{
										if(pixelcnt<pixcnt)  //previous count was 100....
										pixels[ii*width+jj]=-1;
										else
										pixels[ii*width+jj]=-16777216;  // turn to black..
								}

							}
						}



					}
				}

			}

         for( int i=0;i<height;i++)
            {
                for(int j=0;j<width;j++)
                {
                    binarized.setRGB(j,i,pixels[i*width+j]);
                }
            }
         buff_dot=binarized;
         return binarized;
   }
     
     
   public BufferedImage spotEraser(BufferedImage br)
    {
       
        //applying the windowing
        BufferedImage binarized=getBufferedImage(br);
        for(int j=0;j<binarized.getHeight()-6;j++)
        {
             for(int i=0;i<binarized.getWidth()-6;i++)
             {
                 int flag=0;
                 //for topmost edge of the window

                 int y=j;
                 for(int x=i;x<i+6;x++)
                 {
                    if(binarized.getRGB(x,y)==-16777216)
                        flag=1;                             //window overlap with the character
                 }

                 //for bottommost edge of the window
                 y=j+5;
                 for(int x=i;x<i+6;x++)
                 {
                    if(binarized.getRGB(x,y)==-16777216)
                        flag=1;                             //window overlap with the character
                 }

                 //for leftmost edge of the window
                 int x=i;
                 for(y=j;y<j+6;y++)
                 {
                     if(binarized.getRGB(x,y)==-16777216)
                         flag=1;                            //window overlap with the character
                 }

                 //for rightmost edge of the window
                 x=i+5;
                 for(y=j;y<j+6;y++)
                 {
                     if(binarized.getRGB(x,y)==-16777216)
                         flag=1;                            //window overlap with the character
                 }

                 if(flag==0)                //window may contain noise in the form of arcs
                 {
                    for(y=j+1;y<j+6;y++)
                        for(x=i+1;x<i+6;x++)
                        {
                            binarized.setRGB(x, y, -1);
                            //binarized.inpixels[x][y]=binarized.backColor;         //removal of noise
                          //  tempBr.setRGB(y, x, binarized.getRGB(x, y));
                        }
                 }
             }
        }
        buff_filter=binarized;
      return binarized;
    }  
   
     
      public BufferedImage filter(BufferedImage br)
   {
      
        int col, r, g, b, gs[], temp;
        gs = new int[9];
        hh=br.getHeight();
        ww=br.getWidth();
        outPixels=new int[hh][ww];
         inPixels=new int[hh][ww];
          BufferedImage biOut=getBufferedImage(br);
        for(int i=0;i<hh;i++)
         {
           for(int j=0;j<ww;j++)
           {
                inPixels[i][j]=biOut.getRGB(j,i) ;//* 0xffffff;         //using RGB to get the pixel values
           }

         }
        // rgb to grayscale to binary conversion
        for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                col = inPixels[y][x];
                b = col & 0xff;
                g = (col >> 8) & 0xff;
                r = (col >> 16) & 0xff;
                r = g = b = (r + g + b) / 3;
                inPixels[y][x] = (r << 16) | (g << 8) | b;
            }
        }

        for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                int index = 0;

                if (y == 0 || y == (hh - 1) || x == 0 || x == (ww - 1)) {
                    outPixels[y][x] = inPixels[y][x];
                    continue;
                }

                for (int yy = y - 1; yy <= y + 1; yy++) {
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        gs[index] = inPixels[yy][xx] & 0xff;
                        index++;
                    }
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = i + 1; j < 9; j++) {
                        if (gs[i] > gs[j]) {
                            temp = gs[i];
                            gs[i] = gs[j];
                            gs[j] = temp;
                        }
                    }
                }
                // median
                r = g = b = gs[4];
                outPixels[y][x] = (r << 16) | (g << 8) | b;
                if (inPixels[y][x] != outPixels[y][x]) {
          //          medianNoiseCount++;
                }
                biOut.setRGB(x, y, outPixels[y][x]);
            }
        }
     
     buff_filter=biOut;
     return biOut;
   }
     
      
    public BufferedImage rotate(int degrees,BufferedImage br) {
        hh=br.getHeight();
        ww=br.getWidth();
        outPixels=new int[hh][ww];
        getInpixels(br);
         BufferedImage biOut=getBufferedImage(br);
        double rad = (degrees * 3.14) / 180.0;
        for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                outPixels[y][x] = 0xffffff; // default background is white...
            }
        }

        for (int j = 0; j < hh; j++) {
            for (int i = 0; i < ww; i++) {
                int y = j;
                int x = (int) (i - (hh - j) * Math.tan(rad));
                if (x >= 0 && x < ww) {
                    outPixels[y][x] = inPixels[j][i];
                }
            }
        }

        for (int y = 0; y < hh; y++) {
            for (int x = 0; x < ww; x++) {
                biOut.setRGB(x, y, outPixels[y][x]);
            }
        }
        return biOut;
    }
}
    
   

