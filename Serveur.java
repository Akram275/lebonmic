import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;


public class Serveur{
  ArrayList<User> users;
  ArrayList<Domaine> products;

  Serveur(){
    this.users = new ArrayList<User>();
    this.products = new ArrayList<Domaine>();
    Domaine d1 = new Domaine("Téléphonie");
    Domaine d2 = new Domaine("Vélos");
    Domaine d3 = new Domaine("Automobile");
    Domaine d4 = new Domaine("Immobilier");
    this.products.add(d1);
    this.products.add(d2);
    this.products.add(d3);
    this.products.add(d4);
  }

  public static void main(String args[]){
    Serveur s = new Serveur();
    try{
      ServerSocket so = new ServerSocket(4450);
      while(true){
        Socket sock = so.accept();
        Thread t = new Thread(new ServeurThread(s, sock));
        t.start();

      }
    }catch(Exception e){e.printStackTrace();}


  }

}
