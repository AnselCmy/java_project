package CS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SplittableRandom;

public class Protocol {
    static public String parse(String msg) {
        if (msg.contains("(REQUEST)")) {
            return "(REQUEST)";
        }
        if (msg.contains("(RESPONSE")) {
            return msg.substring("(RESPONSE)".length(), msg.length());
        }
        else {
            HashMap<String, String> rst = new HashMap<>();
            int timeIdx = msg.indexOf("(TIME)");
            int textIdx = msg.indexOf("(TEXT)");
            rst.put("TIME", msg.substring(timeIdx + 6, textIdx));
            rst.put("TEXT", msg.substring(textIdx + 6, msg.length()));
            return rst.get("TIME") + rst.get("TEXT");
        }
    }

    static public String wrapText(String text) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        return "(TIME)" + time + "\n" + "(TEXT)" + text + "\n" + "(END)";
    }

    static public String wrapReponse(String response) {
        return "(RESPONSE)" + response + "\n" + "(END)";
    }

    public static void writeToSocket(String msg, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromSocket(BufferedReader bufferedReader) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String tempMsg;
            String parsedMsg;
            // get all msg by looped readline
            while (!(tempMsg = bufferedReader.readLine()).equals("(END)")) {
                stringBuffer.append(tempMsg + "\n");
            }
            // using protocol to parse
            parsedMsg = Protocol.parse(stringBuffer.toString());
            System.out.println("readFromSocket: " + parsedMsg);
            return parsedMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
