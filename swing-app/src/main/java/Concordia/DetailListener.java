package concordia;

//import DetailEvent;

import java.util.EventListener;


public interface DetailListener extends EventListener {
	public void detailEventOccured(DetailEvent event);
		
}
