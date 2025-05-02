package micro;

public class ReservationStation {
	String tag;
	boolean busy;
	String operation ;
	double vj;
	double vk;
	String qj;
	String qk;
	int address;
	int time;
	
	
	
	public ReservationStation(String tag) {
		this.tag = tag;
		this.busy = false;
		operation = "";
		vj = 0;
		vk = 0;
		qj = "";
		qk = "";
		address = 0;
		time = -1;
	}
	
	public String toString() {
		return "Reservation Staion [tag=" + tag + ", busy=" + busy + ", operation=" + operation
		+ ", vj=" + vj + ", vk=" + vk +", qj=" + qj+", qk=" + qk+", address=" + address +"]";
	}
//	public String toString() {
//		return "Reservation Staion [tag=" + tag + ", busy=" + busy + ", operation=" + operation
//		+ ", vj=" + vj + ", vk=" + vk +", qj=" + qj+", qk=" + qk+", address=" + address +"]";
//	}
//	 public String toString() {
//	        String format = "| %-10s | %-5s | %-10s | %-5.2f | %-5.2f | %-5s | %-5s | %-7d |%n";
//	        return String.format(format, tag, busy, operation, vj, vk, qj, qk, address);
//	    }
	
	
//	public static String getTableHeaderA() {
//        return String.format("| %-10s | %-5s | %-10s | %-5s | %-5s | %-5s | %-5s | %-7s |%n",
//                "Tag", "Busy", "Operation", "Vj", "Vk", "Qj", "Qk", "Address");
//    }
//
//    public String toTableRowA() {
//        return String.format("| %-10s | %-5s | %-10s | %-5.2f | %-5.2f | %-5s | %-5s | %-7d |%n",
//                tag, busy, operation, vj, vk, qj, qk, address);
//    }
//    
//    
//    public static String getTableHeaderS() {
//        return String.format("| %-10s | %-7s | %-5s |%n","Tag", "Address", "Busy");
//    }
//
//    public String toTableRowS() {
//        return String.format("| %-10s | %-7d | %-5s |%n", tag, address, busy);
//    }
//
//	
//    public static String getTableHeaderL() {
//        return String.format("| %-10s | %-5s | %-7s | %-5s | %-5s |%n", "Tag", "Busy", "Address", "V", "Q");
//    }
//
//    public String toTableRowL() {
//        return String.format("| %-10s | %-5s | %-7d | %-5.2f | %-5s |%n", tag, busy, address, vj, qj);
//    }

	public static String getTableHeaderA() {
	    return String.format("| %-10s | %-5s | %-10s | %-5s | %-5s | %-5s | %-5s | %-7s | %-5s |%n",
	            "Tag", "Busy", "Operation", "Vj", "Vk", "Qj", "Qk", "Address", "Time");
	}

	public String toTableRowA() {
	    return String.format("| %-10s | %-5s | %-10s | %-5.2f | %-5.2f | %-5s | %-5s | %-7d | %-5s |%n",
	            tag, busy, operation, vj, vk, qj, qk, address, time);
	}

	public static String getTableHeaderL() {
	    return String.format("| %-10s | %-7s | %-5s | %-5s |%n", "Tag", "Address", "Busy", "Time");
	}

	public String toTableRowL() {
	    return String.format("| %-10s | %-7d | %-5s | %-5s |%n", tag, address, busy, time);
	}

	public static String getTableHeaderS() {
	    return String.format("| %-10s | %-5s | %-7s | %-5s | %-5s | %-5s |%n", "Tag", "Busy", "Address", "V", "Q", "Time");
	}

	public String toTableRowS() {
	    return String.format("| %-10s | %-5s | %-7d | %-5.2f | %-5s | %-5s | %-5s |%n", tag, busy, address, vj, qj, time, " ");
	}


	
}
