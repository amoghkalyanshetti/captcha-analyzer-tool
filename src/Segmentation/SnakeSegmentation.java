package Segmentation;
import CAT.Main;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public final class SnakeSegmentation 
{
    public static BufferedImage binOut;
    private static int xFirst, xLast,temp;
    private static BufferedImage image, tempSegments[], segments[];
    private static boolean got_bit=false;
    int arrayOfPixel[];
    private static int ww,hh, height,yCoord, k,start;
    private static boolean visited[][];
	
   
   
    public SnakeSegmentation(BufferedImage img)
    {
        SnakeSegmentation.image=img;
        ww=image.getWidth();
        hh=image.getHeight();
        visited=new boolean[ww][hh];
        getArrayOfPixels();
                
        height=findHeight();
      //  Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    int findHeight()
    {
        int max=0;
           for(int i=0;i<arrayOfPixel.length;i++)
           {
               if(arrayOfPixel[i]>max)
               {
                   max=arrayOfPixel[i];
               }
           }
           return max;
    }
	
    public  void getArrayOfPixels()
    {
        Main.tracker.add(new Object(){}.getClass().getName());
       ww=image.getWidth();//ImageInfo
        hh=image.getHeight();
        arrayOfPixel=new int[ww];
        for(int i=0;i<ww;i++)//counting no of pixels in one vertical line
        {
         
          arrayOfPixel[i]=0;
          for(int j=0;j<hh;j++)
          {
              int x=(new Color(image.getRGB(i, j)).getRed());
              if(x==0) //if black found then increment
              {
                  ++arrayOfPixel[i];
              }
          }
		    
      }
	  
    }
    
	
    public BufferedImage[] generateSegments()
    {
        tempSegments=new BufferedImage[10];
          
    for(int i=0;i<ww;i++)
    {
        for(int j=0;j<hh;j++)
        {
           if((new Color(image.getRGB(i, j)).getRed())==0)
            {
              
                got_bit=true;
		xFirst=i; 		  // First Segmentation Line
                
            } 
            if(got_bit)
            {
                break;
            }
        }
    }
 
    got_bit=false;
    for(int i=ww-1;i>0;i--)
    {
        for(int j=0;j<hh;j++)
        {
            if((new Color(image.getRGB(i, j)).getRed())==0)
            {
                got_bit=true;
		xLast=i; 		// Last Segementation Line  
   
            } 
            if(got_bit)
            {
               break;
            }
        }
    }
    //System.out.println("snake entered");
    got_bit=false;
    start=xFirst;
    for(int i=xFirst; i<=xLast;i++)
    {
         
        for(int j=0;j<hh-1;j++)
        {
           
            if(visited[i][j]==false)
            {
                visited[i][j]=true;
                    
                if((new Color(image.getRGB(i, j+1)).getRed())==0)
                {
            
                    if(j<yCoord) {
                        got_bit=false;
                    }
                    if(got_bit==false)
                    {
                        yCoord=j;
                        got_bit=true;
                    }
                    if((new Color(image.getRGB(i+1, j)).getRed())==0)
                    {
                        
                        break ;
                    }
                    else
                    {
                     temp=i;
                     i=i+1;
                     j=j-1;
                    }
                     
                }
               
                if(j==(hh-2))
                {
                    try
                    {
                        tempSegments[k]=image.getSubimage(start,yCoord+1,i-start,height+3);  //x,y,width,height
                        
                        if(i-start>5)
                        {
                           // ImageIO.write(tempSegments[k],"jpg",new File("D:\\Project\\segment\\"+(i)+".jpg"));
                            k++;
                        }
                    }
                    catch(Exception e)
                    {
                    }
                    if(i-start==1)
                    {
                        start=i+1;
                    }
                    else
                    {
                        start=temp+1;
                    }
                 
                i=start;
                got_bit=false;
                yCoord=0;
                 
                }
            }
             else
            {
                i=i+1;  
            }
              
        }
    }
    segments=new BufferedImage[k];
    //System.out.println("No of segments:"+k);
    System.arraycopy(tempSegments, 0, segments, 0, k);
    k=0;
    Main.tracker.add(new Object(){}.getClass().getName());
    return segments;
  } 
         
}
     
    


     
     
     



    

