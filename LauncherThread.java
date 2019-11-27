import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;



public class LauncherThread implements Runnable{
  String ip;
  String port;
  String username;
  int udp_listen;

  public LauncherThread(int udp_listen, String username){
    //this.ip = ip;
    //this.port = port;
    this.username = username;
    this.udp_listen = udp_listen;
  }

  public void run(){
    try{
      String command = "xterm -e java ClientUDP "+this.udp_listen+" " +this.username;
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
