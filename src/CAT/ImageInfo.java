package CAT;
import java.awt.image.BufferedImage;
public class ImageInfo
{
    public int threshold;
    public BufferedImage originalImage;
    public BufferedImage grayScaledImage;
    public BufferedImage binarizedImage;
    public BufferedImage preprocessedImage;
    public BufferedImage segments[];
    public BufferedImage scaledSegments[];
    public BufferedImage thinnedSegments[];
    
    public enum website {

       AADHAR,
       FREEDNS,
        AOLMAIL, 
        AVECMOBILE, 
        BMACCESS, 
        BOTDETECT, 
        CCAVENUE, 
        CEOMAHARASHTRA, 
        CHALLANSTATUS,
        CITYALLY, 
        DAILYMAILUK, 
        DOOSTANG,
        EASYBM,
        EBAY,
        ESCHOLARSHIP, 
        GATE,
        HOTMAIL,
        INDIANEXPRESS,
        IRCTC, 
        KUIZER,
        MSRCTC,
        MYSPACE, 
        PCSTORE,
        PIXELGROOVY,
        REDIFF,
        SBI,
        SHIKSHA, 
        SIXTEENBYTWO, 
        SNAPSHOT, 
        SPEAKINGTREE, 
        TEST,
        TINYPIC,
        VODAFONE, 
        WEBSPAMPROTECT,
        WIKIPEDIA, 
        YEBHI,


    }
    public ImageInfo()
    {
       originalImage=null;
       grayScaledImage=null;
       binarizedImage=null;
       preprocessedImage=null;
       threshold=-1;
    }  
}
 /*
    BufferedImage CreateImage()
    {
         return new BufferedImage(2400,2400,BufferedImage.TYPE_INT_RGB);
    }
    */


	



