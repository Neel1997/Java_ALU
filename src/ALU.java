public class ALU {

	private static int result;
	
	public static int execute(ALUOpType opcode, int a, int b) throws Exception {
		System.out.print("ALU: ");
		switch(opcode) {
			case ADD:
				System.out.println(a + " + " + b);
				result =  a + b;
				break;
			case SUBTRACT:
				System.out.println(a + " - " + b);
				result =  a - b;
				break;
			case AND:
				System.out.println(a + " & " + b);
				result =  a & b;
				break;
			case OR:
				System.out.println(a + " | " + b);
				result =  a | b;
				break;
			case XOR:
				System.out.println(a + " ^ " + b);
				result = a ^ b;
				break;
			case SLL:
			case SLA:
				System.out.println(a + " << " + b);
				if(b < 0) {
					throw new Exception("Cannot shift by negative value");
				}
				result =  a << b;
				break;
			case SRL:
				System.out.println(a + " >>> " + b);
				if(b < 0) {
					throw new Exception("Cannot shift by negative value");
				}
				result =  a >>> b;
				break;
			case SRA:
				System.out.println(a + " >> " + b);
				if(b < 0) {
					throw new Exception("Cannot shift by negative value");
				}
				result =  a >> b;
				break;
			default:
				result =  0;
		}
		return result;
	}

	public static int getResult() {
		return result;
	}

	public static boolean getZero() {
		return result == 0;
	}
}
