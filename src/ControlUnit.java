public class ControlUnit {

    private final boolean regDst, jump, branch, memWrite, memToReg, ALUSrc, regWrite;
    private final ALUOpType ALUOp;

    private static final ControlUnit[] staticControls =
            {new ControlUnit(false, false, false, false, false, ALUOpType.ADD, true, true), //addi
             new ControlUnit(true, false, false, false, false, ALUOpType.ADD, false, true), //add
             new ControlUnit(false, false, false, false, false, ALUOpType.SUBTRACT, true, true), //subi
             new ControlUnit(true, false, false, false, false, ALUOpType.SUBTRACT, false, true), //sub
             new ControlUnit(false, false, false, false, false, ALUOpType.SLL, true, true), //sll
             new ControlUnit(false, false, false, false, false, ALUOpType.SRL, true, true), //srl
             new ControlUnit(false, false, false, false, true, ALUOpType.ADD, true, true), //lw
             new ControlUnit(false, false, false, true, false, ALUOpType.ADD, true, false), //sw
             new ControlUnit(true, false, false, false, false, ALUOpType.AND, false, true), //and
             new ControlUnit(true, false, false, false, false, ALUOpType.OR, false, true), //or
             new ControlUnit(true, false, false, false, false, ALUOpType.XOR, false, true), //xor
             new ControlUnit(false, false, true, false, false, ALUOpType.SUBTRACT, false, false), //bgt
             new ControlUnit(false, false, true, false, false, ALUOpType.SUBTRACT, false, false), //bne
             new ControlUnit(false, false, true, false, false, ALUOpType.SUBTRACT, false, false), //beq
             new ControlUnit(false, true, false, false, false, ALUOpType.ADD, false, false)}; //j

    public ControlUnit() {
        regDst = jump = branch = memWrite = memToReg = ALUSrc = regWrite = false;
        ALUOp = ALUOpType.ADD;
    }
    
    private ControlUnit(final boolean regDst,
                        final boolean jump,
                        final boolean branch,
                        final boolean memWrite,
                        final boolean memToReg,
                        final ALUOpType ALUOp,
                        final boolean ALUSrc,
                        final boolean regWrite) {
        this.regDst = regDst;
        this.jump = jump;
        this.branch = branch;
        this.memWrite = memWrite;
        this.memToReg = memToReg;
        this.ALUOp =  ALUOp;
        this.ALUSrc = ALUSrc;
        this.regWrite = regWrite;
    }

    public static ControlUnit getControlUnit(final int opCode) throws Exception{
        if(opCode > 14) {
            throw new Exception("Unknown operation");
        }
        return staticControls[opCode];
    }

    public boolean isRegDst() {
        return regDst;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isBranch() {
        return branch;
    }

    public boolean isMemWrite() {
        return memWrite;
    }

    public boolean isMemToReg() {
        return memToReg;
    }

    public ALUOpType getALUOp() {
        return ALUOp;
    }

    public boolean isALUSrc() {
        return ALUSrc;
    }

    public boolean isRegWrite() {
        return regWrite;
    }
}
