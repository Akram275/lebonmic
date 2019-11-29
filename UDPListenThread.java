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

	User		AddUser(String username, int port, String ip, Key RSA_pub)
	{
		User	u = new User(username, port, ip, RSA_pub);

		this.c.contacts.add(u);
		System.out.println("\n****************************************************");
		System.out.println("*       Un nouveau contact à été ajouté              *");
		System.out.println("*       son username : "+username+"                  *");
		System.out.println("******************************************************");
		return (u);
	}

	User		FindUser(String username, int port, String ip, Key RSA_pub)
	{
		User	u;

		for (int i = 0; i < this.c.contacts.size(); i++){
			u = this.c.contacts.get(i);
			if (username.equals(u.get_username())){
				//user update ? -> refresh eventuel de ip/port/Key ?
				return (u);
			}
		}
		if (port > 0){
			return (AddUser(username, port, ip, RSA_pub));
		}else {
			return (null);
		}
	}

	public void	run(){

		try{
			DatagramSocket dso = new DatagramSocket(this.c.udp_listen);
			byte[]data = new byte[100];
			DatagramPacket paquet = new DatagramPacket(data,data.length);
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
				st = new String(paquet.getData(), 0, paquet.getLength());
				cnx = st.split("\n");
				cnx2 = st.split("***");
				//System.out.println("cnx" + cnx[0]);
				if (cnx.size() == 6){//CAS: Client nous envoie SPEAK
					ip = cnx[1];
					port_ = Integer.parseInt(cnx[2]);
					username = cnx[3];
					k = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cnx[4].getBytes()));
					if (cnx[0].equals("OK")){ // message de contact bien formate
						FindUser(username, port_, ip, k);
						byte[] data1;
						//Generation/Chiffrement de CleAES (chiffrement grace a k)
						//Update de u avec CleAESChifree
						String mess = "[CleAESChiffree]"+"***"+username+"***"+this.c.username;
						data1 = mess.getBytes();
						DatagramSocket		dso1 = new DatagramSocket();
						InetSocketAddress	ia = new InetSocketAddress(ip.substring(1),port_);
						DatagramPacket		paquet2 = new DatagramPacket(data1,data1.length,ia);
						dso1.send(paquet2);
					}
				}else if (cnx2.size() == 3){//Connection depuis un client
					username = cnx[2];
					ip = dso.getInetAddress().getAddress().toString();
					port_ = dso.getInetAddress().getPort();
					u = FindUser(username, port_, ip, null); //RSA_pub: On en pas besoin mais comment faire ?
					//Dechiffrement de CleAESChiffre avec this.RSA_priv ?
					//Update de u avec CleAESChiffre ?
				}else if (cnx2.size() == 2){ // Cas Client connu (a priori) et message chiffre
					username = cnx2[0];
					//u = FindUser(username, 0, null, null); //seuls des contacts avec cle peuvent nous parler
					//Dechiffrement: mess = cn[1].dechiffrement(u.get_AESKey);
					//System.out.print("\n"+ u.get_username()+ "> "+mess+"\n\nusername : ");
				}else if (cnx2.size() == 4 && cnx2[1].equals("CLIENT")
						&& cnx2[2].equals("UDP") && cnx2[3].equals("END")){ //Cas de deconnection
					username = cnx2[0];
					//pop contact de la liste ?
				}
			}
		} catch(Exception e){e.printStackTrace();}
	}

}
