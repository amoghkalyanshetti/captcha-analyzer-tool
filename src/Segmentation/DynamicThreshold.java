package Segmentation;
import java.awt.image.BufferedImage;

public final class DynamicThreshold extends DisconnectedSegment
{
    int width;
    int noOfl;
    boolean cmp;
    int r;
    public DynamicThreshold(int l,BufferedImage image,boolean cmp)
    {
        super(0,image);//initial value of threshold 0
        noOfl=l;
        width=20;
        this.cmp=cmp;
    }
    public DynamicThreshold(int w,int l,BufferedImage image,boolean cmp)
    {
        super(0,image);//initial value of threshold 0
        noOfl=l;
        width=w;
        this.cmp=cmp;
    }
    public DynamicThreshold(int r,int w,int l,BufferedImage image,boolean cmp)
    {
        super(r,0,image);//initial value of threshold 0
        noOfl=l;
        width=w;
        this.cmp=cmp;
        this.r=r;
    }
    int minThreshold()//to find next minimum threshold
    {
        int min=getThreshold();
        int threshold=1000;//temp
        for(int i=0;i<(arrayOfPixel.length);i++)
        {
            if(min<arrayOfPixel[i]&&arrayOfPixel[i]<threshold)
            {
                threshold=arrayOfPixel[i];
            }
        }
        return threshold;
     }
    @Override
    public    BufferedImage[] generateSegments()
    {
        this.getArrayOfPixels();
        this.getXCoord();
        this.getYCoord();
        while(XCoord[0].size()<noOfl)//no of segments less than 4
        {
           setThreshold(minThreshold());//setting threshold to next minimum value
           XCoord[0].removeAllElements();//clearing array
           XCoord[1].removeAllElements();
           getXCoord();
          // System.out.println("Size of XCoord ="+XCoord[0].size());
        }
        segments=super.generateSegments();//after finding threshold
        if(cmp)
        {
            comparison();
        }
        System.out.println("Threshold "+getThreshold());
        return segments;
    }

    void comparison()
    {
       
        int index[]=new int[5];//storing index
        BufferedImage s[][]=new BufferedImage[5][];//storing segments
        int j=0;
        for(int i=0;i<XCoord[0].size();i++)
        {
            int d=(Integer)XCoord[1].elementAt(i)-(Integer)XCoord[0].elementAt(i);
            if(d>width)
            {
               DynamicThreshold temp=new DynamicThreshold(r,width,2,segments[i],true);
                s[j]=temp.generateSegments();
                index[j]=i;
               
                System.out.println("Found");
                j++;
                
            }
        }
        for(int i=j-1;i>=0;i--)
        {
             segments=shift(s[i],segments,index[i]);
        }
    }
}
    

