import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class SchedulerGUI {
    private JFrame frame;
    private JTextField nameField, arrivalField, burstField, priorityField;
    private JTextArea outputArea;
    private GanttChartPanel chartPanel;

    public SchedulerGUI() {
        frame = new JFrame("CPU Scheduling Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Job Input Panel
        JPanel jobPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        jobPanel.setBorder(BorderFactory.createTitledBorder("Job Info"));

        jobPanel.add(new JLabel("Job Name:"));
        nameField = new JTextField();
        jobPanel.add(nameField);

        jobPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        jobPanel.add(arrivalField);

        jobPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        jobPanel.add(burstField);

        jobPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField("0");
        jobPanel.add(priorityField);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        JButton addButton = new JButton("Add Job");
        controlPanel.add(addButton);

        JButton scheduleButton = new JButton("Schedule");
        controlPanel.add(scheduleButton);

        JButton clearButton = new JButton("Clear");
        controlPanel.add(clearButton);

        JButton loadButton = new JButton("Load Output Logs");
        controlPanel.add(loadButton);

        // Wrap Panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(jobPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        chartPanel = new GanttChartPanel();

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(chartPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int arrival = Integer.parseInt(arrivalField.getText().trim());
                int burst = Integer.parseInt(burstField.getText().trim());
                int priority = Integer.parseInt(priorityField.getText().trim());
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Job name cannot be empty.");
                    return;
                }
                JobStorage.addJob(new Job(name, arrival, burst, priority));
                outputArea.append("✅ Job Added:\n");
                outputArea.append("• Name: " + name + "\n");
                outputArea.append("• Arrival Time: " + arrival + "\n");
                outputArea.append("• Burst Time: " + burst + "\n");
                outputArea.append("• Priority: " + priority + "\n\n");

                nameField.setText("");
                arrivalField.setText("");
                burstField.setText("");
                priorityField.setText("0");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers for times and priority.");
            }
        });

        scheduleButton.addActionListener(e -> {
            List<Job> jobs = JobStorage.getJobs();
            if (jobs.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No jobs to schedule. Add jobs first.");
                return;
            }

            // Prompt user to select algorithm
            String[] algorithms = {"FCFS", "SJF", "Round Robin", "Priority"};
            String algo = (String) JOptionPane.showInputDialog(
                frame,
                "Select Scheduling Algorithm:",
                "Algorithm Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                algorithms,
                algorithms[0]
            );

            if (algo == null) return; // user cancelled

            int quantum = 2;
            if ("Round Robin".equals(algo)) {
                String input = JOptionPane.showInputDialog(frame, "Enter Time Quantum:", "2");
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Time Quantum required for Round Robin. Using default = 2");
                } else {
                    try {
                        quantum = Integer.parseInt(input.trim());
                        if (quantum <= 0) {
                            JOptionPane.showMessageDialog(frame, "Time Quantum must be positive. Using default = 2");
                            quantum = 2;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid Time Quantum. Using default = 2");
                        quantum = 2;
                    }
                }
            }

            List<GanttSegment> segments = Scheduler.schedule(jobs, algo, quantum);
            chartPanel.setSegments(segments);

            outputArea.append("\n--- Scheduling Run ---\n");
            outputArea.append("Algorithm: " + algo + (algo.equals("Round Robin") ? (" (Quantum=" + quantum + ")") : "") + "\n");
            outputArea.append("Execution Order:\n");
            for (GanttSegment segment : segments) {
                outputArea.append(segment.getJobName() + " [" + segment.getStartTime() + " - " + segment.getEndTime() + "]\n");
            }
            outputArea.append("----------------------\n");

            appendOutputToFile("output.txt", algo, quantum, segments);
            appendJobsToFile("jobs.txt", jobs);
        });

        clearButton.addActionListener(e -> {
            JobStorage.clear();
            outputArea.setText("");
            chartPanel.setSegments(null);
        });

        loadButton.addActionListener(e -> {
            outputArea.setText("");
            try (BufferedReader reader = new BufferedReader(new FileReader("output.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading output.txt");
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void appendJobsToFile(String filename, List<Job> jobs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {  // append mode
            writer.write("=== Job List Entry ===\n");
            for (Job job : jobs) {
                writer.write("JobName: " + job.getName());
                writer.newLine();
                writer.write("ArrivalTime: " + job.getArrivalTime());
                writer.newLine();
                writer.write("BurstTime: " + job.getBurstTime());
                writer.newLine();
                writer.write("Priority: " + job.getPriority());
                writer.newLine();
                writer.newLine();
            }
            writer.write("=== End of Job List ===\n\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to write jobs file.");
        }
    }

    private void appendOutputToFile(String filename, String algo, int quantum, List<GanttSegment> segments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) { // append mode
            writer.write("=== Scheduling Run ===\n");
            writer.write("Algorithm: " + algo);
            if (algo.equals("Round Robin")) {
                writer.write(" (Quantum=" + quantum + ")");
            }
            writer.newLine();

            for (GanttSegment segment : segments) {
                writer.write(segment.getJobName() + " [" + segment.getStartTime() + " - " + segment.getEndTime() + "]");
                writer.newLine();
            }
            writer.write("=== End of Run ===\n\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to write output file.");
        }
    }
}
