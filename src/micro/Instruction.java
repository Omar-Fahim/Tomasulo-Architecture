package micro;

public class Instruction {
	String name;
	String destination;
	String sourceOne;
	String sourceTwo ;
	Double imm = null;
	
	public Instruction() {
		this.name = "";
		destination = "";
		sourceOne ="";
		sourceTwo = "";
	}

	@Override
	public String toString() {
		return "[name=" + name + ", destination=" + destination + ", sourceOne=" + sourceOne
				+ ", sourceTwo=" + sourceTwo + ", imm=" + imm + "]";
	}



	
}
