import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.security.*;
import java.lang.*;
public class Client{



  public static void main(String args[]){

    try{
      Scanner sc = new Scanner(System.in);
      String username = "-"; //Sera modifiée par la suite car dans SPEAK on suppose qu'elle initialisée
      String username2;
      String ip;
      String domaine;
      /***********************Element de securité***********************/
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      kpg.initialize(1024);
      KeyPair kp;
      Key pub;
      String key_pub;
      String key_pub_distant;
      Key pvt;
      Base64.Encoder encoder = Base64.getEncoder();
      /****************************************************************/
      String name;
      String desc;
      int prix;
      int id;
      String rep;
      Thread t = new Thread();
      int port;
      LauncherThread launch = null;
      int udp_listen = 0;
      String cmd;
      Socket so = new Socket("localhost", 4450); //172.28.173.21
      BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(so.getOutputStream()));
      so.setTcpNoDelay(true);
      String fst = br.readLine();
      System.out.print("->" +fst + "\n");
      System.out.println(">  : Command line ");
      System.out.println("-> : server responses ");
      while(true){
        System.out.print(">");
        cmd = sc.nextLine();
        //System.out.println(cmd);
        switch(cmd){
          case "SIGNUP" :
            pw.println(cmd);
            pw.flush();
            System.out.print("username : ");
            username = sc.nextLine();
            //System.out.println ("username = {" + username + "} + len = " + username.length());
            pw.println(username);
            pw.flush();
            System.out.print("udp port n° : ");

            port = Integer.parseInt(sc.nextLine());
            udp_listen = port;
            kpg.initialize(1024);
            kp = kpg.generateKeyPair();
            pub = kp.getPublic();
            pvt = kp.getPrivate();
            key_pub = encoder.encodeToString(pub.getEncoded());
            pw.println(port);
            pw.println(key_pub);
            pw.flush();
            pw.print("***\n");
            pw.flush();
            //System.out.println("on est al");
            rep = "";
            //System.out.println ("AZPOEIZAPOIEPOZAIEPOZA");
            while(!rep.equals("***")){
                rep = "";
                rep = br.readLine();
                if(rep.equals("OK")){
                  launch = new LauncherThread(udp_listen, username);
                  t = new Thread(launch);
                  t.start();
                }
                if(!rep.equals("***")){
                  System.out.print("->" + rep + "\n");
                }
            }

            break;

          case "LOGIN" :
          pw.println(cmd);
          pw.flush();
          System.out.print("username : ");
          username = sc.nextLine();
          //System.out.println ("username = {" + username + "} + len = " + username.length());
          pw.println(username);
          pw.flush();
          System.out.print("udp port n° : ");
          port = Integer.parseInt(sc.nextLine());
          udp_listen = port;
          pw.println(port);
          pw.flush();
          pw.print("***\n");
          pw.flush();
          //System.out.println("on est al");
          rep = "";
          //System.out.println ("AZPOEIZAPOIEPOZAIEPOZA");
          while(!rep.equals("***")){
              rep = "";
              rep = br.readLine();
              if(rep.equals("OK")){
                launch = new LauncherThread(udp_listen, username);
                t = new Thread(launch);
                t.start();
              }
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
          }
          break;

          case "ADDARTICLE" :
            pw.println(cmd);
            pw.flush();
            pw.flush();
            System.out.print("domain : ");
            domaine = sc.nextLine();
            pw.println(domaine);
            pw.flush();
            System.out.print("name : ");
            name = sc.nextLine();
            pw.println(name);
            pw.flush();
            System.out.print("price : ");
            prix = Integer.parseInt(sc.nextLine());
            pw.println(prix);
            pw.flush();
            System.out.print("desc : ");
            desc = sc.nextLine();
            pw.println(desc);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            break;

          case "ASKARTICLES" :
            pw.println(cmd);
            pw.flush();
            System.out.print("domain : ");
            domaine = sc.nextLine();
            pw.println(domaine);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            break;

          case "ASKDOMAIN" :
            pw.println(cmd);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            break;

          case "ASKMYARTICLES" :
            pw.println(cmd);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            break;

          case "LOGOUT" :
            pw.println(cmd);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(rep.equals("OK")){
                launch.proc.destroy();
              }
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            break;

          case "DELARTICLE":
            pw.println(cmd);
            pw.flush();
            System.out.print("product id : ");
            id = Integer.parseInt(sc.nextLine());
            pw.println(id);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              System.out.print("->" + rep + "\n");

            }
            break;

          case "ADDDOMAIN":
            pw.println(cmd);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              System.out.print("->" + rep + "\n");

            }
            break;

          case "QUIT" :
            pw.println(cmd);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = "";
            while(!rep.equals("***")){
              rep = "";
              rep += br.readLine();
              if(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
              }
            }
            launch.proc.destroy();
            return;

          case "SPEAK":
            pw.println(cmd);
            pw.flush();
            System.out.print("product id : ");
            id = Integer.parseInt(sc.nextLine());
            pw.println(id);
            pw.flush();
            pw.println("***");
            pw.flush();
            rep = br.readLine();
            if(rep.equals("ERROR")){
              while(!rep.equals("***")){
                System.out.print("->" + rep + "\n");
                rep = br.readLine();
                }
                break;
            }
            ip = br.readLine();
            String port_s = br.readLine();
            username2 = br.readLine();
            key_pub_distant = br.readLine();

            br.readLine(); // Les 3 étoiles
            byte[] data;
            String mess = "OK\n"ip+"\n"+udp_listen+"\n"+username+"\n"+key_pub_distant;
            //System.out.println("j'envoie "+mess);
            data = mess.getBytes();
            try{
              DatagramSocket dso = new DatagramSocket();
              System.out.println(ip.substring(1));
              InetSocketAddress ia=new  InetSocketAddress("localhost",port);
              DatagramPacket paquet=new DatagramPacket(data,data.length,ia);
              dso.send(paquet);
            }catch(Exception e){e.printStackTrace();}
            //Thread t = new Thread(new ClientUDP(ip, port, username));
            //t.start();
            //Thread t = new Thread(new LauncherThread(udp_listen));
            //t.start();
            //Runtime.getRuntime().exec(command);
            //Runtime.getRuntime().exec("/bin/bash -c ls");
            //Runtime.getRuntime().exec("../../../../../../usr/bin/x-terminal-emulator");

            break;

          case "" : //Enter
            break;

          default :
            System.out.println ("UNKNOWN_COMMAND");
            System.out.print("SUPPORTED COMMANDS : ");
            System.out.print("SIGN UP -");
            System.out.print(" LOGIN -");
            System.out.print(" LOGOUT -");
            System.out.print(" ASKDOMAIN -");
            System.out.print(" ASKARTICLES -");
            System.out.print(" ASKMYARTICLES -");
            System.out.print(" ADDDOMAIN -");
            System.out.print(" ADDARTICLE -");
            System.out.print(" DELARTICLE -");
            System.out.print(" SPEAK \n");

            break;
    }
   }
  }catch(Exception e){e.printStackTrace();}

  }

}
