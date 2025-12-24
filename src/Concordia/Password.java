package Concordia;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*---------------------------------------------------------------------------------------
/*
 * The 'Password' class holds is invoked on entry to system to guarantee security.
 * The password is held here.
 * Time contraints - enhancement. Check against the 'User' database. Is loaded in memory.
 * 
 ---------------------------------------------------------------------------------------*/
public class Password {
	public static void main(String[] args){
		String user = JOptionPane.showInputDialog(null,"User");
		String password = JOptionPane.showInputDialog(null,"Password");
		
		if ("root".equals(user) && "ROOT".equals(password)){
			JOptionPane.showMessageDialog(null,"login okay");}
			else
				JOptionPane.showMessageDialog(null,"login not okay");		
		}
	}


