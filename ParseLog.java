package sem2proj;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ParseLog {

    public static void main(String[] args) {
        
        String logFilePath = "extracted_log";

        System.out.printf("%-25s %-30s %-15s %-15s %-15s %-10s%n", 
            "Timestamp", "Action", "Job ID", "InitPrio", "usec", "Exit Status");
          List<String[]> errorCounts = new ArrayList<>(); 
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(logFilePath)))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] logParts = line.split(" ");

                if (logParts.length >= 3) {
                    String timestamp = logParts[0].replace("[", "") + " " + logParts[1].replace("]", "");
                    String action = logParts[2];

                    String jobId = "";
                    String initialPriority = "";
                    String microseconds = "";
                    String exitStatus = "";

                    for (String part : logParts) {
                        if (part.startsWith("JobId=")) {
                            jobId = part.substring("JobId=".length());
                        } else if (part.startsWith("InitPrio=")) {
                            initialPriority = part.substring("InitPrio=".length());
                        } else if (part.startsWith("usec=")) {
                            microseconds = part.substring("usec=".length());
                        } else if (part.startsWith("WEXITSTATUS")) {
                            exitStatus = part.substring("WEXITSTATUS".length());
                        }
                    }

                    System.out.printf("%-25s %-30s %-15s %-15s %-15s %-10s%n", 
                        timestamp, action, jobId, initialPriority, microseconds, exitStatus);
                        if (line.contains("error: This association")) {
                        String user = line.substring(line.indexOf("user=") + "user=".length(), line.indexOf(" "));
                        boolean userExists = false;
                        for (String[] count : errorCounts) {
                            if (count[0].equals(user)) {
                             userExists = true;
                            count[1] = String.valueOf(Integer.parseInt(count[1]) + 1);
                                break;
                     
                     }
            }
                            if (!userExists) {
                                errorCounts.add(new String[]{user, "1"});
                      }
                    }
                }
            }
            System.out.println("\nNumber of jobs causing error by user:");
            for (String[] count : errorCounts) {
                System.out.printf("%-10s: %s%n", count[0], count[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
