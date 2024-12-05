import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

// Class representing a Student
class Student {
    private String id;
    private String name;
    private int age;
    private String course;

    public Student(String id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + course;
    }

    public static Student fromString(String record) {
        String[] parts = record.split(",");
        return new Student(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
    }
}

// Main Class for Student Management System
public class AdvancedStudentManagementSystem {
    private static final String DATABASE_URL = "jdbc:sqlite:students.db";

    public static void main(String[] args) {
        // Load SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC"); // This loads the SQLite JDBC driver
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "SQLite JDBC driver not found. Please ensure the driver is added to the classpath.");
            return;
        }

        initializeDatabase();
        SwingUtilities.invokeLater(AdvancedMainFrame::new);
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS students (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    age INTEGER,
                    course TEXT
                )
            """;
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error initializing database: " + e.getMessage());
        }
    }
}

// Main Frame with Tabs
class AdvancedMainFrame extends JFrame {
    public AdvancedMainFrame() {
        setTitle("Advanced Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Add Student", new AddStudentPanel());
        tabbedPane.add("Update Student", new UpdateStudentPanel());
        tabbedPane.add("Remove Student", new RemoveStudentPanel());
        tabbedPane.add("View Students", new ViewStudentPanel());
        tabbedPane.add("Export/Import", new FileOperationPanel());

        add(tabbedPane);
        setVisible(true);
    }
}

// Panel to Add Students
class AddStudentPanel extends JPanel {
    public AddStudentPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Student ID:");
        JTextField idField = new JTextField();

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel courseLabel = new JLabel("Course:");
        JTextField courseField = new JTextField();

        JButton addButton = new JButton("Add Student");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(idLabel, gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(ageLabel, gbc);
        gbc.gridx = 1;
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(courseLabel, gbc);
        gbc.gridx = 1;
        add(courseField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(addButton, gbc);

        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String course = courseField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || ageText.isEmpty() || course.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid age input.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(AdvancedStudentManagementSystem.DATABASE_URL);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students VALUES (?, ?, ?, ?)")) {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                pstmt.setInt(3, age);
                pstmt.setString(4, course);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student added successfully.");
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                courseField.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage());
            }
        });
    }
}
