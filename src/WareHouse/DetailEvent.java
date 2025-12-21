package WareHouse;

import java.util.EventObject;

//------------------------------------------------------------------
//
// Not used here,..for use with custom fired events
//
//----------------------------------------------------------------
public class DetailEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String text;
	
	public DetailEvent(Object source,String text){
		super(source);
		
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
}
