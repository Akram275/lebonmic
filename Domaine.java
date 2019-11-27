import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;


public class Domaine{
  private String name;
  private ArrayList<Article> products;


  public Domaine(String name){
    this.name = name;
    this.products = new ArrayList<Article>();
  }

  public String get_name(){
    return this.name;
  }
  public ArrayList<Article> get_products(){
    return this.products;
  }

}
