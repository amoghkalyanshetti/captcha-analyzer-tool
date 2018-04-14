package preprocessing;
import CAT.CommonUtilityClass;
import CAT.ImageInfo;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

public class Binarization  //class contains all function for complete binarization
{
    int threshold;    
    public BufferedImage getBinaryImage(BufferedImage grayScaledImg)                //gets binary image using dynamic threshold
    {
        
         return convertGrayToBinary(grayScaledImg,this.threshold=calcThreshold(grayScaledImg));
    }
     public BufferedImage getBinaryImage(BufferedImage grayScaledImg,int threshold) //gets binary Image using static threshold
    {
        return  convertGrayToBinary(grayScaledImg,this.threshold=threshold);   
    }
    
    public BufferedImage convertOrgToGray(BufferedImage orignalImage)    //converts orignal image to grayscaled image
    {       
         System.out.println("temp's width:"+orignalImage.getWidth()+"\ntemp's height:"+orignalImage.getHeight());
        BufferedImage grayScaledImage=new BufferedImage(orignalImage.getWidth(), orignalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<orignalImage.getWidth(); i++) 
        {
            for(int j=0; j<orignalImage.getHeight(); j++) 
            {
                int  alpha    =new Color(orignalImage.getRGB(i,j)).getAlpha();
                int  red      =new Color(orignalImage.getRGB(i,j)).getRed();  
                int  green    =new Color(orignalImage.getRGB(i,j)).getGreen();
                int  blue     =new Color(orignalImage.getRGB(i,j)).getBlue();  
                //System.out.println(alpha+" "+red+" "+green+" "+blue);
                //ystem.out.println(orignalImage.getRGB(i,j));
                int  gray     = (int)(0.21 * red + 0.71 * green + 0.07 * blue);
                int newPixel  =(new CommonUtilityClass()).colorToRGB(alpha, gray,gray,gray);
                
                grayScaledImage.setRGB(i, j, newPixel);

            }      
         }
        return grayScaledImage;
    }
        
    
    BufferedImage convertGrayToBinary(BufferedImage grayScaledImg,int threshold) //converts grayscaled image to binary image
    {  
        System.out.println("Threshold="+threshold);
        BufferedImage binarizedImg =new BufferedImage(grayScaledImg.getWidth(),grayScaledImg.getHeight(),BufferedImage.TYPE_BYTE_BINARY); 
        for(int i=0;i<grayScaledImg.getWidth();i++)
        {
            for(int j=0;j<grayScaledImg.getHeight();j++)
            {
                int  red  = new Color(grayScaledImg.getRGB(i,j)).getRed(); 
                int alpha = new Color(grayScaledImg.getRGB(i, j)).getAlpha();
                if(red>threshold)
                   binarizedImg.setRGB(i,j,0xFFFFFF);
                else
                {
                   binarizedImg.setRGB(i,j,0);
                }
            }
        }
        return binarizedImg;  
    }
    
    
    public int calcThreshold(BufferedImage grayScaledImg)  //generates dynamic threshold 
    {
        Raster raster =grayScaledImg.getData();
        DataBuffer buffer = raster.getDataBuffer();
        DataBufferByte byteBuffer = (DataBufferByte) buffer;
        byte[] srcData = byteBuffer.getData();
        byte[] dstData = new byte[srcData.length];      
        int histData[] = new int[256];
        int ptr = 0;
        while (ptr < srcData.length)
        {
	    int h = 0xFF & srcData[ptr++];
	    histData[h] ++;
        }
	int total = srcData.length;
	float sum = 0;
	for (int t=0 ; t<256 ; t++) 
            sum += t * histData[t];

	float background_sum=0;
	int   background_weight=0,foreground_weight=0;

	float maximum_variance = 0;
	int   threshold= 0;

	for (int t=0 ; t<256 ; t++)
	{
	    background_weight  += histData[t];                                  // Weight Background
	    
            if (background_weight  == 0) 
                continue;
			
            foreground_weight = total - background_weight ;			// Weight Foreground
            
            if (foreground_weight == 0) 
                break;
            
            background_sum += (float) (t * histData[t]);
	    float background_mean = background_sum / background_weight ;	// Mean Background
	    float foreground_mean = (sum - background_sum ) /foreground_weight; // Mean Foreground

	                                                              
	    float between_variance = (float)background_weight                   // Calculate Between Class Variance
                                    *(float)foreground_weight 
                                    *(background_mean - foreground_mean)
                                    *(background_mean - foreground_mean);	

	    
	    if ( between_variance > maximum_variance)                           // Check if new maximum found
            {
		maximum_variance=  between_variance;
		threshold = t;
	    }
	}
	return threshold;
    }   
    public int getThreshold()
    {
      return threshold;
    }
} 


    

