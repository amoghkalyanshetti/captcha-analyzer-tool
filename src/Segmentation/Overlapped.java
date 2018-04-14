/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Segmentation;
import CAT.Main;
import java.awt.image.*;
/**
 *
 * @author Shree Ganesh
 */
public final class Overlapped extends DisconnectedSegment
{
    int width,r;
    public Overlapped(int w,BufferedImage img)
    {
        super(0,img);
        width=w;
       // Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        
    }
    public Overlapped(int r,int w,BufferedImage img)
    {
        super(r,0,img);
        width=w;
        this.r=r;
       // Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    @Override
     public BufferedImage[] generateSegments()
    {
        getArrayOfPixels();
        getXCoord();
        getYCoord();
        segments=super.generateSegments();
        comparison();
        Main.tracker.add(new Object(){}.getClass().getName());
        //System.out.println("total no of segments: ............................................................"+segments.length);
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
               
                //System.out.println("Found");
                j++;
                
            }
        }
        for(int i=j-1;i>=0;i--)
        {
             segments=shift(s[i],segments,index[i]);
        }
        
        
    }
    
}
