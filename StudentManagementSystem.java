package josh;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {
    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private JTabbedPane tabbedPane;
    private ArrayList<Student> students; // List to store students
    private JTable studentsTable; // Table to display students

    public StudentManagementSystem() {
        students = new ArrayList<>(); // Initialize the list of students
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Student Management", createStudentManagementPanel());
        tabbedPane.addTab("Course Enrollment", createCourseEnrollmentPanel());
        tabbedPane.addTab("Grade Management", createGradeManagementPanel());

        add(tabbedPane);
    }

    private JPanel createStudentManagementPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create buttons
        JButton addStudentButton = new JButton("Add Student");
        JButton updateStudentButton = new JButton("Update Student");
        JButton deleteStudentButton = new JButton("Delete Student");
        JButton viewStudentButton = new JButton("View Students");

        // Add action listeners using ActionListener
        addStudentButton.addActionListener(e -> showAddStudentForm());
        updateStudentButton.addActionListener(e -> showUpdateStudentForm());
        deleteStudentButton.addActionListener(e -> deleteSelectedStudent());
        viewStudentButton.addActionListener(e -> showStudentList());

        // Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(deleteStudentButton);
        buttonPanel.add(viewStudentButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        // Create a table to display students
        studentsTable = new JTable();
        panel.add(new JScrollPane(studentsTable), BorderLayout.CENTER); // Use JScrollPane to make it scrollable

        return panel;
    }

    private JPanel createCourseEnrollmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton enrollCourseButton = new JButton("Enroll Course");
        enrollCourseButton.addActionListener(e -> enrollCourse());

        panel.add(enrollCourseButton, BorderLayout.SOUTH);
        return panel; // Placeholder for course enrollment implementation
    }

    private JPanel createGradeManagementPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton setGradeButton = new JButton("Set Grade");
        setGradeButton.addActionListener(e -> setGrade());

        panel.add(setGradeButton, BorderLayout.SOUTH);
        return panel; // Placeholder for grade management implementation
    }

    private void showAddStudentForm() {
        JDialog dialog = new JDialog(this, "Add Student", true);
        dialog.setLayout(new GridLayout(0, 2));
        dialog.setSize(300, 200);

        // Fields
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Student ID:"));
        dialog.add(idField);

        // Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            if (!name.isEmpty() && !id.isEmpty()) {
                // Check for duplicate IDs
                if (students.stream().anyMatch(s -> s.getStudentId().equals(id))) {
                    JOptionPane.showMessageDialog(dialog, "Student ID already exists.");
                    return;
                }
                students.add(new Student(name, id)); // Add student to the list
                JOptionPane.showMessageDialog(dialog, "Student added successfully."); // Success message
                showStudentList(); // Refresh the student list
                dialog.dispose(); // Close dialog
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields."); // Input validation
            }
        });

        dialog.add(addButton);

        dialog.setVisible(true);
    }

    private void showUpdateStudentForm() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            Student selectedStudent = students.get(selectedRow);
            JTextField nameField = new JTextField(selectedStudent.getName());
            JTextField idField = new JTextField(selectedStudent.getStudentId());

            Object[] message = {
                "Name:", nameField,
                "Student ID:", idField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String updatedName = nameField.getText();
                String updatedId = idField.getText();

                if (!updatedName.isEmpty() && !updatedId.isEmpty()) {
                    // Check for duplicate ID (excluding current student)
                    boolean idExists = students.stream()
                            .anyMatch(s -> !s.equals(selectedStudent) && s.getStudentId().equals(updatedId));

                    if (idExists) {
                        JOptionPane.showMessageDialog(this, "Another student already has this ID.");
                    } else {
                        selectedStudent.setName(updatedName);
                        selectedStudent.setStudentId(updatedId);
                        JOptionPane.showMessageDialog(this, "Student updated successfully."); // Success message
                        showStudentList(); // Refresh the table
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
        }
    }
    private void deleteSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                students.remove(selectedRow);
                JOptionPane.showMessageDialog(this, "Student deleted successfully."); // Success message
                showStudentList(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    private void showStudentList() {
        // Create a table model and set it to the JTable
        String[] columnNames = {"Name", "Student ID", "Enrolled Courses", "Grades"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add students to the model
        for (Student student : students) {
            StringBuilder courses = new StringBuilder();
            StringBuilder grades = new StringBuilder();

            // Building courses and grades as a comma-separated string
            for (String course : student.getEnrolledCourses()) {
                courses.append(course).append(", ");
            }

            for (Double grade : student.getGrades()) {
                grades.append(grade != null ? grade : "N/A").append(", ");
            }

            // Remove the last comma and space if there's any
            if (courses.length() > 0) {
                courses.setLength(courses.length() - 2);
            }
            if (grades.length() > 0) {
                grades.setLength(grades.length() - 2);
            }

            model.addRow(new Object[]{student.getName(), student.getStudentId(), courses.toString(), grades.toString()});
        }

        studentsTable.setModel(model); // Set the model to the table
    }

    private void enrollCourse() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            Student selectedStudent = students.get(selectedRow);
            String course = JOptionPane.showInputDialog(this, "Enter Course Name:");
            if (course != null && !course.isEmpty()) {
                selectedStudent.enrollCourse(course);
                JOptionPane.showMessageDialog(this, "Student enrolled in " + course + " successfully."); // Success message
                showStudentList(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to enroll in a course.");
        }
    }

    private void setGrade() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            Student selectedStudent = students.get(selectedRow);
            String course = (String) JOptionPane.showInputDialog(this,
                "Select Course",
                "Set Grade",
                JOptionPane.PLAIN_MESSAGE,
                null,
                selectedStudent.getEnrolledCourses().toArray(),
                selectedStudent.getEnrolledCourses().get(0));

            if (course != null) {
                try {
                    double grade = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Grade (0.0 - 100.0):"));
                    selectedStudent.setGrade(selectedStudent.getEnrolledCourses().indexOf(course), grade);
                    JOptionPane.showMessageDialog(this, "Grade set for " + course + " successfully."); // Success message
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid grade input. Please enter a number between 0.0 and 100.0.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to set a grade.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}