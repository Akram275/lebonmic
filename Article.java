import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;




public class Article{
	private String		name;
	private int			id;
	private int			price;
	private String		desc;
	private String		domaine;
	private static int	count; //Pour les identifiants


	public Article(String name, String desc, int price, String domaine){
		this.name = name;
		this.desc = desc;
		this.id = count++;
		this.price = price;
		this.domaine = domaine;
	}

	public String get_name(){
		return this.name;
	}
	public String get_desc(){
		return this.desc;
	}
	public int get_id(){
		return this.id;
	}
	public int get_price(){
		return this.price;
	}
	public String get_domaine(){
		return this.domaine;
	}
	public int get_ref(){
		return this.id;
	}

}
