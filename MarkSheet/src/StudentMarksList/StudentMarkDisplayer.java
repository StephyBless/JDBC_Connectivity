package StudentMarksList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentMarkDisplayer {
    private JFrame frame;
    private JTextField regIdField, semesterField;
    private JComboBox<String> subjectDropdown;
    private JTextField marksField;
    private JTable resultArea;
    private DefaultTableModel tableModel;
    private JLabel percentageLabel;
    private JLabel resultLabel;
    private Connection conn;
    
    public StudentMarkDisplayer() {
        frame = new JFrame("Student Mark Displayer");
        frame.setLayout(new FlowLayout());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel regLabel = new JLabel("Registration ID:");
        regIdField = new JTextField(10);
        
        JLabel semLabel = new JLabel("Semester:");
        semesterField = new JTextField(2);
        
        JLabel subLabel = new JLabel("Subject:");
        subjectDropdown = new JComboBox<>();
        
        JLabel markLabel = new JLabel("Marks:");
        marksField = new JTextField(3);
        
        JButton fetchSubjectsBtn = new JButton("Load Subjects");
        JButton submitBtn = new JButton("Submit Marks");
        JButton showBtn = new JButton("Show Marks");
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Subject");
        tableModel.addColumn("Marks");
        resultArea = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(resultArea);
        
        percentageLabel = new JLabel("Total Percentage: ");
        
        resultLabel = new JLabel("Pass: ", SwingConstants.CENTER);
        resultLabel.setOpaque(true);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setPreferredSize(new Dimension(200, 30));
        
        frame.add(regLabel);
        frame.add(regIdField);
        frame.add(semLabel);
        frame.add(semesterField);
        frame.add(fetchSubjectsBtn);
        frame.add(subLabel);
        frame.add(subjectDropdown);
        frame.add(markLabel);
        frame.add(marksField);
        frame.add(submitBtn);
        frame.add(showBtn);
        frame.add(tableScroll);
        frame.add(percentageLabel);
        frame.add(resultLabel);
        
        fetchSubjectsBtn.addActionListener(e -> loadSubjects());
        submitBtn.addActionListener(e -> insertMarks());
        submitBtn.setBackground(new Color(70, 130, 180)); // Steel Blue
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
 // Change text color
        
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitBtn.setBackground(Color.GREEN); // Hover color
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
            	submitBtn.setBackground(new Color(70, 130, 180)); // Normal color
            }
        });


        showBtn.addActionListener(e -> displayMarks());
        
        connectToDatabase();
        frame.setVisible(true);
    }

    
    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_marks_db", "root", "godgrace");
            JOptionPane.showMessageDialog(frame, "Connection to DB Successful");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Connection Failed: " + e.getMessage());
        }
    }
    
    private void loadSubjects() {
        subjectDropdown.removeAllItems();
        String semester = semesterField.getText();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT subject_name FROM mark WHERE semester = ?");
            ps.setInt(1, Integer.parseInt(semester));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subjectDropdown.addItem(rs.getString("subject_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error Fetching Subjects: " + e.getMessage());
        }
    }
    
    private void insertMarks() {
        String regId = regIdField.getText();
        String semester = semesterField.getText();
        String subject = (String) subjectDropdown.getSelectedItem();
        String marks = marksField.getText();
        
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO mark (reg_id, semester, subject_name, marks) VALUES (?, ?, ?, ?)");
            ps.setString(1, regId);
            ps.setInt(2, Integer.parseInt(semester));
            ps.setString(3, subject);
            ps.setInt(4, Integer.parseInt(marks));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Marks Submitted Successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error Submitting Marks: " + e.getMessage());
        }
    }
    
    private void displayMarks() {
    	tableModel.setRowCount(0);
        int totalMarks = 0, count = 0;
        String regId = regIdField.getText();
        String semester = semesterField.getText();
        
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT subject_name, marks FROM mark WHERE reg_id = ? AND semester = ?");
            ps.setString(1, regId);
            ps.setInt(2, Integer.parseInt(semester));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	int marks = rs.getInt("marks");
                totalMarks += marks;
                count++;
                tableModel.addRow(new Object[]{rs.getString("subject_name"), marks});
            }
            if (count > 0) {
                double percentage = (double) totalMarks / count;
                String status = percentage >= 35 ? "Pass" : "Fail";
               
                percentageLabel.setText("Total Percentage: " + String.format("%.2f", percentage) + "% (" + status + ")");
                resultLabel.setBackground(percentage >= 35 ? Color.GREEN : Color.RED);
            } else {
                percentageLabel.setText("No Marks Available");
                resultLabel.setBackground(Color.LIGHT_GRAY);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error Fetching Marks: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new StudentMarkDisplayer();
    }
}
