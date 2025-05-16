# 🖥️ CPU Job Scheduling System

This Java Swing-based GUI application simulates a **CPU Job Scheduling System**. It allows users to visualize and compare how different CPU scheduling algorithms work, complete with Gantt Chart support and performance metrics.

## 🚀 Features

- ✅ Interactive GUI to enter job details
- ✅ Supports major CPU scheduling algorithms:
  - First-Come, First-Served (FCFS)
  - Shortest Job First (SJF)
  - Priority Scheduling
  - Round Robin
- ✅ Gantt Chart visualization for job execution
- ✅ Export results to `output.txt`
- ✅ Real-time calculation of:
  - Completion Time
  - Turnaround Time
  - Waiting Time

---

## 📂 Project Structure

### `Scheduler.java`
Contains logic for all scheduling algorithms. Implements methods to compute scheduling results based on the selected algorithm.

### `SchedulerGUI.java`
The main user interface:
- Input job details: ID, Arrival Time, Burst Time, Priority
- Choose algorithm and time quantum (for Round Robin)
- View results in table form
- Export results to file
- Display Gantt Chart

### `GanttChartPanel.java`
Custom Swing panel that renders a dynamic Gantt Chart showing how jobs are scheduled over time.

### `JobStorage.java`
Handles storage and retrieval of job data entered through the GUI.

---

## 🛠️ Technologies Used

- **Language**: Java
- **Framework**: Java Swing (GUI)
- **Paradigm**: Object-Oriented Programming (OOP)

---

## 📸 Screenshots

> _Add screenshots here of the GUI and Gantt Chart view for better visualization._

---

## 📁 Output Format

Results are displayed in a tabular format, showing:
- Job ID
- Arrival Time
- Burst Time
- Completion Time
- Turnaround Time
- Waiting Time

The Gantt Chart provides a visual representation of the scheduling.

You can also export the results to a file named `output.txt`.

---

## 🧪 How to Run

1. Compile all Java files:
   ```bash
   javac *.java
