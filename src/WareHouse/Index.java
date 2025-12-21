package WareHouse.domain;


//---------------------------------------------------------------------------
// Used in conjunction for the sort function by date. Not fully implemented.
//
// ---------------------------------------------------------------------------
import java.util.Date;

public class Index {
	
		int i,j,k;
		Date date;
		
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
		public int getJ() {
			return j;
		}
		public void setJ(int j) {
			this.j = j;
		}
		public int getK() {
			return k;
		}
		public void setK(int k) {
			this.k = k;
		}
		public Date getDated() {
			return date;
		}
		public void setDated(Date date) {
			this.date = date;
		}
	
	   public String toString(){
		   return ("\n"+i+j+k+date);
	   }
}
