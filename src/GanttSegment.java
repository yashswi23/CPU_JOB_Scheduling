public class GanttSegment {
    private String jobName;
    private int startTime;
    private int endTime;

    public GanttSegment(String jobName, int startTime, int endTime) {
        this.jobName = jobName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getJobName() {
        return jobName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}
