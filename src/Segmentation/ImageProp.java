package Segmentation;

import java.awt.image.BufferedImage;

public class ImageProp 
{
    BufferedImage main,segments[];
    int Xstart,Xend,arrayOfPixel[],ww,hh,noOfZero=0,startYCoord,endYCoord=0;
    public ImageProp(BufferedImage img)
    {
        main=img;
        ww=img.getWidth();
        hh=img.getHeight();
        
    }
    void Properties()
    {
        getXstart();
        getXend();
        getProjection();
    }
     void getProjection()
   {
       
        arrayOfPixel=new int[Xend-Xstart+1];
        for(int i=Xstart;i<=Xend;i++)//counting no of pixels in one vertical line
        {
         int k=i-Xstart;
          arrayOfPixel[k]=0;
          for(int j=0;j<hh;j++)
          {
              int x=(main.getRGB(i, j));
              if(x==0xff000000) //if black found then increment
              {
                  ++arrayOfPixel[k];
              }
          }
		    
      }
   }
    void getXstart()
     {
         L1: for(int i=0;i<ww;i++)
         {
             for(int j=0;j<hh;j++)
             {
                 int x=main.getRGB(i, j);
                 if(x==0xff000000)
                 {
                     Xstart=i;
                     break L1;
                 }
             }
         }
         
     }
    void getXend()
     {
         L1: for(int i=ww-1;i>=0;i--)
         {
             for(int j=0;j<hh;j++)
             {
                 int x=(main.getRGB(i, j));
                 if(x==0xff000000)
                 {
                     Xend=i;
                     break L1;
                 }
             }
         }
         
     }
    void countOfZero()
    { 
	for(int i=0;i<arrayOfPixel.length;i++)
        {
            if(arrayOfPixel[i]==0)
            {
                ++noOfZero;
            }
        }
      
    }
    public void getYCoord()
    {
    
        endYCoord=0;
        L1:  for(int i=0;i<hh;i++)//logic to find height of character(y coordinate)
          {


              for(int j=0;j<ww;j++)
              {
                  if(main.getRGB(j, i)==0xff000000)//finding first black pixel
                  {

                      startYCoord=i;
                      break L1;//save time
                  }
              }
        }
         L2:  for(int i=hh-1;i>=startYCoord;i--)//logic to find height of character(y coordinate)
          {


              for(int j=0;j<ww;j++)
              {
                  if(main.getRGB(j, i)==0xff000000)//finding first black pixel
                  {

                      endYCoord=i;
                      break L2;//save time
                  }
              }
        }
    
         
       if(endYCoord==startYCoord) {
              endYCoord = hh;
          }
        // System.out.println("Start of y axis "+startYCoord+"   endinggg   "+endYCoord+" height of image : "+hh);
    }
}
