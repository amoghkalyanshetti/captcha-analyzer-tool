package Segmentation;
import CAT.Main;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class DisconnectedSegment
{
    public static BufferedImage binOut;
    protected BufferedImage segments[];
    private static int threshold;
    public BufferedImage image;
    static int startYCoord=0,endYCoord=0;
    int arrayOfPixel[];
    private static int ww,hh,minSize;//Must be in the ImageInfo class
    public int size;
    Vector XCoord[]=new Vector[2];//saving space
    static int index=0;

    public DisconnectedSegment(int t,BufferedImage image)
    {
        minSize=2;
        threshold=t;
        this.image=image;   
       // Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    public DisconnectedSegment(int r,int t,BufferedImage image)
    {
        minSize=r;
        threshold=t;
        this.image=image;   
     //   Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    
    
    
    public void getProjection()
    {
        getArrayOfPixels();
        getXCoord();
        getYCoord();
    }


    protected void getArrayOfPixels()
    {
        ww=image.getWidth();//ImageInfo
        hh=image.getHeight();
        arrayOfPixel=new int[ww];
        for(int i=0;i<ww;i++)//counting no of pixels in one vertical line
        {
            arrayOfPixel[i]=0;
            for(int j=0;j<hh;j++)
            {
                int x=(image.getRGB(i, j));
                if(x==0xff000000) //if black found then increment
                {
                    ++arrayOfPixel[i];
                }
            }

        }
        for(int i=0;i<arrayOfPixel.length;i++)//to display the array of pixel
        {
       // System.out.println(i+" "+arrayOfPixel[i]);
        }

    }

    @SuppressWarnings("empty-statement")
    protected final void getXCoord()
    {

        XCoord[0]=new Vector();//starting x coordinates of character
        XCoord[1]=new Vector();//ending x coordinate of character

        //System.out.println(threshold);
        for(int i=0;i<(arrayOfPixel.length);i++)//main logic for finding strt n end point
        {
            if(arrayOfPixel[i]>threshold)
            {
                XCoord[0].addElement(i);//startPoint if have choosed 0 for complete disconnected image
                try
                {
                    while(arrayOfPixel[++i]>threshold);//continue if not 0
                    XCoord[1].addElement(i);//endpoint
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    XCoord[1].addElement(i-1);
                }
                if(((Integer)XCoord[1].elementAt(XCoord[0].size()-1)-(Integer)XCoord[0].elementAt(XCoord[0].size()-1))<=minSize)
                {
                    XCoord[0].removeElementAt(XCoord[0].size()-1);
                    XCoord[1].removeElementAt(XCoord[1].size()-1);
                   // System.out.println("Removed a small segment just now");
                }

            }
        }
    }

    protected final void getYCoord()
    {
        endYCoord=0;
        L1:for(int i=0;i<hh;i++)//logic to find height of character(y coordinate)
        {
            for(int j=0;j<ww;j++)
            {
                if(image.getRGB(j, i)==0xff000000)//finding first black pixel
                {
                    startYCoord=i;
                    break L1;//save time
                }
            }
        }
        L2:for(int i=hh-1;i>=startYCoord;i--)//logic to find height of character(y coordinate)
        {
            for(int j=0;j<ww;j++)
            {
                if(image.getRGB(j, i)==0xff000000)//finding first black pixel
                {
                    endYCoord=i;
                    break L2;//save time
                }
            }
        }
        if(endYCoord==startYCoord)
        {
            endYCoord = hh;
        }
        //System.out.println("Start of y axis "+startYCoord+"   endinggg   "+endYCoord+" height of image : "+hh);
    }


    public BufferedImage[] generateSegments()
    {

        size=XCoord[0].size();
        //System.out.println("Size no of segements : "+XCoord[0].size());
        segments=new BufferedImage[size];
        //System.out.println(segments.length);
        for(int i=0;i<segments.length;i++)
        {
            //System.out.println(i+" = "+(Integer)XCoord[1].elementAt(i));
            segments[i]=image.getSubimage((Integer)XCoord[0].elementAt(i), startYCoord,((Integer)XCoord[1].elementAt(i))-((Integer)XCoord[0].elementAt(i)), endYCoord-startYCoord+1);
            //segement(Xleft,Yleft,width,height);
            try{

                ImageIO.write(segments[i],"jpg",new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\"+ DisconnectedSegment.index+i +".jpg"));
            }
            catch(IOException e)
            {
               // System.out.println("Failed");
            }


        }
        DisconnectedSegment.index=DisconnectedSegment.index+segments.length;
        Main.tracker.add(new Object(){}.getClass().getName());
        return segments;
    }

/*void printImage()
{
// System.out.println("No of total segments: "+segments.length);
for(int i=0;i<segments.length;i++)
{
try{

ImageIO.write(segments[i],"jpg",new File("D:\\Project\\segment\\Botdetect\\"+ DisconnectedSegment.index+i +".jpg"));
}
catch(IOException e)
{
System.out.println("Failed");
}
}
}*/
    BufferedImage[] shift(BufferedImage[] seg,BufferedImage[] org,int i)
    {
        int l=org.length+seg.length-1;
       // System.out.println("length........."+l);
        BufferedImage temp[]=new BufferedImage[l];
        System.arraycopy(org, 0, temp, 0, org.length);
       // System.out.println(temp.length);
        for(int j=org.length-1;j>=i+1;j--)//creating space by shifting
        {

            temp[j+seg.length-1]=temp[j];
        }
        System.arraycopy(seg, 0, temp, i, seg.length);
        return temp;
    }
   final void setThreshold(int t)
   {
       threshold=t;
   }
   final int getThreshold()
   {
       return threshold;
   }
}
