package preprocessing;
import CAT.CommonUtilityClass;
import CAT.Main;
import java.awt.image.BufferedImage;
import java.awt.Color;
public class NoiseRemoval 
{     
   
    public BufferedImage unknownNoiseRemoval (BufferedImage binaryImage,int x,int y)
    {
       BufferedImage temp=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY); 
       int recordX[]=new int[binaryImage.getWidth()];
       int recordY[]=new int[binaryImage.getHeight()];
       for(int i=0;i<binaryImage.getHeight();i++)
       {
         for(int j=0;j<binaryImage.getWidth();j++)
         {
           temp.setRGB(j,i, binaryImage.getRGB(j, i));
           if((new Color(binaryImage.getRGB(j, i)).getGreen()) != 255)
           {
             recordX[j]+=1;
             recordY[i]+=1 ;
           }
         }
       }
      for(int i=0;i<binaryImage.getHeight();i++)
      {
        if(recordY[i]<=y) 
        {
          for(int j=0;j<binaryImage.getWidth();j++)
          {
             temp.setRGB(j,i,0xFFFFFF);
          }
        }
      }
      for(int i=0;i<binaryImage.getWidth();i++)
      {
        if(recordX[i]<=x) 
        {
          for(int j=0;j<binaryImage.getHeight();j++)
          {
             temp.setRGB(i,j,0xFFFFFF);
          }
        }
      }
      Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return  temp;
    }
    public BufferedImage arcRemovedObj(BufferedImage binaryImage)
    {
       BufferedImage arcRemoved=new BufferedImage(binaryImage.getWidth(),binaryImage.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImage.getHeight();i++)
        {
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                if(255==new Color(binaryImage.getRGB(j,i)).getGreen())
                  arcRemoved.setRGB(j,i,0xFFFFFF);
                else 
                 arcRemoved.setRGB(j,i,0);
            }
  
        } 
        //applying the windowing
        for(int j=0;j<arcRemoved.getHeight()-10;j++)
        {
            for(int i=0;i<arcRemoved.getWidth()-10;i++)
            {
                int flag=0;
                //for topmost edge of the window

                int y=j;
                for(int x=i;x<i+10;x++)
                {
                    if(arcRemoved.getRGB(x,y)==-16777216)
                        flag=1;                             //window overlap with the character
                }

                //for bottommost edge of the window
                y=j+9;
                for(int x=i;x<i+10;x++)
                {
                    if(arcRemoved.getRGB(x,y)==-16777216)
                        flag=1;                             //window overlap with the character
                }

                 //for leftmost edge of the window
                int x=i;
                for(y=j;y<j+10;y++)
                {
                    if(arcRemoved.getRGB(x,y)==-16777216)
                        flag=1;                            //window overlap with the character
                }

                //for rightmost edge of the window
                x=i+9;
                for(y=j;y<j+10;y++)
                {
                    if(arcRemoved.getRGB(x,y)==-16777216)
                        flag=1;                            //window overlap with the character
                }

                if(flag==0)                //window may contain noise in the form of arcs
                {
                    for(y=j+1;y<j+10;y++)
                        for(x=i+1;x<i+10;x++)
                        {
                            arcRemoved.setRGB(x, y, -1);
                            //binarized.inpixels[x][y]=binarized.backColor;         //removal of noise
                            //tempBr.setRGB(y, x, binarized.getRGB(x, y));
                        }
                }
            }
        }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        return arcRemoved;
    } 
    public BufferedImage getVerLineRmvObj2px(BufferedImage binaryImage)
    {
       BufferedImage vLineFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY); 
       for(int i=0;i<binaryImage.getWidth();i++)
       {    
           for(int j=0;j<binaryImage.getHeight();j++)
           {
               boolean pixel_curr,pixel_right1,pixel_left1,pixel_left2,pixel_right2;
               pixel_curr=binaryImage.getRGB(i,j) != 0xFFFFFF  ;
               if(pixel_curr)
               {
                    try
                    { 
                       pixel_left1   = binaryImage.getRGB(i-1,j) != 0xFFFFFF ;
                       pixel_left2   = binaryImage.getRGB(i-2,j) != 0xFFFFFF ;
                       pixel_right1  = binaryImage.getRGB(i+1,j) != 0xFFFFFF ;
      
                       pixel_right2  = binaryImage.getRGB(i+2,j) != 0xFFFFFF ;
                 
                       if(
                           ( pixel_left1 && !pixel_left2 && !pixel_right1 && !pixel_right2) 
                                               ||
                           ( !pixel_left1 && !pixel_left2 && pixel_right1 && !pixel_right2)
                                               ||
                           ( !pixel_left1  && !pixel_left2 && !pixel_right2 && !pixel_right1)                         
                         )
                        {
                            vLineFree.setRGB(j,i,0xFFFFFF); 
                        }
                       else
                        {
                            vLineFree.setRGB(j,i,0); 
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {     
                       //vLineFree.setRGB(j,i, 0xFFFFFF);  
                    }
                }
               else
               {
                    vLineFree.setRGB(j,i,0xFFFFFF);    
               }
            }
        } 
       Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
       return vLineFree;
    }
    public BufferedImage getHorLineRmvObj2px(BufferedImage binaryImage)
    {
        BufferedImage hLineFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImage.getHeight();i++)
        {    
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                boolean pixel_curr,pixel_up1,pixel_up2,pixel_down1,pixel_down2,pixel_right,pixel_left;
                pixel_curr=(binaryImage.getRGB(j,i) != 0xFFFFFF) ;
                if(pixel_curr)
                {
                    try
                    {
                       pixel_up1   =binaryImage.getRGB(j,i-1) != 0xFFFFFF ;
                       pixel_up2   =binaryImage.getRGB(j,i-2) != 0xFFFFFF ;
                       pixel_down1 =binaryImage.getRGB(j,i+1) != 0xFFFFFF ;
                       pixel_down2 =binaryImage.getRGB(j,i+2) != 0xFFFFFF;
                       if(
                            ( pixel_up1 && !pixel_down1 && !pixel_down2 && !pixel_up2)
                                                 ||
                            ( !pixel_up1 && pixel_down1 && !pixel_down2 && !pixel_up2)
                                                 ||
                            ( !pixel_up1  && !pixel_down1 && !pixel_down2 && !pixel_up2)                
                          )
                        {
                            hLineFree.setRGB(j,i, 0xFFFFFF); 
                        }
                        else
                        {
                            hLineFree.setRGB(j,i, 0);
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                         hLineFree.setRGB(j,i, 0xFFFFFF); 
                    }
                }
                else
                {
                    hLineFree.setRGB(j,i, 0xFFFFFF);  
                }
            }
        } 
        Main.tracker.add((String)new Object(){}.getClass().getEnclosingMethod().getName());
        return binaryImage;
    }
   
public BufferedImage boundaryNoiseRmvImg(BufferedImage binaryImage,int boundaryPx)
    {
       BufferedImage edgeFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
       int width= binaryImage.getWidth();
       int height=binaryImage.getHeight();
       for(int i=0;i<binaryImage.getHeight();i++)
           for(int j=0;j<binaryImage.getWidth();j++)
           {
                edgeFree.setRGB(j,i,binaryImage.getRGB(j,i));
           }
       for(int t=0;t<boundaryPx;t++)
       {
         //  System.out.println();
        //  System.out.println("Height");
          for(int i=0+t;i<height-t;i++)
          {
            edgeFree.setRGB(t,i, 0xFFFFFF);
         //   System.out.print("(0,"+i+")");
            edgeFree.setRGB(width-1-t,i,0xFFFFFF);
            // System.out.print("("+ (width-1) +","+i+")");
          }
         // System.out.println();
         // System.out.println("Width");
          for(int i=0+t;i<width-t;i++)
          {
            edgeFree.setRGB(i,t, 0xFFFFFF);
          //  System.out.print("(0,"+i+")");
            edgeFree.setRGB(i,height-1-t,0xFFFFFF);
           // System.out.print("("+ (height-1) +","+i+")");
          }
       }
       return  edgeFree;
    }
 public BufferedImage arcRemovalMSRCTC(BufferedImage grayscaledImg,int colour)
    {
        BufferedImage arcFree=new BufferedImage(grayscaledImg.getWidth(),grayscaledImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<grayscaledImg.getWidth(); i++) 
        {
            for(int j=0; j<grayscaledImg.getHeight(); j++) 
            {
                int newPixel=grayscaledImg.getRGB(i, j);
                if(newPixel!=colour)
                   arcFree.setRGB(i, j, newPixel);
                else
                {
                   int t=j,k=j;
                   try
                   {
                   while(grayscaledImg.getRGB(i,--t)==colour);
                   }
                   catch(Exception e){t++;}
                   try
                   {
                   while(grayscaledImg.getRGB(i,++k)==colour);
                   }
                   catch(Exception e){k--;} 
                  int pixel=grayscaledImg.getRGB(i,t);
                  
                 
                   for(int p=t;p<k;p++)
                        arcFree.setRGB(i,p,pixel);     
                  
                }
            }      
         }        
     return arcFree;
    }
    public BufferedImage getEdgeRmvImg(BufferedImage binaryImage)
    {
       BufferedImage edgeFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
       int width= binaryImage.getWidth();
       int height=binaryImage.getHeight();
       for(int i=0;i<binaryImage.getHeight();i++) {
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                 edgeFree.setRGB(j,i,binaryImage.getRGB(j,i));
            }
        }
  
       for(int i=0;i<height;i++)
       {
             edgeFree.setRGB(0,i, 0xFFFFFF);
             edgeFree.setRGB(width-1,i,0xFFFFFF);
       }
       for(int i=0;i<width;i++)
       {
            edgeFree.setRGB(i,0, 0xFFFFFF);
            edgeFree.setRGB(i,height-1,0xFFFFFF);
       }
       Main.tracker.add((String)new Object(){}.getClass().getEnclosingMethod().getName());
       
      
       return  edgeFree;
    }
    public  BufferedImage getHorLineRmvObj(BufferedImage binaryImage)
    {
        BufferedImage hLineFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImage.getHeight();i++)
        {
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                if(255==new Color(binaryImage.getRGB(j,i)).getGreen())
                  hLineFree.setRGB(j,i,0xFFFFFF);
                else 
                  hLineFree.setRGB(j,i,0);
            }
  
        } 
        for(int i=0;i<binaryImage.getHeight();i++)
        {    
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                boolean pixel_curr,pixel_up,pixel_down,pixel_right,pixel_left;
                pixel_curr=(hLineFree.getRGB(j,i) != 0xFFFFFF) ;
                if(pixel_curr)
                {
                    try
                    {
                        pixel_up   = hLineFree.getRGB(j,i-1) != 0xFFFFFF ;
                        pixel_down = hLineFree.getRGB(j,i+1) != 0xFFFFFF ;
                        pixel_right= hLineFree.getRGB(j+1,i) != 0xFFFFFF ;
                        pixel_left = hLineFree.getRGB(j-1,i) != 0xFFFFFF ;
                        if(
                            ( pixel_up==false  &&  pixel_down==false) 
                                            &&
                            ( pixel_right==true  ||pixel_left==true )
                          )
                        {
                            hLineFree.setRGB(j,i, 0xFFFFFF); 
                        }
                        else
                        {
                            hLineFree.setRGB(j,i,0); 
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        hLineFree.setRGB(j,i, 0xFFFFFF); 
                    }
                }
                else
                {
                    hLineFree.setRGB(j,i, 0xFFFFFF);     
                }
            }
        } 
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        return binaryImage;
    }
    public BufferedImage  getVerLineRmvObj(BufferedImage binaryImage)
    {
        BufferedImage vLineFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImage.getHeight();i++)
        {
            for(int j=0;j<binaryImage.getWidth();j++)
            {
                if(255==new Color(binaryImage.getRGB(j,i)).getGreen())
                  vLineFree.setRGB(j,i,0xFFFFFF);
                else 
                  vLineFree.setRGB(j,i,0);
            }
  
        } 
        
        for(int i=0;i<binaryImage.getWidth();i++)
        {    
            for(int j=0;j<binaryImage.getHeight();j++)
            { 
                boolean pixel_curr,pixel_up,pixel_down,pixel_right,pixel_left;
                pixel_curr=(vLineFree.getRGB(i,j) != 0xFFFFFF) ;
                if(pixel_curr)
                {
                    try
                    {
                        pixel_up   = new Color(vLineFree.getRGB(i,j-1)).getGreen() != 255 ;
                        pixel_down = new Color(vLineFree.getRGB(i,j+1)).getGreen() != 255 ;
                        pixel_right= new Color(vLineFree.getRGB(i+1,j)).getGreen() != 255 ;
                        pixel_left = new Color(vLineFree.getRGB(i-1,j)).getGreen() != 255 ;
                        if(
                            ( pixel_right==false  &&  pixel_left==false ) 
                                        &&
                            (pixel_up==true  ||  pixel_down==true )
                          )
                        {
                           vLineFree.setRGB(i,j, 0xFFFFFF); 
                        }  
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        vLineFree.setRGB(i,j, 0xFFFFFF); 
                    }
                }
            } 
        }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        return vLineFree;
    }
    public BufferedImage getDotRmvObj(BufferedImage binaryImage)
    {
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
       return getDotRmvImg(binaryImage,2039);
    }
    public BufferedImage getAdvDotRmvObj(BufferedImage binaryImage,int t)
    {
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
       return getDotRmvImg(binaryImage,t);
    }
    BufferedImage getDotRmvImg(BufferedImage binaryImage,int t)
    {
        BufferedImage dotFree=new BufferedImage(binaryImage.getWidth(), binaryImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int j=0;j<binaryImage.getHeight();j++)
        {
            for(int i=0;i<binaryImage.getWidth();i++)
            { 
                int pixel=(int)(new Color(binaryImage.getRGB(i,j))).getRed();  
                if(pixel==0)
                {
                    int total=0,val[][]=new int[3][3] ; 
                    int row=i-1,col=j-1;
                    try
                    {
         
                        for(int m=0 ;m<3;m++)
                            for(int n=0;n<3;n++)
                                total+=val[m][n]=new Color(binaryImage.getRGB(row+m,col+n)).getGreen();
                        if(total>t)
                        {
                            dotFree.setRGB(i,j, 0xFFFFFF);     
                        }
                        else
                        {
                            dotFree.setRGB(i,j, 0);     
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        dotFree.setRGB(i,j, 0xFFFFFF);
                    }
                }
                else
                {
                    dotFree.setRGB(i,j,0xFFFFFF);
                }
            }
        }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        return dotFree;
    }
    public BufferedImage getRevGrayscaledObj(BufferedImage orignalImage)
    {
        BufferedImage inverseGrayscaledImage=new BufferedImage(orignalImage.getWidth(), orignalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<orignalImage.getWidth(); i++) 
        {
            for(int j=0; j<orignalImage.getHeight(); j++) 
            {
                int  alpha    =new Color(orignalImage.getRGB(i,j)).getAlpha();
                int  red      =new Color(orignalImage.getRGB(i,j)).getRed();  
                int  green    =new Color(orignalImage.getRGB(i,j)).getGreen();
                int  blue     =new Color(orignalImage.getRGB(i,j)).getBlue();  
                int  gray = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                int inverse_pixel=(new CommonUtilityClass()).colorToRGB(254-alpha,254-gray,254-gray,254-gray);
                //System.out.println(inverse_pixel);
                inverseGrayscaledImage.setRGB(i,j,inverse_pixel);
            }
        }	
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
        return inverseGrayscaledImage;
    }
    
    /*Charachter  Repairing algorithms*/
    public BufferedImage getCharRepairedObj(BufferedImage binaryImg)
    {
       BufferedImage repImage=new BufferedImage(binaryImg.getWidth(), binaryImg.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImg.getHeight();i++)
        {     
            for(int j=0;j<binaryImg.getWidth();j++)
            {
                repImage.setRGB(j,i,binaryImg.getRGB(j,i));
            }
        } 
       
       for(int i=0;i<binaryImg.getHeight();i++)
       {
         for(int j=1;j<binaryImg.getWidth()-1;j++)
         {
            int pixel=(int)(new Color(repImage.getRGB(j,i))).getRed();
            int left=(int)(new Color(repImage.getRGB(j-1,i))).getRed();
            int right=(int)(new Color(repImage.getRGB(j+1,i))).getRed();
            if(left==right && left==0 && pixel==255)
              repImage.setRGB(j,i,0);
         }
             
       }
       
        for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=1;j<binaryImg.getHeight()-1;j++)
         {
            int pixel =(int)(new Color(repImage.getRGB(i,j))).getRed();
            int up    =(int)(new Color(repImage.getRGB(i,j-1))).getRed();
            int down  =(int)(new Color(repImage.getRGB(i,j+1))).getRed();
            if(up==down && up==0 && pixel==255)
               repImage.setRGB(i,j,0);
         }
             
       }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return  repImage;   
    }
    public void getCharRepairedH2pxObj(BufferedImage binaryImg)
    {
        for(int i=0;i<binaryImg.getHeight();i++)
        {
            for(int j=1;j<binaryImg.getWidth()-2;j++)
            {
               int pixel = (int)(new Color(binaryImg.getRGB(j,i))).getRed();
               int left1 = (int)(new Color(binaryImg.getRGB(j-1,i))).getRed();
               int right1= (int)(new Color(binaryImg.getRGB(j+1,i))).getRed();
               int right2= (int)(new Color(binaryImg.getRGB(j+2,i))).getRed();
               if(left1==right2 && left1==0 && right1==pixel && pixel==255)
               {
                    binaryImg.setRGB(j,i,0);
                    binaryImg.setRGB(j+1,i,0);
               }
         }
             
       }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    public void getCharRepairedW2pxObj(BufferedImage binaryImg)
    { 
        for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=1;j<binaryImg.getHeight()-2;j++)
         {
            int pixel  =(int)(new Color(binaryImg.getRGB(i,j))).getRed();
            int up1    =(int)(new Color(binaryImg.getRGB(i,j-1))).getRed();
            int down1  =(int)(new Color(binaryImg.getRGB(i,j+1))).getRed();
            int down2  =(int)(new Color(binaryImg.getRGB(i,j+1))).getRed();
            if(up1==down2 && up1==0)
            {
              binaryImg.setRGB(i,j,0);
              binaryImg.setRGB(i,j+1,0);
            }
         }
             
       }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
    }
    public BufferedImage  getThickWCharObj(BufferedImage binaryImg)
    {
       BufferedImage img=new BufferedImage(binaryImg.getWidth(),binaryImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
       for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=0;j<binaryImg.getHeight();j++)
         {
            int pixel =(int)(new Color(binaryImg.getRGB(i,j))).getRed();
            if(pixel==0)
            {
               img.setRGB(i,j,0);
               try
               {
               img.setRGB(i-1,j,0);
               img.setRGB(i+1,j,0);
               }
               catch(Exception e)
               {}
            }
            else
              img.setRGB(i,j,0xFFFFFF);
         }
             
       }
       Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
       return img;
    }
     public BufferedImage getThickHCharObj(BufferedImage binaryImg)
    {
       BufferedImage img=new BufferedImage(binaryImg.getWidth(),binaryImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
       for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=0;j<binaryImg.getHeight();j++)
         {
            int pixel =(int)(new Color(binaryImg.getRGB(i,j))).getRed();
            if(pixel==0)
            {
               img.setRGB(i,j,0);
               try
               {
               img.setRGB(i,j-1,0);
               img.setRGB(i,j+1,0);
               }
               catch(Exception e)
               {}
            }
            else
              img.setRGB(i,j,0xFFFFFF);
         }
             
       }
       Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
       return img;
    }
    public BufferedImage  getSlant120ThickCharObj(BufferedImage binaryImg)
    {
      BufferedImage img=new BufferedImage(binaryImg.getWidth(),binaryImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
       for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=0;j<binaryImg.getHeight();j++)
         {
            int pixel =(int)(new Color(binaryImg.getRGB(i,j))).getRed();
            if(pixel==0)
            {
               img.setRGB(i,j,0);
               try
               {
               img.setRGB(i-1,j-1,0);
               img.setRGB(i+1,j+1,0);
               }
               catch(Exception e)
               {}
            }
            else
              img.setRGB(i,j,0xFFFFFF);
         }
             
       }
      Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return img;
    } 
    public BufferedImage  getSlant60ThickCharObj(BufferedImage binaryImg)
    {
      BufferedImage img=new BufferedImage(binaryImg.getWidth(),binaryImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
       for(int i=0;i<binaryImg.getWidth();i++)
       {
         for(int j=0;j<binaryImg.getHeight();j++)
         {
            int pixel =(int)(new Color(binaryImg.getRGB(i,j))).getRed();
            if(pixel==0)
            {
               img.setRGB(i,j,0);
               try
               {
               img.setRGB(i-1,j+1,0);
               img.setRGB(i+1,j-1,0);
               }
               catch(Exception e)
               {}
            }
            else
              img.setRGB(i,j,0xFFFFFF);
         }
             
       }
      Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return img;
    } 
    public BufferedImage  getCharRepaired2px120Obj(BufferedImage binaryImg)
    {
     for(int i=0;i<binaryImg.getHeight();i++)
        {
            for(int j=1;j<binaryImg.getWidth()-2;j++)
            {
               int pixel     = (int)(new Color(binaryImg.getRGB(j,i))).getRed();
               int leftUp    = (int)(new Color(binaryImg.getRGB(j-1,i-1))).getRed();
               int rightDown1= (int)(new Color(binaryImg.getRGB(j+1,i+1))).getRed();
               int rightDown2= (int)(new Color(binaryImg.getRGB(j+2,i+2))).getRed();
               if(leftUp== rightDown2 && leftUp==0 && rightDown1==pixel && pixel==255)
               {
                    binaryImg.setRGB(j,i,0);
                    binaryImg.setRGB(j+1,i,0);
               }
         }
             
       }
     return binaryImg;
    }
     public BufferedImage getCharRepaired2px60Obj(BufferedImage binaryImg)
    {
     for(int i=0;i<binaryImg.getHeight();i++)
        {
            for(int j=1;j<binaryImg.getWidth()-2;j++)
            {
               int pixel     = (int)(new Color(binaryImg.getRGB(j,i))).getRed();
               int rightUp    = (int)(new Color(binaryImg.getRGB(j+1,i-1))).getRed();
               int leftDown1= (int)(new Color(binaryImg.getRGB(j-1,i+1))).getRed();
               int leftDown2= (int)(new Color(binaryImg.getRGB(j-2,i+2))).getRed();
               if(rightUp==leftDown2 && rightUp==0 && leftDown1==pixel && pixel==255)
               {
                    binaryImg.setRGB(j,i,0);
                    binaryImg.setRGB(j+1,i,0);
               }
         }
             
       }
     Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
     return binaryImg;
    }
    public BufferedImage getSlantCharRepairedObj(BufferedImage binaryImg)
     {
      BufferedImage repImage=new BufferedImage(binaryImg.getWidth(), binaryImg.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for(int i=0;i<binaryImg.getHeight();i++)
        {     
            for(int j=0;j<binaryImg.getWidth();j++)
            {
                repImage.setRGB(j,i,binaryImg.getRGB(j,i));
            }
        } 
       
       for(int i=1;i<binaryImg.getHeight()-1;i++)
       {
         for(int j=1;j<binaryImg.getWidth()-1;j++)
         {
            int pixel=(int)(new Color(repImage.getRGB(j,i))).getRed();
            int leftUp   =(int)(new Color(repImage.getRGB(j-1,i-1))).getRed();
            int rightDown=(int)(new Color(repImage.getRGB(j+1,i+1))).getRed();
            if(leftUp==rightDown && rightDown==0 && pixel==255)
               repImage.setRGB(j,i,0);
         }
             
       }
       
        for(int i=1;i<binaryImg.getWidth()-1;i++)
       {
         for(int j=1;j<binaryImg.getHeight()-1;j++)
         {
            int pixel =(int)(new Color(repImage.getRGB(i,j))).getRed();
            int rightUp    =(int)(new Color(repImage.getRGB(i+1,j-1))).getRed();
            int leftDown   =(int)(new Color(repImage.getRGB(i-1,j+1))).getRed();
            if(rightUp==leftDown && leftDown==0 && pixel==255)
               repImage.setRGB(i,j,0);
         }
             
       }
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return  repImage; 
     }
    
    //public BufferedImage get 
    //special binarization for website challanstatus
    //-7895161
    //-3552823
    //-197380
    public BufferedImage getBinarizedImage(BufferedImage grayImg)
    {
      int partialPoint=-1;
      
      BufferedImage partialImg1=new BufferedImage(grayImg.getWidth(),grayImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
      BufferedImage partialImg2=new BufferedImage(grayImg.getWidth(),grayImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
      partialImg1=(new Binarization()).convertGrayToBinary(grayImg,125);
      partialImg2=(new Binarization()).convertGrayToBinary(getRevGrayscaledObj(grayImg),125);
      int initialColor=new Color(partialImg1.getRGB(0, 0)).getRed();
      for(int i=1;i<grayImg.getWidth();i++)
      {
         int nextColor=new Color(partialImg1.getRGB(i, 0)).getRed();
         if(nextColor!=initialColor)
         {
             partialPoint=i;
             break;
         }
      }
      if(new Color(partialImg1.getRGB(0, 0)).getRed()==255)
      {
         
         for(int i=partialPoint;i<grayImg.getWidth();i++)
          {
             for(int j=0;j<grayImg.getHeight();j++)
             {
                  partialImg1.setRGB(i, j,partialImg2.getRGB(i, j));
             }
          }
      }
      else
      {
          for(int i=partialPoint;i<grayImg.getWidth();i++)
          {
             for(int j=0;j<grayImg.getHeight();j++)
             {
                  partialImg2.setRGB(i, j,partialImg1.getRGB(i, j));
             }
          }
          partialImg1=partialImg2;
      }
      Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
      return partialImg1;
    }
    //special arc removal for MSRCTC
    public BufferedImage arcRemovalMSRCTC(BufferedImage grayscaledImg)
    {
        BufferedImage arcFree=new BufferedImage(grayscaledImg.getWidth(),grayscaledImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<grayscaledImg.getWidth(); i++) 
        {
            for(int j=0; j<grayscaledImg.getHeight(); j++) 
            {
                int newPixel=grayscaledImg.getRGB(i, j);
                if(newPixel!=-7895161)
                   arcFree.setRGB(i, j, newPixel);
                else
                {
                   int t=j,k=j;
                   while(grayscaledImg.getRGB(i,--t)==-7895161);
                   while(grayscaledImg.getRGB(i,++k)==-7895161);
                   int pixel=grayscaledImg.getRGB(i,t);
                  
                   for(int p=t;p<k;p++)
                        arcFree.setRGB(i,p,pixel);        
                }
            }      
         }  
        Main.tracker.add(new Object(){}.getClass().getEnclosingMethod().getName());
     return arcFree;
    }
    
}
