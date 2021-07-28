import java.util.Arrays;

public class Processor {

    private final int MEM_SIZE = 512;
    private final int NUM_REGISTERS = 8;

    private ControlUnit controlUnit;
    private Instruction instruction;
    private int PC, clockCycle;
    private int[] instructionMem = new int[MEM_SIZE];
    private int[] mem = new int[MEM_SIZE];
    private int[] regArray = new int[NUM_REGISTERS];
    
    public Processor() {
        this.controlUnit = new ControlUnit();
        this.instruction = new Instruction();
    }
    
    public void run(int [] instructions, int numInstructions) {
        this.instructionMem = instructions;
        this.PC = 0;
        this.clockCycle = 1;
        boolean clk = true;
        
        regArray[0] = 0x0005;
        regArray[2] = 0x0010;
        regArray[3] = 0x0040;
        regArray[4] = 0x1010;
        regArray[5] = 0x000F;
        regArray[6] = 0x00F0;
        
        mem[0] = 0x0101;
        mem[1] = 0x0110;
        mem[2] = 0x0011;
        mem[3] = 0x00F0;
        mem[4] = 0x00FF;
        mem[5] = 0x0000;
        
        while(true) {
        	if (clk) {
        	    fetch(this.PC);
        	    System.out.println("Clock cycle: " + this.clockCycle);
        	    printProcessorState();
        	    clk = false;
        	} else {
        	    clk = true;
                this.clockCycle++;
            }
        	// condition for last instruction
            if(PC == numInstructions) {
                System.out.println("Completed all " + numInstructions +  " instructions");
                return;
            }
            if(PC > numInstructions) {
                System.err.println("ERROR:: Out-of-bounds - instruction memory");
                return;
            }
        }
    }

    public void printProcessorState() {
        System.out.println("PC:: " + this.PC);
        printRegisterState();
        printMemoryState();
        System.out.println();
    }

    public void printRegisterState() {
        System.out.println("Registers:: " + Arrays.toString(this.regArray));
    }

    public void printMemoryState() {
        System.out.println("Memory:: " + Arrays.toString(this.mem));
    }

    /**
     * Fetches instruction from memory and passes to decode
     * @param memLoc memory location of instruction
     */
    private void fetch(final int memLoc) {
        try {
            System.out.println("Executing instruction: " + memLoc);
            this.instruction = new Instruction(this.instructionMem[memLoc]);
            this.controlUnit = ControlUnit.getControlUnit(this.instruction.getOpCode());
            System.out.println("Instruction: "
                                + this.instruction.getInstructionName()
                                + " OpCode: "
                                + instruction.getOpCode()
                                + " Type: "
                                + instruction.getInstructionType());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        // if J type, forward to PC increment
        if(this.controlUnit.isJump()) {
            try {
                this.PC = this.instruction.getTargetAddress();
                return;
            } catch (Exception e) {
                System.err.println(e.getMessage() + "incorrect id of jump");
                e.printStackTrace();
            }
        } else {
            if(!this.controlUnit.isBranch()) { // did not jump or branch
                this.PC++;
            }
        }
        decode();
    }

    /**
     * Decodes instruction and sets up control unit for current clock cycle
     */
    private void decode() {
        try {
            final int a = regArray[instruction.getRs()];
            final int b = regArray[instruction.getRt()];
            execute(a, b);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executes ALU operation
     */
    private void execute(final int a,
                         final int b) {
        final int writeData = b;
        if(controlUnit.isALUSrc()) {
            try {
                ALU.execute(controlUnit.getALUOp(), a, instruction.getImmediate());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                ALU.execute(controlUnit.getALUOp(), a, b);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        // branching
        if(controlUnit.isBranch()) {
            try {
                switch (instruction.getOpCode()) {
                    case 11: //bgt
                        if (ALU.getResult() > 0) {
                            this.PC += this.instruction.getImmediate();
                            System.out.println("branch: " + ALU.getResult());
                        }
                        else {
                        	PC++;
                        }
                        return;
                    case 12: //bne
                        if (ALU.getResult() != 0) {
                            this.PC += this.instruction.getImmediate();
                        }
                        else {
                        	PC++;
                        }
                        return;
                    case 13: //beq
                        if (ALU.getResult() == 0) {
                            this.PC += this.instruction.getImmediate();
                        }
                        else {
                        	PC++;
                        }
                        return;
                    default:
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        // forward result of ALU to registers
        if(!controlUnit.isMemToReg()) {
            writeBack(ALU.getResult());
        }
        memAccess(writeData);
    }

    /**
     * Accesses memory
     */
    private void memAccess(final int writeData) {
        if(controlUnit.isMemWrite()) {
            mem[ALU.getResult()] = writeData;
        } else {
            int writeBackData;
            if(controlUnit.isMemToReg()) {
                writeBackData = mem[ALU.getResult()];
            } else {
                writeBackData = ALU.getResult();
            }
            writeBack(writeBackData);
        }
    }

    /**
     * Writes back to register file
     */
    private void writeBack(final int writeData) {
        if(!controlUnit.isRegWrite()) {
            return;
        }
        try {
            if (controlUnit.isRegDst()) {
                regArray[instruction.getRd()] = writeData;
                System.out.println("Rd: " + instruction.getRd() + "<--" + writeData );
            } else {
                regArray[instruction.getRt()] = writeData;
                System.out.println("Rt: " + instruction.getRt() + "<--" + writeData );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
