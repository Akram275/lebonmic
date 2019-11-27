import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
public class Test{
    public static void main(String args[]){
	try{	
          String command = "xterm -e java ClientUDP 127.0.0.1 12312 JZHddez";
          Process proc = Runtime.getRuntime().exec(command);
          BufferedReader reader =  new BufferedReader(new InputStreamReader(proc.getInputStream()));
          String line = "";
          while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
          }

        proc.waitFor();   
	}catch(Exception e){}
    }
}
