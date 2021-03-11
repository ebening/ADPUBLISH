package com.adinfi.formateador.dao;

/**
 *
 * @author Guillermo Trejo
 */
public enum StatementConstant {
    SC_N1(-1),
    SC_0(0),
    SC_1(1),
    SC_2(2),
    SC_3(3),
    SC_4(4),
    SC_5(5),
    SC_6(6),
    SC_7(7),
    SC_8(8),
    SC_9(9),
    SC_10(10),
    SC_11(11),
    SC_12(12),
    SC_13(13),
    SC_14(14),
    SC_15(15),
    SC_16(16),
    SC_17(17),
    SC_18(18),
    SC_19(19),
    SC_20(20),
    SC_21(21),
    SC_22(22),
    SC_23(23),
    SC_24(24),
    SC_25(25),
    SC_26(26), 
    SC_27(27)
    ;
    
    private final int value;
    
    private StatementConstant(int id) {
        this.value = id;
    }
    
    public int get() {
        return value;
    }
}

