
package Segmentation;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manali Kale
 */

import java.awt.Color;
import java.awt.image.*;
import java.util.Stack;
import java.awt.Graphics2D;

public class EightConnectivity 
{
    int xmax,xmin;
    int hh,ww;
    private int pixels_2d[][];
    BufferedImage image, tempSegments[], segments[];
    private int n, x;
    int black;
    
    //Constructor to initialize values
    public EightConnectivity (BufferedImage image)
    {
        this.image=image;
        ww=image.getWidth();
        hh=image.getHeight();
        getPixels2d();
        black=new Color(0,0,0).getRGB();
    }
     
    void getPixels2d()
    {
        pixels_2d=new int[hh+1][ww+1];
        for(int i=1;i<hh;i++)
        {
         
            for(int j=1;j<ww;j++)
            {
           
                int x=(new Color(image.getRGB(j, i)).getRed());
           
                if(x==0)
                {
                    pixels_2d[i][j]=1;
                    //System.out.print("1");
                }
                 else
                {
                    pixels_2d[i][j]=0; 
                    //System.out.print(" ");
                }
            }
           // System.out.println();
        }
    }
    
     public BufferedImage[] generateSegments()
    {
        int i=0;
        tempSegments=new BufferedImage[20];
        for(int j=0;j<ww;j++)
        {
            //x and y endpoints form a window for a character.
            xmin=32000;
            xmax=-32000;

            tempSegments[n]=new BufferedImage(70,50,BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g = tempSegments[n].createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0,0,70,50);
            
            
            // 8 connectivity algorithm to get continuous region of black pixel
            // here we find out the leftmost black pixel where we need to begin segmentation
            // After all the characters have been segmented, no black pixel will be found

            int min=32000;
            for(int h1=0;h1<hh;h1++)
            {
                for(int w1=j;w1<ww;w1++)   // w1=j ===> start from xmax of previously segmented character.....
                {
                    if(pixels_2d[h1][w1]==1)
                    {
                        if(min>w1)
                        
                        {
                            min=w1;
                            i=h1;
                            break;
                        }
                    }
                }
            } 
            j=min;
            if(j>=ww-1)
            {
                break;
            }
            
            Stack s1=new Stack();
            s1.push(i);
            s1.push(j);
        
            Object i1,j1;

            while(true)
            {					
                try
                {

                    j1=s1.pop();
                    i1=s1.pop();
                 
                 
                    if((Integer)j1>xmax)
                    {
                        xmax=(Integer)j1;
                    }
                    if((Integer)j1<xmin)
                    {
                        xmin=(Integer)j1;
                    }
                    
                }
                catch(Exception e)
                {
                    break;
                }

                pixels_2d[(Integer)i1][(Integer)j1]=0;  

                if(pixels_2d[(Integer)i1+1][(Integer)j1]==1)
                {   
		    s1.push((Integer)i1+1);
                    s1.push(j1);
                    if(((Integer)j1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1-xmin,(Integer)i1+1,black);     
                    
                }
                 if(pixels_2d[(Integer)i1-1][(Integer)j1]==1)
                {
		    s1.push((Integer)i1-1);
                    s1.push(j1);
                     if(((Integer)j1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1-xmin,(Integer)i1-1,black);  
                }
                if(pixels_2d[(Integer)i1][(Integer)j1-1]==1)
                {
                    s1.push(i1);
                    s1.push((Integer)j1-1);
                   if(((Integer)j1-1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1-1-xmin,(Integer)i1,black);  
                }
                if(pixels_2d[(Integer)i1][(Integer)j1+1]==1)
                {
                    s1.push(i1);
                    s1.push((Integer)j1+1);
                     if(((Integer)j1+1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1+1-xmin,(Integer)i1,black);  
                }
                if(pixels_2d[(Integer)i1-1][(Integer)j1-1]==1)
                {
                    s1.push((Integer)i1-1);
                    s1.push((Integer)j1-1);
                    if(((Integer)j1-1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1-1-xmin,(Integer)i1-1,black);  
                }
                if(pixels_2d[(Integer)i1-1][(Integer)j1+1]==1)
                {
                    s1.push((Integer)i1-1);
                    s1.push((Integer)j1+1);
                     if(((Integer)j1+1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1+1-xmin,(Integer)i1-1,black);  
                }
                if(pixels_2d[(Integer)i1+1][(Integer)j1-1]==1)
                {
                    s1.push((Integer)i1+1);
                    s1.push((Integer)j1-1);
                    if(((Integer)j1-1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1-1-xmin,(Integer)i1+1,black);  
                }
                if(pixels_2d[(Integer)i1+1][(Integer)j1+1]==1)
                {
                    s1.push((Integer)i1+1);
                    s1.push((Integer)j1+1);
                     if(((Integer)j1+1-xmin)>=0)
                    tempSegments[n].setRGB((Integer)j1+1-xmin,(Integer)i1+1,black);  
                }
                
            }
            
            
            //set j to the right extreme point so that next segmentation starts from there
            j=xmax; 
            n++;
        }
        segments=new BufferedImage[n];
        System.out.println("No of segments:"+n);
        System.arraycopy(tempSegments, 0, segments, 0, n);
        
        return segments;
    }  
}
