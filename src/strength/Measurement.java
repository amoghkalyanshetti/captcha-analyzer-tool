/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strength;

/**
 *
 * @author Shree Ganesh
 */
public class Measurement {
    
    public float segmentationRate;
    public float per;
    public String extraCharDetected="";
    public String CLASS;
    public static void main(String args[])
    {
        java.util.Scanner inp=new java.util.Scanner(System.in);
        String m,u;
        System.out.println("Machine:");
        m=inp.next();
        System.out.println("User:");
        u=inp.next();
        new Measurement().measure(m, u);
                
   }
    public float measure(String machine,String user)
    {
        int correctChar=0;
        int machineLength=machine.length();
        int userLength=user.length();
        segmentationRate=(float)machineLength*100/userLength;
        if(machineLength<=userLength)
        {
            for(int i=0;i<machineLength&&i<userLength;i++)//formacLen==userLen and macLength<userLen
            {
                if(machine.charAt(i)==user.charAt(i))
                {
                    ++correctChar;
                }
            }
        }
        if(machineLength>userLength)
        {
            int last=0;
            int i,j;
            for(i=0,j=0;i<machineLength&&i<userLength;i++,j++)//formacLen==userLen and macLength<userLen//i pointer of user n j pointer of machine
            {
                if(machine.charAt(j)==user.charAt(i))//match found
                { 
                    System.out.println("checking at "+i);
                    ++correctChar;
                    last=j;
                }
                else //match not found with current location
                {
                     extraCharDetected=extraCharDetected+machine.charAt(j);//i
                     System.out.println("Not got "+user.charAt(i));
                    for(int k=j+1;k<machineLength;k++)//i+1
                    {
                        if(machine.charAt(k)==user.charAt(i))
                        {
                            ++correctChar;
                            j=k;
                            last=j;
                            System.out.println("GOT at "+j);
                            break;
                        }
                        else
                        {
                            System.out.println(" NOT GOT");
                            extraCharDetected=extraCharDetected+machine.charAt(k);
                        }
                    }
                }
            }
            if(i<machineLength)
            extraCharDetected=extraCharDetected+machine.substring(last+1);
             segmentationRate=(float)100.0-((float)segmentationRate-100);
        }
        per=((float)correctChar/userLength)*100;
     /*   if(machineLength>userLength&&per==100)
         {
             extraCharDetected=machine.substring(userLength,machineLength);
         }*/
        //int guessedCorrect=userLength-wrongChar-(userLength-machineLength);
         per=((float)correctChar/userLength)*100;
         
         
       
         if(per>75)
         {
             CLASS="WEAK";
         }
         else if(per>50)
         {
             CLASS="MODERATE";
         }
         else
         {
             CLASS="STRONG";
         }
        System.out.println("Correct char:"+correctChar+"\nPercentage: "+per+"\n Extra detected:"+extraCharDetected+"\n segementationRate: "+segmentationRate+"\n CLASS : "+CLASS);
        return per;
    }
}