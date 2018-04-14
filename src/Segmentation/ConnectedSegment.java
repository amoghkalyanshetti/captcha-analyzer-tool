package Segmentation; 

//import preprocessing.ImageInfo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class ConnectedSegment{

        // Class Variable declaration
        private BufferedImage brArr[];
        int arrIndex;
        private int maxLen,minLen;
        int ww,hh,inPixels[][];
        BufferedImage biIn;

        //Default constructor
        public ConnectedSegment(BufferedImage biIn)
        {
            brArr=new BufferedImage[15];
            arrIndex=0;
            minLen=0;maxLen=0;
            this.biIn=biIn;
            ww=biIn.getWidth();
            hh=biIn.getHeight();
            System.out.println("h== "+hh+"w=="+ww);
            inPixels=new int[hh][ww];
            for(int i=0;i<hh;i++)
            {
           for(int j=0;j<ww;j++)
           {
                inPixels[i][j]=biIn.getRGB(j,i) ;//* 0xffffff;         //using RGB to get the pixel values
           }

         }
           
        }
        
               //1st function....
        //This function uses projection based algorithm to segment
        public BufferedImage[] segmentImage()
        {
           
            BufferedImage tempBr=new BufferedImage(ww,hh,BufferedImage.TYPE_BYTE_BINARY); //creating a temperary br
            Graphics2D grap=tempBr.createGraphics();
            grap.drawImage(biIn,0,0,ww,hh,null);
            for(int i=0;i<hh;i++)
            {
                for(int j=0;j<ww;j++)
                {
                    inPixels[i][j]=tempBr.getRGB(j,i) & 0x00ffffff;             //retreiving RGB values from image in a matrix
                }
            }
            //vertical projection
            int vert_proj[];
            vert_proj=new int[ww];

            for(int j=0;j<ww;j++)
            {
                int cnt=0;
                for(int i=0;i<hh;i++)
                {
                   
                    if(inPixels[i][j]==0)              //checks if the current point is black
                    {
                        cnt++;                                                    //counting the non-white pixels vertically
                    }
                    tempBr.setRGB(j, i,inPixels[i][j]);
                }
                vert_proj[j]=cnt;   // counts total no. of black pixels per vertical line
                 System.out.println(vert_proj[j]);
            }
            int max;
            max=vert_proj[0];
            float total=0;
            for(int i=0;i<ww;i++)
            {
                total=total + vert_proj[i];                                     //total used to calculate total black pixel density in the image
                if(max<vert_proj[i])
                    max=vert_proj[i];                                            //maximum vertical projection value for calculating threshold
            }
            total=total*100/(hh * ww);                        //finding the pixel density in the entire image
         //   System.out.println("% " + total);
             //setting the max and min length for the character
            if(total<15.0)
            {
                minLen=10;
                maxLen=40;
            }
            else if(total>25.0 && total<45.0)
            {
                minLen=30;
                maxLen=60;
            }

            // Projection based algorithm...
            int lastX=0,neww;
            BufferedImage segBr;
            for(int i=0;i<ww;i++)
            {
                if(vert_proj[i]<(0.15* max))                                        //comparing threshold value
                {
                    neww=i-lastX;
                   // System.out.println("New width" + neww + "Old Width" + objImage.ww);
                    if(neww<=1)
                        lastX=i;
                    if((neww)>minLen && (neww)<maxLen)                          //if segment length can be character length
                    {
                        addToBr(neww,hh,1,lastX,biIn,0);          //save the segment
                        lastX=i;
                    }
                    else if(neww>maxLen)                                    // else send to 2nd function for further segmentation
                    {
                        segBr=new BufferedImage(neww,hh,BufferedImage.TYPE_INT_RGB);
                       ImageInfo tempImage=new ImageInfo();              //segBr only contains the current segment
                        //addToBr(neww,objImage.hh,0,lastX,br);          //save the segment
                     
                        for(int x=0;x<neww;x++)
                                for(int y=0;y<hh;y++)
                                    segBr.setRGB(x,y,biIn.getRGB(x+lastX,y));
                        tempImage.getInpixels(segBr);
                        segmentTop(segBr,tempImage);                //calling 2nd function
                        lastX=i;
                    }
                }
            }
            BufferedImage segments[]=new BufferedImage[arrIndex];
            System.arraycopy(brArr, 0, segments, 0, arrIndex);
             return segments;
        }
        
         //2nd function
        //This function checks for segmentation points from top of the image..
        public void segmentTop(BufferedImage br,ImageInfo objImage)
        {

            int startX=0;
           // System.out.println("Top" + objImage.ww);
            //getting the 1st black point
            outer:for(int i=0;i<objImage.ww;i++)
            {
                for(int j=0;j<objImage.hh;j++)
                {
                    if((br.getRGB(i, j)& 0x00ffffff)==0)            //checks if the current point is black
                    {
                        startX=i+1;
                        break outer;
                    }
                }
            }
            if((objImage.ww-startX)<maxLen+15)                    //if only last part of image remaining
            {
                addToBr(((objImage.ww-1-startX)),objImage.hh,1,startX,br,1);          //save the segment
            }
            else                                        //else if a bigger segment
            {
                 //getting the position of topmost black pixels
                int thresMin=maxLen*35/100;
                int thresMax=maxLen*65/100;
                int[] yPos=new int[maxLen+startX-thresMin+1];
                for(int i=startX+thresMin;i<thresMax+startX;i++)
                {
                    for(int j=0;j<objImage.hh;j++)
                    {
                        if((br.getRGB(i, j)& 0x00ffffff)==0)            //checks if the current point is black
                        {
                            yPos[i-(startX+thresMin)]=j;                //saving the position of the black pixel
                            break;
                        }
                    }
                }

                //getting the highest black pixel from all the above stored ones
                int min=objImage.hh;
                int minPos=0;
                for(int i=0;i<yPos.length;i++)
                {
                    if(yPos[i]<=min && yPos[i]!=0)
                    {
                        min=yPos[i];
                        minPos=i+startX+thresMin;
                    }
                }

               // System.out.println(min + " min and min POs   " + minPos);

                if(minPos>=maxLen*40/100 && minPos<=maxLen*60/100)
                {
                    outer1:for(int i1=minPos+10;i1<minPos+thresMin;i1++)
                    {
                        for(int j1=-1;j1<=3;j1++)
                            if((br.getRGB(i1,min+j1) & 0x00ffffff)==0)
                            {
                                minPos=i1;
                                min=min + j1;
                                break outer1;
                            }
                    }
                }

              //  System.out.println(min + "new min and min POs   " + minPos);

                //tracing the outline till the direction of pixel changes
                Point segPoint=new Point();
                loop:for(int j=0;j<objImage.hh;j++)
                {
                    int i=minPos;
                    if((br.getRGB(i,j)& 0x00ffffff)==0)                 //checks if the current point is black
                    {
                        while(j<objImage.hh && i<objImage.ww)
                        {
                            try{
                            if((br.getRGB(i+1,j-1)& 0x00ffffff)==0) // on direction change if going north east
                            {
                                i++;
                                segPoint.x=i;
                                segPoint.y=j;
                                break loop;
                            }
                            else if((br.getRGB(i+1,j)& 0x00ffffff)==0)  // going east
                                i++;
                            else if((br.getRGB(i+1,j+1)& 0x00ffffff)==0) //going south- east
                            {
                                i++;
                                j++;
                            }
                            else if((br.getRGB(i,j+1)& 0x00ffffff)==0)   //going south
                                j++;
                            else                                        //for any other direction
                            {
                                i++;
                                segPoint.x=i;
                                segPoint.y=j;
                                break loop;
                            }
                            }
                            catch(Exception e)
                            {
                                System.out.println("Eror is " + e + i + " " + j);
                                break loop;
                            }
                        }
                    }
                }

              //  System.out.println(segPoint);
                try{
                if((br.getRGB(segPoint.x-1,segPoint.y)& 0x00ffffff)==0)                     //checking for number of black pixels before the segmentation point
                    if((br.getRGB(segPoint.x-2,segPoint.y)& 0x00ffffff)==0)
                        if((br.getRGB(segPoint.x-3,segPoint.y)& 0x00ffffff)==0)
                                  segPoint.x=segPoint.x-2;
                }
                catch(Exception e)
                {
                    System.out.println(segPoint);
                }
                if((segPoint.x-startX)<maxLen && (segPoint.x-startX)>minLen)            //checking  if the segment can be a character
                {
                    addToBr(((segPoint.x-startX)),objImage.hh,1,startX,br,1);          //save the segment
                    BufferedImage tempBr=new BufferedImage(objImage.ww,objImage.hh,BufferedImage.TYPE_BYTE_BINARY); //creating a temperary br
                    Graphics2D grap=tempBr.createGraphics();
                    grap.drawImage(br,0,0,objImage.ww,objImage.hh,null);
                    for(int x=segPoint.x+1;x<objImage.ww;x++)                           //making that part of segment white in the original image
                        for(int y=0;y<objImage.hh;y++)
                            tempBr.setRGB(x-segPoint.x-1, y,br.getRGB(x, y));
                    objImage.ww=objImage.ww-segPoint.x;
                    segmentTop(tempBr, objImage);                               //calling the function with modified image
                }
                else                                    //   if segment length is greater
                {
                    if((segPoint.x-startX)>0)
                    {
                        BufferedImage segBr=new BufferedImage((segPoint.x-startX),objImage.hh,BufferedImage.TYPE_INT_RGB);
                        ImageInfo tempImage=new ImageInfo();
                        //addToBr((segPoint.x-startX),objImage.hh,0,startX,br,0);          //save the segment
                        for(int x=0;x<(segPoint.x-startX);x++)
                             for(int y=0;y<objImage.hh;y++)
                                 segBr.setRGB(x,y,br.getRGB(x+startX,y));      // make a segBr containing this segment
                        tempImage.getInpixels(segBr);
                        int tempSeg=segmentBottom(segBr,tempImage);             // call 3rd function to segment this segment

                       // System.out.println(tempSeg + "temp");
                        if(tempSeg<objImage.ww-5)
                        {
                            BufferedImage tempBr=new BufferedImage(objImage.ww,objImage.hh,BufferedImage.TYPE_BYTE_BINARY); //creating a temperary br
                            Graphics2D grap=tempBr.createGraphics();
                            grap.drawImage(br,0,0,objImage.ww,objImage.hh,null);
                            for(int x=tempSeg;x<objImage.ww;x++)                           //making that part of segment white in the original image
                                for(int y=0;y<objImage.hh;y++)
                                    tempBr.setRGB(x-tempSeg, y,br.getRGB(x, y));
                            objImage.ww=objImage.ww-tempSeg;
                            segmentTop(tempBr, objImage);
                        }
                    }
                }

            }
        }
        
        
        //3rd function
        //This function checks for segmentation points from the bottom of the image
        public int segmentBottom(BufferedImage br,ImageInfo objImage)
        {
            int startX=0;
           // System.out.println("Bottom");
            //getting the 1st point
            outer:for(int i=0;i<objImage.ww;i++)
            {
                for(int j=0;j<objImage.hh;j++)
                {
                    if((br.getRGB(i, j)& 0x00ffffff)==0)                //checks if the current point is black
                    {
                        startX=i+1;
                        break outer;
                    }
                }
            }
      //      System.out.println(startX + " startx");
            
    //        System.out.println("Reamining length" + (objImage.ww-startX) + "    " + startX);
            if((objImage.ww-startX)>maxLen + 15)             //checking to see if length of the segment is enough
            {
            //getting the position of bottommost black pixels
                int thresMin=maxLen*35/100;
                int thresMax=maxLen*65/100;
                int[] yPos=new int[maxLen+startX-thresMin+1];
                for(int i=startX+thresMin;i<thresMax+startX;i++)
                {
                    for(int j=objImage.hh-1;j>0;j--)
                    {
                       if((br.getRGB(i, j)& 0x00ffffff)==0)             //checks if the current point is black
                        {
                            yPos[i-(startX+thresMin)]=j;                //storing the position of the pixel
                            break;
                        }
                    }
                }

                

                //getting the lowermost black pixel
                int max=0;
                int maxPos=0;
                for(int i=0;i<yPos.length;i++)
                {
                    if(yPos[i]>=max && yPos[i]!=objImage.hh-1)
                    {
                        max=yPos[i];
                        maxPos=i+startX+thresMin;
                    }
                }

                //tracing the outline till the direction of pixel changes
                Point segPoint=new Point();
                loop:for(int j=0;j<objImage.hh;j++)
                {
                    int i=maxPos;
                    if((br.getRGB(i,j)& 0x00ffffff)==0)                 //checks if the current point is black
                    {
                        while(j>0 && i<objImage.ww)
                        {
                            try{
                            if((br.getRGB(i+1,j+1)& 0x00ffffff)==0) // on direction change ir going north east
                            {
                                i++;
                                segPoint.x=i;
                                segPoint.y=j;
                                break loop;
                            }
                            else if((br.getRGB(i+1,j)& 0x00ffffff)==0)  // going east
                                i++;
                            else if((br.getRGB(i+1,j-1)& 0x00ffffff)==0) //going south- east
                            {
                                i++;
                                j--;
                            }
                            else if((br.getRGB(i,j-1)& 0x00ffffff)==0)   //going south
                                j--;
                            else                                        //for any other direction
                            {
                                i++;
                                segPoint.x=i;
                                segPoint.y=j;
                                break loop;
                            }
                            }
                            catch(Exception e)
                            {
                                System.out.println("Error is " + e + i + " " + j);
                            }
                        }
                    }
                }

                try{
                if((br.getRGB(segPoint.x-1,segPoint.y)& 0x00ffffff)==0)                     //checking for number of black pixels before the segmentation point
                    if((br.getRGB(segPoint.x-2,segPoint.y)& 0x00ffffff)==0)
                        if((br.getRGB(segPoint.x-3,segPoint.y)& 0x00ffffff)==0)
                              segPoint.x=segPoint.x-2;
                }
                catch(Exception e)
                {
                    System.out.println("Exception occured " + e);
                }
      //          System.out.println(segPoint.x-startX + " segpoint" + segPoint.x);
                if((segPoint.x-startX)<maxLen && (segPoint.x-startX)>minLen)            //checking if segment can be a character
                {
                    addToBr(((segPoint.x-startX)),objImage.hh,1,startX,br,1);          //save the segment
                    BufferedImage tempBr=new BufferedImage(objImage.ww,objImage.hh,BufferedImage.TYPE_BYTE_BINARY); //creating a temperary br
                    Graphics2D grap=tempBr.createGraphics();
                    grap.drawImage(br,0,0,objImage.ww,objImage.hh,null);
                    for(int x=segPoint.x;x<objImage.ww;x++)                           //making that part of segment white in the original image
                        for(int y=0;y<objImage.hh;y++)
                            tempBr.setRGB(x-segPoint.x, y,br.getRGB(x, y));
                    objImage.ww=objImage.ww-segPoint.x;
                    segmentTop(tempBr, objImage);
                    return(segPoint.x);
                }
                else
                {
                    System.out.println("Half");
                    BufferedImage tempBr=new BufferedImage(objImage.ww,objImage.hh,BufferedImage.TYPE_BYTE_BINARY); //creating a temperary br
                    Graphics2D grap=tempBr.createGraphics();
                    grap.drawImage(null,0,0,objImage.ww,objImage.hh,null);
                    for(int x=0;x<objImage.ww/2;x++)                           //making that part of segment white in the original image
                        for(int y=0;y<objImage.hh;y++)
                            tempBr.setRGB(x, y,br.getRGB(x, y));
                    int tempWW=objImage.ww/2;
                    addToBr(tempWW,objImage.hh,1,0,tempBr,1);          //save the segment
                    for(int x=tempWW;x<objImage.ww;x++)                           //making that part of segment white in the original image
                        for(int y=0;y<objImage.hh;y++)
                            tempBr.setRGB(x-tempWW, y,br.getRGB(x, y));
                    addToBr(tempWW,objImage.hh,1,tempWW,tempBr,1);          //save the segment
                    return objImage.ww;
                }
            }
            else
            {
              //  System.out.println("saving");
                addToBr(((objImage.ww-1-startX)),objImage.hh,1,startX,br,1);          //save the segment
                return(objImage.ww-1-startX);
            }
            //return(0);
        }

        //This function returns the saved segments
        public BufferedImage returnBr(int index)
        {
            if(index<this.arrIndex)         //compare the segment number to be retunred with actual number of segments stored
            {
               // System.out.println(arrIndex + " " + index);
                return(brArr[index]);
            }
            else                //else return a 50x50 white pixel image
            {
                BufferedImage tempBr=new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
                for(int i=0;i<50;i++)
                    for(int j=0;j<50;j++)
                        tempBr.setRGB(i, j,0x00ffffff);
                return(tempBr);
            }
        }

        
        public void addToBr(int ww,int hh,int flag,int lastX,BufferedImage br, int algoFlag)
        {
            int tempAdd=0;
      //      Preprocess objPr=new Preprocess();
            if(flag==1)                 //checking whether to add white pixels before and after the segment
                tempAdd=00;  //changed from 20
            else
                tempAdd=0;
            BufferedImage tempBr=new BufferedImage(ww+(tempAdd*2),hh,BufferedImage.TYPE_INT_RGB);        //allocating memory space to br

            //saving the segment
            for(int x=0;x<tempAdd;x++)
                for(int y=0;y<hh;y++)
                   tempBr.setRGB(x,y,0x00ffffff);
            for(int x=tempAdd;x<(tempAdd+(ww));x++)
                for(int y=0;y<hh;y++)
                   tempBr.setRGB(x,y,br.getRGB(x+lastX-tempAdd, y));
            if(flag==1)
            {
                for(int x=(tempAdd+(ww));x<((tempAdd*2)+(ww));x++)
                    for(int y=0;y<hh;y++)
                        tempBr.setRGB(x,y,0x00ffffff);
            }
//            if(algoFlag==1)
//                brArr[arrIndex]=objPr.cleanSegment(tempAdd+ww, hh, tempBr);
//            else
           
                brArr[arrIndex]=tempBr;
            arrIndex++;                 //increamenting the total number of segments
            System.out.println(arrIndex+"  Segment...");
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
}