import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GanttChartPanel extends JPanel {
    private List<GanttSegment> segments;

    public void setSegments(List<GanttSegment> segments) {
        this.segments = segments;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (segments == null || segments.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        int x = 10, y = 20, height = 40;
        int unitWidth = 30;

        for (GanttSegment segment : segments) {
            int width = (segment.getEndTime() - segment.getStartTime()) * unitWidth;
            g2.setColor(Color.CYAN);
            g2.fillRect(x, y, width, height);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, width, height);
            g2.drawString(segment.getJobName(), x + width / 2 - 10, y + 25);
            g2.drawString(String.valueOf(segment.getStartTime()), x - 2, y + 55);
            x += width;
        }
        g2.drawString(String.valueOf(segments.get(segments.size() - 1).getEndTime()), x - 2, y + 55);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 100);
    }
}
