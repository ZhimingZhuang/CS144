import java.security.*;
import java.io.*;

public class ComputeSHA {
	
	public static void main(String[] args)throws Exception{
		
		MessageDigest md = MessageDigest.getInstance("SHA-1");	
		FileInputStream fis = new FileInputStream(args[0]);
		
		int nread = 0;
		byte[] dataBytes = new byte[1024];
		
		while((nread = fis.read(dataBytes)) != -1){
			md.update(dataBytes,0,nread);
		}
			
		byte[] byteData = md.digest();
		
		StringBuffer hexString = new StringBuffer();
		for(int i = 0; i < byteData.length; i++){
			String hex = Integer.toHexString(0xFF & byteData[i]);
			if(hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		System.out.println(hexString.toString());
	}
}

