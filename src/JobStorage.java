import java.io.*;
import java.util.*;

public class JobStorage {
    private static final List<Job> jobs = new ArrayList<>();

    public static void addJob(Job job) {
        jobs.add(job);
    }
    public static List<Job> getJobs() {
        return Collections.unmodifiableList(jobs);
    }
    public static void clear() {
        jobs.clear();
    }
    public static void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Job job : jobs) {
                writer.println(job.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadFromFile(String filename) {
        clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0];
                    int arrival = Integer.parseInt(parts[1]);
                    int burst = Integer.parseInt(parts[2]);
                    int priority = Integer.parseInt(parts[3]);
                    addJob(new Job(name, arrival, burst, priority));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
