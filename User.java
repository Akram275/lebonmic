import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.security.*;


public class User{
  private String username;
  private int UDP; //port udp
  private boolean connected;
  private ArrayList<Article> products;
  private String IP;
  private Key key;
	private SecretKey AES_key;

  public User(String username, int port, String ip, Key k){
    this.username = username;
    this.UDP = port;
    this.connected = true;
    this.products = new ArrayList<Article>();
    this.IP = ip;
	if ((k)){
	    this.key = k;
	}
    //this.keys = new KeyPairGenerator("RSA");
  }

  public String get_username(){
    return this.username;
  }
  public ArrayList<Article> get_products(){
    return this.products;
  }
  public int get_port(){
    return this.UDP;
  }
  public void set_port(int d){
    this.UDP = d;
  }
  public boolean is_connected(){
    return this.connected;
  }
  public void set_connected(boolean b){
    this.connected =b;
  }
  public ArrayList<Article> get_articles(){
    return this.products;
  }
  public String get_ip(){
    return this.IP;
  }
  public void set_ip(String ip){
    this.IP = ip;
  }
  public Key get_key(){
    return this.key;
  }
  public void setKey(Key k){
    this.key = k;
  }
  public void setAesKey(SecretKey k){
    this.AESkey = k;
  }
}
