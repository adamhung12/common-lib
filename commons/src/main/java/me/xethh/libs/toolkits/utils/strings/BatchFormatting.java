package me.xethh.libs.toolkits.utils.strings;

public class BatchFormatting {
    public static String format(String msg, String[] inputs){
        switch (inputs.length){
            case 0: return msg;
            case 1: return String.format(msg, inputs[0]);
            case 2: return String.format(msg, inputs[0], inputs[1]);
            case 3: return String.format(msg, inputs[0], inputs[1], inputs[2]);
            case 4: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3]);
            case 5: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4]);
            case 6: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5]);
            case 7: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5],inputs[6]);
            case 8: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5],inputs[6],inputs[7]);
            case 9: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5],inputs[6],inputs[7],inputs[8]);
            case 10: return String.format(msg, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5],inputs[6],inputs[7],inputs[8],inputs[9]);
            default: throw new RuntimeException(String.format("Fail to format msg[%ms] due to the variable length[%d] not support.",inputs.length));
        }

    }
}
