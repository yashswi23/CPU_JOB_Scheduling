import java.util.*;

public class Scheduler {

    public static List<GanttSegment> schedule(List<Job> jobs, String algorithm, int quantum) {
        switch (algorithm) {
            case "FCFS":
                return fcfs(jobs);
            case "SJF":
                return sjf(jobs);
            case "Round Robin":
                return roundRobin(jobs, quantum);
            case "Priority":
                return priorityScheduling(jobs);
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    private static List<GanttSegment> fcfs(List<Job> jobs) {
        List<GanttSegment> segments = new ArrayList<>();
        jobs.sort(Comparator.comparingInt(Job::getArrivalTime));
        int currentTime = 0;
        for (Job job : jobs) {
            int start = Math.max(currentTime, job.getArrivalTime());
            int end = start + job.getBurstTime();
            segments.add(new GanttSegment(job.getName(), start, end));
            currentTime = end;
        }
        return segments;
    }

    private static List<GanttSegment> sjf(List<Job> jobs) {
        List<GanttSegment> segments = new ArrayList<>();
        List<Job> jobList = new ArrayList<>(jobs);
        jobList.sort(Comparator.comparingInt(Job::getArrivalTime));
        int currentTime = 0;
        List<Job> readyQueue = new ArrayList<>();

        while (!jobList.isEmpty() || !readyQueue.isEmpty()) {
            for (Iterator<Job> it = jobList.iterator(); it.hasNext();) {
                Job job = it.next();
                if (job.getArrivalTime() <= currentTime) {
                    readyQueue.add(job);
                    it.remove();
                }
            }
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            readyQueue.sort(Comparator.comparingInt(Job::getBurstTime));
            Job job = readyQueue.remove(0);
            int start = currentTime;
            int end = start + job.getBurstTime();
            segments.add(new GanttSegment(job.getName(), start, end));
            currentTime = end;
        }
        return segments;
    }

    private static List<GanttSegment> roundRobin(List<Job> jobs, int quantum) {
        List<GanttSegment> segments = new ArrayList<>();
        Queue<Job> queue = new LinkedList<>();
        List<Job> jobList = new ArrayList<>(jobs);
        jobList.sort(Comparator.comparingInt(Job::getArrivalTime));
        int currentTime = 0;

        int index = 0;
        while (index < jobList.size() || !queue.isEmpty()) {
            while (index < jobList.size() && jobList.get(index).getArrivalTime() <= currentTime) {
                queue.add(jobList.get(index));
                index++;
            }
            if (queue.isEmpty()) {
                currentTime = (index < jobList.size()) ? jobList.get(index).getArrivalTime() : currentTime;
                continue;
            }
            Job job = queue.poll();
            int execTime = Math.min(quantum, job.getRemainingTime());
            int start = currentTime;
            int end = start + execTime;
            segments.add(new GanttSegment(job.getName(), start, end));
            currentTime = end;
            job.setRemainingTime(job.getRemainingTime() - execTime);

            // Add newly arrived jobs while current job runs
            while (index < jobList.size() && jobList.get(index).getArrivalTime() <= currentTime) {
                queue.add(jobList.get(index));
                index++;
            }

            if (job.getRemainingTime() > 0) {
                queue.add(job);
            }
        }
        return segments;
    }

    private static List<GanttSegment> priorityScheduling(List<Job> jobs) {
        List<GanttSegment> segments = new ArrayList<>();
        List<Job> jobList = new ArrayList<>(jobs);
        jobList.sort(Comparator.comparingInt(Job::getArrivalTime));
        int currentTime = 0;
        List<Job> readyQueue = new ArrayList<>();

        while (!jobList.isEmpty() || !readyQueue.isEmpty()) {
            for (Iterator<Job> it = jobList.iterator(); it.hasNext();) {
                Job job = it.next();
                if (job.getArrivalTime() <= currentTime) {
                    readyQueue.add(job);
                    it.remove();
                }
            }
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            readyQueue.sort(Comparator.comparingInt(Job::getPriority));
            Job job = readyQueue.remove(0);
            int start = currentTime;
            int end = start + job.getBurstTime();
            segments.add(new GanttSegment(job.getName(), start, end));
            currentTime = end;
        }
        return segments;
    }
}
