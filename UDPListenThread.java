import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;


public class UDPListenThread implements Runnable{
  ClientUDP c;

  public UDPListenThread(ClientUDP c){
    this.c = c;
  }

  public void run(){

    try{
      DatagramSocket dso=new DatagramSocket(this.c.udp_listen);
      byte[]data=new byte[100];
      DatagramPacket paquet=new DatagramPacket(data,data.length);
      String st;
      boolean flag = false;
      int port_;
      String[] cnx;
      String username;
      String ip;
      while(true){
        dso.receive(paquet);
        st=new String(paquet.getData(),0,paquet.getLength());
        cnx = st.split("\n");
        //System.out.println("cnx" + cnx[0]);
        if(cnx[0].equals("CONNECT")){
          for(int i =0;i<this.c.contacts.size();i++){
            if(cnx[2].equals(this.c.contacts.get(i).get_username())){
              flag = true;
            }
          }
          if(!flag){
            port_ = Integer.parseInt(cnx[1]);
            username = cnx[2];
            ip = paquet.getAddress().toString();
            User u = new User(username, port_, ip);
            this.c.contacts.add(u);
            System.out.println("Un nouveau contact à été ajouté");
            System.out.println("son username : "+username);
            /*Maintenant Il faut envoyer nos information au client distant pour qu'il fasse de méme */
            byte[] data1;
            String mess = "CONNECT\n"+this.c.udp_listen+"\n"+this.c.username;
            data1 = mess.getBytes();
            DatagramSocket dso1 = new DatagramSocket();
            System.out.println(ip.substring(1));
            InetSocketAddress ia=new  InetSocketAddress(ip.substring(1),port_);
            DatagramPacket paquet2=new DatagramPacket(data,data.length,ia);
            dso1.send(paquet2);
          }
        }else{
          System.out.println(st);
        }
      }
    } catch(Exception e){e.printStackTrace();}

}

}
