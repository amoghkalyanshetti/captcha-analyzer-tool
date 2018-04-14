/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Segmentation;
//import Preprocessing.*;
//import javax.imageio.ImageIO;
import java.awt.image.*;
//import java.io.*;
//import java.awt.Color;
//import java.util.*;
/**
 *
 * @author Shree Ganesh
 */
public class Connected extends DisconnectedSegment{
    Connected(int t,BufferedImage image)
    {
        super(0,image);
    }
    @Override
    public    BufferedImage[] generateSegments()
    {
        getArrayOfPixels();//no .of black pixels
        int t=(int) 0.15*max(arrayOfPixel);//15 percent of max no. of black pixel
        setThreshold(t);
        getXCoord();
        getYCoord();
        BufferedImage[] s=super.generateSegments();
        comparison();
        return s;
                
        
    }
    
     void comparison()
    {
        for(int i=0;i<XCoord[0].size();i++)
        {
            int d=(Integer)XCoord[1].elementAt(i)-(Integer)XCoord[0].elementAt(i);
            if(d>15)//set according to the width of characters
            {
               // System.out.println("Found");
                segmentTop(segments[i]);
            }
        }
    }
    int max(int[]n)//max number of an array
    {
        int max=0;
        for(int i=0;i<n.length;i++)
        {
            if(n[i]>max)
            {
                max=n[i];
            }
        }
        return max;
    }
    void segmentTop(BufferedImage img)
    {
        
    }
}
