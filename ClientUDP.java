import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class ClientUDP{
	ArrayList<User>	contacts; //Liste des locuteurs
	int				udp_listen;
	String			username;


	public ClientUDP(int udp_listen, String username){
		this.udp_listen = udp_listen;
		this.username = username;
		contacts = new ArrayList<User>();
	}

	public static void main(String[] args){
		if (args.length != 2){
			System.out.println("Error occured");
			System.exit(1);
		}
		/*On récupére le port sur lequel on va écouter les Communications*/
		int			udp_int = Integer.parseInt(args[0]);
		Stringu		username = args[1];
		ClientUDP	cl_udp = new ClientUDP(udp_int, username);
		Thread		t = new Thread(new UDPListenThread(cl_udp));
		t.start();
		Scanner		sc = new Scanner(System.in);
		String		s;
		User		u = null;
		byte[]		data;

		System.out.println("---------------------Communications de "+cl_udp.username+"-----------");
		try{
			DatagramSocket		dso = new DatagramSocket();
			while (true){
				if (cl_udp.contacts.size() == 0){
					System.out.println("Vous n'avez pour l'instant pas de contacts");
					while (cl_udp.contacts.size() == 0){
						Thread.sleep(1000);
					}
				}
				if (cl_udp.contacts.size() > 0){
					System.out.println("A qui voulez vous envoyer de message ? : ");
					for (int i = 0;i<cl_udp.contacts.size();i++){
						System.out.println(" "+i+") "+cl_udp.contacts.get(i).get_username());
					}
				}
				System.out.print("username : ");
				username = sc.nextLine();
				for (int i = 0; i < cl_udp.contacts.size(); i++){
					if (username.equals(cl_udp.contacts.get(i).get_username())){
						u = cl_udp.contacts.get(i);
					}
				}
				if (u == null){
					System.out.println("Cet utilisateur ne fait pas parti de vos contacts\n");
					continue ;
				}
				//Envoi
				System.out.print("message : ");
				s = sc.nextLine();
				data = s.getBytes();
				//Chiffrement AES avec u.getAESKey()
				InetSocketAddress	ia = new InetSocketAddress(u.get_ip().substring(1),u.get_port());
				DatagramPacket		paquet = new DatagramPacket(data,data.length,ia);
				dso.send(paquet);
				u = null;
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
