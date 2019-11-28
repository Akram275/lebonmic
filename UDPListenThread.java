import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.security.*;

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
      boolean flag;
      Key k;
      User u;
      String id_emetteur;
      String id_receveur;
      int port_;
      String[] cnx;
      String[] cnx2;
      String username;
      String ip;

      while(true){
        flag = false;
        dso.receive(paquet);
        st=new String(paquet.getData(),0,paquet.getLength());
        cnx = st.split("\n");
        cnx2 = st.split("***");
        //System.out.println("cnx" + cnx[0]);
        if(cnx.size() == 6){
          port_ = Integer.parseInt(cnx[2]);
          username = cnx[3];
          ip = cnx[1];
          if(cnx[0].equals("OK")){
             for(int i =0;i<this.c.contacts.size();i++){
               if(cnx[3].equals(this.c.contacts.get(i).get_username())){
                 u = this.c.contacts.get(i);
                 flag = true;
              }
            }
            if(!flag){//from client
              k = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cnx[4].getBytes()));
              u = new User(username, port_, ip, k);
              this.c.contacts.add(u);
              System.out.println("\n****************************************************");
              System.out.println("*       Un nouveau contact à été ajouté              *");
              System.out.println("*       son username : "+username+"                  *");
              System.out.println("******************************************************");
              /*Maintenant Il faut envoyer nos information au client distant pour qu'il fasse de méme */
            }
          byte[] data1;
          //Generation de CLE
          String mess = "[CleAESChiffree]"+"***"+username+"***"+this.c.username;
            //System.out.println("j'envoie "+mess);
            data1 = mess.getBytes();
            DatagramSocket dso1 = new DatagramSocket();
            //System.out.println(ip.substring(1));
            InetSocketAddress ia = new  InetSocketAddress(ip.substring(1),port_);
            DatagramPacket paquet2=new DatagramPacket(data1,data1.length,ia);
            dso1.send(paquet2);
          }
        }
      }else if(cnx2.size() == 3){

          for(int i = 0;i<this.c.contacts.size();i++){
            if(paquet.getAddress().toString().equals(this.c.contacts.get(i).get_ip())){
              System.out.print("\n"+this.c.contacts.get(i).get_username()+ "> "+st+"\n\nusername : ");
            }
          }
          //System.out.println("st);
        }
      }
    } catch(Exception e){e.printStackTrace();}

}

}
