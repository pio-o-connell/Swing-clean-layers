package Concordia;

//--------------------------------------------------------------------------------------
// Comparator to be used with sort operations as part of  'Item Report' functionality.
//
//-------------------------------------------------------------------------------------


import java.util.Comparator;
import java.util.Date;
import Concordia.domain.Index;

@SuppressWarnings("rawtypes")
public class DateComparator implements Comparator<Index> {
 

	public int compare(Index o1, Index o2) {
		Date date1 = o1.getDated();
		Date date2 = o2.getDated();
		return date1.compareTo(date2);
	}


}


