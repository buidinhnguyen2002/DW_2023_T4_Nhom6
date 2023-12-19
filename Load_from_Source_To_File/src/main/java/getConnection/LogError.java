package getConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogError {
     public static void log(String message) {
          String filePath = "D:\\data_warehouse\\DW_2023_T4_Nhom6\\Load_from_Source_To_File\\src\\main\\java\\log.txt";
          String status = "Failed";
          writeLogToFile(filePath, status, message);
     }
    static void writeLogToFile(String filePath, String status, String errorMessage) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(status + " at " + java.time.LocalDateTime.now() + " with message: " + errorMessage);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
