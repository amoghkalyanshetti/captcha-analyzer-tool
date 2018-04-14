/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Segmentation;

import CAT.CommonUtilityClass;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 *
 * @author Shree Ganesh
 */
public class FloodFill {
    public BufferedImage img;
    int orignalColor;
    int desiredColor;
    Stack cordValues;
    public FloodFill(BufferedImage img,int desiredColor,int pixelColor)
    {
     this.img=img;
     orignalColor=pixelColor;
     this.desiredColor=desiredColor;
     cordValues=new Stack();
    }
    public FloodFill(BufferedImage b)
    {
        img=b;
    }
    
    public void fill(int x,int y,BufferedImage grayImg) 
    {
       //System.out.println(grayImg.getHeight()+" "+grayImg.getWidth()+"gray");
       //System.out.println(img.getHeight()+"  "+img.getWidth()+"Orignal");
       CommonUtilityClass util=new  CommonUtilityClass();
       //util.fileWriter(grayImg,"D:\\","imgNew.jpg");
        cordValues.push(y);
        cordValues.push(x);
        int xObject,yObject;
        int t=(grayImg.getRGB(x, y)==-1)?0:-1;
        while(x<grayImg.getWidth()&&x>=0&&y<grayImg.getHeight()&&y>=0)
        {
           try
           {
             xObject=(Integer)cordValues.pop();
             yObject=(Integer)cordValues.pop();
           }
           catch(EmptyStackException e)
           {
               break;
           }
           img.setRGB(xObject,yObject,desiredColor);
           grayImg.setRGB(xObject,yObject,t);
           //System.out.println();
         //  System.out.println("orignal-Pixel="+orignalColor);
        //   System.out.println("color obtained="+grayImg.getRGB(xObject,yObject));
        //  System.out.println("x= "+xObject+" y= "+yObject);
           try
           {
           // System.out.println((xObject-1)+"  "+(yObject-1)+"  "+grayImg.getRGB(xObject-1,yObject-1)+" "+orignalColor);
             if(grayImg.getRGB(xObject-1,yObject-1)==orignalColor)
             {
               cordValues.push(yObject-1);
               cordValues.push(xObject-1);
               
             }
           }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try
           {
          // System.out.println((xObject-1)+"  "+(yObject+1)+"  "+grayImg.getRGB(xObject-1,yObject+1)+" "+orignalColor);
            if(grayImg.getRGB(xObject-1,yObject+1)==orignalColor)
            {
               cordValues.push(yObject+1);
               cordValues.push(xObject-1);
               
            }
           }
           catch(ArrayIndexOutOfBoundsException e){}
           try
           {
         // System.out.println((xObject+1)+"  "+(yObject-1)+"  "+grayImg.getRGB(xObject+1,yObject-1)+" "+orignalColor);
           if(grayImg.getRGB(xObject+1,yObject-1)==orignalColor )
           {
               cordValues.push(yObject-1);
               cordValues.push(xObject+1);
              
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try
           {
         // System.out.println((xObject+1)+"  "+(yObject+1)+"  "+grayImg.getRGB(xObject+1,yObject+1)+" "+orignalColor);
           if(grayImg.getRGB(xObject+1,yObject+1)==orignalColor )
           {
              cordValues.push(yObject+1);
              cordValues.push(xObject+1);
              
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try{
               
        // System.out.println((xObject)+"  "+(yObject-1)+"  "+grayImg.getRGB(xObject,yObject-1)+" "+orignalColor);
           if(grayImg.getRGB(xObject,yObject-1)==orignalColor)
           {
              cordValues.push(yObject-1);
              cordValues.push(xObject);
               
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try
           {
         // System.out.println((xObject-1)+"  "+(yObject)+"  "+grayImg.getRGB(xObject-1,yObject)+" "+orignalColor);
           if(grayImg.getRGB(xObject-1,yObject)==orignalColor)
           {
               cordValues.push(yObject);
               cordValues.push(xObject-1);
              
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try
           {
         // System.out.println((xObject+1)+"  "+(yObject)+"  "+grayImg.getRGB(xObject+1,yObject)+" "+orignalColor);
           if(grayImg.getRGB(xObject+1,yObject)==orignalColor)
           {
              cordValues.push(yObject);
              cordValues.push(xObject+1);
               
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}
           
           try
           {
          //System.out.println((xObject)+"  "+(yObject+1)+"  "+grayImg.getRGB(xObject,yObject+1)+" "+orignalColor);
           if(grayImg.getRGB(xObject,yObject+1)==orignalColor)
           {
              cordValues.push(yObject+1);
              cordValues.push(xObject);
           
           }
            }
           catch(ArrayIndexOutOfBoundsException e){}   
        }
       
    }
    private boolean checkShades(int color,int shade)
    {
       int variation=0;
       Color c=new Color(color);
       Color s=new Color(shade);
       
       int red  =Math.abs(c.getRed()  - s.getRed()  );
       int blue =Math.abs(c.getBlue() - s.getBlue() );
       int green=Math.abs(c.getGreen()- s.getGreen());
       c=null;//changed
       s=null;
       return (red<=variation && green<=variation && blue<=variation);
    }
    
//   
    
}
