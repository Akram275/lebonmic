import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.security.*;
import java.lang.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ServeurThread implements Runnable{
  public Serveur s;
  public Socket so;
  public User u;
  public int indice; //L'indice de l'utilisateur dans la liste des utilisateur

  public ServeurThread(Serveur s, Socket so){
    this.s = s;
    this.so = so;
    this.u = null;
  }



  public void SIGN_UP(BufferedReader br, PrintWriter pw){
    try{

      Base64.Decoder decoder = Base64.getDecoder();
      System.out.println("on est dans SIGNUP");
      String username = br.readLine();
      String udp = br.readLine();
      String key_pub = br.readLine();
      System.out.println(key_pub);
      Key k = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoder.decode(key_pub)));
      String end = br.readLine();

      System.out.println ("username = {" + username + "}");
      System.out.println ("udp = {" + udp + "}");
      System.out.println ("end = {" + end + "}");




      if(!end.equals("***")){
        System.out.println ("signup end case");
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u != null){
        pw.print("ERROR\nALREADY_LOGIN\n***\n");
        pw.flush();
        return;
      }
      for(int i = 0;i<username.length();i++){
        if((int)username.charAt(i) >= 126 || (int)username.charAt(i) < 32){
          pw.print("ERROR\nSPECIAL_CHARACTER\n***\n");
          pw.flush();
          return;
        }
      }
      for(int i = 0;i<this.s.users.size();i++){
        if(this.s.users.get(i).get_username().equals(username)){
          pw.print("ERROR\nLOGIN_UNVAILABLE\n***\n");
          pw.flush();
          return;
        }
      }
      try{
        int udp_int = Integer.parseInt(udp);
        String ip = this.so.getInetAddress().toString();
        User u = new User(username, udp_int, ip, k);
        //u.setKey(k);
        this.u = u;
        this.s.users.add(u);

        this.indice = this.s.users.size() - 1;
        pw.print("OK\n***\n");
        pw.flush();
        System.out.println ("SIGN UP Everything is fine");
        return;
      }catch(Exception e){e.printStackTrace();}

      pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }

  public void LOGIN(BufferedReader br, PrintWriter pw){
    try{

      String username = br.readLine();
      String udp = br.readLine();
      String key = br.readLine();
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u != null){
        pw.print("ERROR\nALREADY_LOGIN\n***\n");
        pw.flush();
        return;
      }
      for(int i =0;i<this.s.users.size();i++){
        if(this.s.users.get(i).get_username().equals(username)){
          int udp_int = Integer.parseInt(udp);
          this.u = this.s.users.get(i);  //Un racourcis pour acceder plus vite aux info
          this.u.set_connected(true);
          this.s.users.get(i).set_connected(true);
          this.u.set_port(udp_int);
          this.s.users.get(i).set_connected(true);
          pw.print("OK\n***\n");
          pw.flush();
          return;
        }
      }
      pw.print("ERROR\nLOGIN_FALSE\n***\n");
      pw.flush();


    }catch(Exception e){
      e.printStackTrace();
      pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
      pw.flush();
    }
  }
  public void ASKARTICLES(BufferedReader br, PrintWriter pw){
    try{
      String domaine = br.readLine();
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }

      for(int i = 0;i<this.s.products.size();i++){
        if(this.s.products.get(i).get_name().equals(domaine)){
          pw.println(this.s.products.get(i).get_name());
          pw.flush();
          for(int j = 0;j<this.s.products.get(i).get_products().size();j++){
            System.out.println("on est dans la boucle");
            pw.print(this.s.products.get(i).get_products().get(j).get_name() + "\n" +
                     this.s.products.get(i).get_products().get(j).get_price() + "\n" +
                     this.s.products.get(i).get_products().get(j).get_desc()   + "\n" +
                     this.s.products.get(i).get_products().get(j).get_ref()   +"\n"


            );
            System.out.print(this.s.products.get(i).get_products().get(j).get_name() + "\n" +
                     this.s.products.get(i).get_products().get(j).get_price() + "\n" +
                     this.s.products.get(i).get_products().get(j).get_desc()   + "\n" +
                     this.s.products.get(i).get_products().get(j).get_ref()   +"\n"
                     );
            pw.flush();
          }
          pw.print("END\n***\n");
          pw.flush();
          return;
        }
      }
      pw.print("ERROR\nNO_EXISTANT_DOMAIN\n***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }

  public void QUIT(BufferedReader br, PrintWriter pw){
    try{
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      pw.print("OK\n***\n");
      pw.flush();
      pw.close();
      this.so.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public void ASKDOMAIN(BufferedReader br, PrintWriter pw){
    try{
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      for(int i = 0;i<this.s.products.size();i++){
        pw.print(this.s.products.get(i).get_name()+"\n");
        pw.flush();
      }
      pw.print("***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }
  public void ASKMYARTICLES(BufferedReader br, PrintWriter pw){

    try{
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u == null){
        pw.print("ERROR\nUSER_NOT_LOGIN\n***\n");
        pw.flush();
        return;
      }
      for(int i = 0;i<this.u.get_articles().size();i++){
        pw.print(this.u.get_articles().get(i).get_domaine() +"\n"+
               this.u.get_articles().get(i).get_name()      +"\n"+
               this.u.get_articles().get(i).get_price()     +"\n"+
               this.u.get_articles().get(i).get_desc()      +"\n"+
               this.u.get_articles().get(i).get_ref()       +"\n"
               );
        pw.flush();

      }
      pw.print("END\n***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }
  public void ADDARTICLE(BufferedReader br, PrintWriter pw){
    try{

      String domaine = br.readLine();
      System.out.println(domaine);
      String nom = br.readLine();
      System.out.println(nom);
      String prix = br.readLine();
      System.out.println(prix);
      String desc = br.readLine();
      System.out.print(desc);
      String end = br.readLine();
      if(this.u == null){
        pw.print("ERROR\nUSER_NOT_LOGIN\n***\n");
        pw.flush();
        return;
      }
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      int prix_int = Integer.parseInt(prix);
      for(int i = 0;i<this.s.products.size();i++){
        if(domaine.equals(this.s.products.get(i).get_name())){
          Article a = new Article(nom, desc, prix_int, domaine);
          System.out.println(this.s.products.get(i).get_name());
          System.out.println(this.s.products.get(i).get_products().size());
          this.s.products.get(i).get_products().add(a);
          System.out.println(this.s.products.get(i).get_products().size());
          this.u.get_articles().add(a);
          //a revoir;
          pw.print("OK\n***\n");
          pw.flush();
          return;
        }

      }
      pw.print("ERROR\nDOMAIN_UKNOWN\n***\n");
      pw.flush();

    }catch(Exception e){e.printStackTrace();pw.println("ERROR\nWRONG_ARGUMENT\n***\n");pw.flush();}
  }

  public void LOGOUT(BufferedReader br, PrintWriter pw){
    try{
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u == null){
        pw.print("ERROR\nUSER_NOT_LOGIN\n***\n");
        pw.flush();
        return;
      }
      this.u = null;
      pw.print("OK\n***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }
  public void DELARTICLE(BufferedReader br, PrintWriter pw){
    try{
      String id = br.readLine();
      int id_int = Integer.parseInt(id);
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u == null){
        pw.print("ERROR\nUSER_NOT_LOGIN\n***");
        pw.flush();
        return;
      }
      for(int i = 0;i<this.u.get_articles().size();i++){
        if(id_int == this.u.get_articles().get(i).get_ref()){
          //On commence par le supprimer de la liste des articles de l utilisateur
          this.u.get_articles().remove(i);
          System.out.println("rm dans les articles du user");
          //On le cherche ensuite parmi les articles stockée dans le serveur
          for(int j = 0;j<this.s.products.size();j++){
            for(int k = 0;k<this.s.products.get(j).get_products().size();k++){
              if(this.s.products.get(j).get_products().get(k).get_ref() == id_int){
                this.s.products.get(j).get_products().remove(k);
                System.out.println("size aprés le rm = "+ this.s.products.get(j).get_products().size() );
                pw.print("OK\n***\n");
                pw.flush();
                return;
              }
            }
          }
        }
      }
      pw.print("ERROR\nID_NOT_FOUND\n***\n");
      pw.flush();
    }catch(Exception e){e.printStackTrace();}
  }
  public void ADDDOMAIN(BufferedReader br, PrintWriter pw){


  }
  public void SPEAK(BufferedReader br, PrintWriter pw){
    try{
      String ip = br.readLine();
      int ip_int = Integer.parseInt(ip);
      String end = br.readLine();
      if(!end.equals("***")){
        pw.print("ERROR\nWRONG_ARGUMENT\n***\n");
        pw.flush();
        return;
      }
      if(this.u == null){
        pw.print("ERROR\nUSER_NOT_LOGIN\n***\n");
        pw.flush();
        return;
      }
      for(int i = 0;i<this.s.users.size();i++){
        for(int j = 0;j<this.s.users.get(i).get_articles().size();j++){
          if(ip_int == this.s.users.get(i).get_articles().get(j).get_ref()){
            if(this.s.users.get(i).is_connected()){
              pw.print("OK\n"+this.s.users.get(i).get_ip()+   "\n"
              + this.s.users.get(i).get_port() +   "\n"
              + this.s.users.get(i).get_username() + "\n"
              + this.s.users.get(i).getRSA()       + "\n"
              +"***\n");
              pw.flush();
              return;
             }
            pw.print("ERROR\nUSER_NOT_LOGIN\n***\n");
            pw.flush();
           }
         }
        }
        pw.print("ERROR\nUSER_MISSING\n***\n");
        pw.flush();
     }catch(Exception e){e.printStackTrace();}
  }
  public void run(){
    try{
      BufferedReader br = new BufferedReader(new InputStreamReader(this.so.getInputStream()));
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.so.getOutputStream()));
      this.so.setTcpNoDelay(true);
      pw.print("bienvenu sur le-bon-mic\n");
      pw.flush();


      while(true){
        String rec = br.readLine();
        System.out.println(rec);
        switch(rec){


          case "SIGNUP" :
            System.out.println("case SIGN UP");
            SIGN_UP(br, pw);
            break;

          case "LOGIN" :
            LOGIN(br, pw);
            break;

          case "ADDARTICLE" :
            ADDARTICLE(br, pw);
            break;

          case "ASKARTICLES" :
            ASKARTICLES(br, pw);
            break;

          case "ASKDOMAIN" :
            ASKDOMAIN(br, pw);
            break;

          case "ASKMYARTICLES" :
            ASKMYARTICLES(br, pw);
            break;

          case "LOGOUT" :
            LOGOUT(br, pw);
            break;

          case "DELARTICLE":
            DELARTICLE(br, pw);
            break;

          case "ADDDOMAIN":
            ADDDOMAIN(br, pw);
            break;

          case "QUIT" :
            QUIT(br, pw);
            break;

          case "SPEAK":
            SPEAK(br, pw);
            break;

          default :
            System.out.println ("UNKNOWN_COMMAND");
            pw.print("ERROR\nUNKNOWN_COMMAND\n***\n");
            pw.flush();
            break;
    }
   }
  }catch(Exception e){e.printStackTrace();}
 }
}
