public class Instruction {

    private static final InstructionType[] types =
            {InstructionType.I,
             InstructionType.R,
             InstructionType.I,
             InstructionType.R,
             InstructionType.I,
             InstructionType.I,
             InstructionType.I,
             InstructionType.I,
             InstructionType.R,
             InstructionType.R,
             InstructionType.R,
             InstructionType.I,
             InstructionType.I,
             InstructionType.I,
             InstructionType.J};
    private final int OP_SHIFT = 12;
    private final int RS_SHIFT = 9;
    private final int RT_SHIFT = 6;
    private final int RD_SHIFT = 3;

    private int opCode, rs, rt, rd, func, immediate, targetAddress;
    private InstructionType instructionType;

    public Instruction() {}

    public Instruction(int instruction) throws Exception {
        this.opCode = instruction >>> OP_SHIFT;
        if(this.opCode > 14) {
            throw new Exception("Unknown operation code: " + this.opCode);
        }
        this.instructionType = types[opCode];

        switch(this.instructionType) {
            case R:
                this.rs = (instruction >>> RS_SHIFT) & 0x7;
                this.rt = (instruction >>> RT_SHIFT) & 0x7;
                this.rd = (instruction >>> RD_SHIFT) & 0x7;
                this.func = instruction & 0x7;
                break;
            case I:
                this.rs = (instruction >>> RS_SHIFT) & 0x7;
                this.rt = (instruction >>> RT_SHIFT) & 0x7;
                this.immediate = instruction & 0x3F;
                if((this.immediate & 0x20) > 0) {
                    this.immediate ^= 0x3F;
                    this.immediate += 1;
                    this.immediate *= -1;
                }
                System.out.println("Found immediate: " + this.immediate);
                break;
            case J:
                targetAddress = instruction & 0xFFF;
                break;
        }
    }

    public int getOpCode() {
        return opCode;
    }

    public int getRs() throws Exception {
        if(this.instructionType == InstructionType.J) {
            throw new Exception("Instruction does not contain source register");
        }
        return rs;
    }

    public int getRt() throws Exception {
        if(this.instructionType == InstructionType.J) {
            throw new Exception("Instruction does not contain target register");
        }
        return rt;
    }

    public int getRd() throws Exception {
        if(this.instructionType != InstructionType.R) {
            throw new Exception("Instruction does not contain destination register");
        }
        return rd;
    }

    public int getFunc() throws Exception {
        if(this.instructionType != InstructionType.R) {
            throw new Exception("Instruction does not contain a function value");
        }
        return func;
    }

    public int getImmediate() throws Exception {
        if(this.instructionType != InstructionType.I) {
            throw new Exception("Instruction does not contain an immediate");
        }
        return immediate;
    }

    public int getTargetAddress() throws Exception {
        if(this.instructionType != InstructionType.J) {
            throw new Exception("Instruction does not contain target address");
        }
        return targetAddress;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    private String names[] = {"addi", "add", "subi", "sub", "sll", "srl", "lw", "sw", "and", "or", "xor", "bgt", "bne", "beq", "j"};

    public String getInstructionName() {
        return names[this.opCode];
    }
}
