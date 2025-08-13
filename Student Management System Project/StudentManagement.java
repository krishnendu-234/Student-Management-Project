import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagement extends JFrame implements ActionListener {

    private JTextField nameField, rollField, gradeField, classField, genderField, emailField, phoneField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    JComboBox<String> sortCriteria;
    JRadioButton ascending, descending;
    private JTextField searchRollField;
    private JButton searchButton, deleteButton;

    public StudentManagement() {
        setLayout(null);
        setSize(900, 600);
        setTitle("Student Management");
        setLocation(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 25);
        add(nameField);

        JLabel rollLabel = new JLabel("Roll Number:");
        rollLabel.setBounds(20, 60, 100, 25);
        add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(120, 60, 150, 25);
        add(rollField);

        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setBounds(20, 100, 100, 25);
        add(gradeLabel);

        gradeField = new JTextField();
        gradeField.setBounds(120, 100, 150, 25);
        add(gradeField);

        JLabel classLabel = new JLabel("Class:");
        classLabel.setBounds(20, 140, 100, 25);
        add(classLabel);

        classField = new JTextField();
        classField.setBounds(120, 140, 150, 25);
        add(classField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 180, 100, 25);
        add(genderLabel);

        genderField = new JTextField();
        genderField.setBounds(120, 180, 150, 25);
        add(genderField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 220, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 220, 150, 25);
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(20, 260, 100, 25);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(120, 260, 150, 25);
        add(phoneField);

        JButton addButton = new JButton("Add Student");
        addButton.setBounds(20, 300, 250, 30);
        addButton.addActionListener(this);
        add(addButton);

        // Label for Search/Delete
        JLabel searchLabel = new JLabel("Search/Delete by Roll No.:");
        searchLabel.setBounds(20, 450, 190, 25);
        add(searchLabel);

// Roll number field for search/delete
        searchRollField = new JTextField();
        searchRollField.setBounds(170, 450, 100, 25);
        add(searchRollField);

// Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(40, 490, 90, 25);
        searchButton.addActionListener(e -> searchStudentByRoll());
        add(searchButton);

// Delete button
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(150, 490, 90, 25);
        deleteButton.addActionListener(e -> deleteStudentByRoll());
        add(deleteButton);

        JButton sortButton = new JButton("Sort Students");
        sortButton.setBounds(20, 340, 250, 30);
        sortButton.addActionListener(e -> sortStudents());
        add(sortButton);

        JLabel sortLabel = new JLabel("Sort By:");
        sortLabel.setBounds(20, 380, 100, 25);
        add(sortLabel);

        sortCriteria = new JComboBox<>(new String[]{"Name", "Roll Number", "Grade"});
        sortCriteria.setBounds(120, 380, 150, 25);
        add(sortCriteria);

        ascending = new JRadioButton("Ascending", true);
        ascending.setBounds(20, 420, 100, 25);
        add(ascending);

        descending = new JRadioButton("Descending");
        descending.setBounds(140, 420, 100, 25);
        add(descending);

        ButtonGroup sortOrder = new ButtonGroup();
        sortOrder.add(ascending);
        sortOrder.add(descending);

        // Table Setup
        tableModel = new DefaultTableModel(
                new String[]{"Roll No", "Name", "Grade", "Class", "Gender", "Email", "Phone"}, 0
        );
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(300, 20, 570, 500);
        add(scrollPane);

        loadStudentData();

        setVisible(true);
    }

    private Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/student_management";
        String username = "root";
        String password = "Chakra6=";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    @Override
public void actionPerformed(ActionEvent e) {
    // Input Validation
    if (!rollField.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(null, "Roll Number must be numeric.");
        return;
    }
    if (!emailField.getText().contains("@")) {
        JOptionPane.showMessageDialog(null, "Invalid email format.");
        return;
    }
    if (!phoneField.getText().matches("\\d{10}")) {
        JOptionPane.showMessageDialog(null, "Phone number must be 10 digits.");
        return;
    }
    try {
        double grade = Double.parseDouble(gradeField.getText());
        if (grade < 0 || grade > 100) {
            JOptionPane.showMessageDialog(null, "Grade must be between 0 and 100.");
            return;
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Grade must be a valid number.");
        return;
    }

    String query = "INSERT INTO student(name, roll_number, grade, class, gender, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getConnection()) {
        if (conn == null) {
            return;
        }

        PreparedStatement pstm = conn.prepareStatement(query);
        pstm.setString(1, nameField.getText());
        pstm.setInt(2, Integer.parseInt(rollField.getText()));
        pstm.setDouble(3, Double.parseDouble(gradeField.getText()));
        pstm.setString(4, classField.getText());
        pstm.setString(5, genderField.getText());
        pstm.setString(6, emailField.getText());
        pstm.setString(7, phoneField.getText());
        pstm.executeUpdate();

        JOptionPane.showMessageDialog(null, "Student added successfully!");

        clearField();
        loadStudentData();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
}


    private void clearField() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
        classField.setText("");
        genderField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    private void loadStudentData() {
        tableModel.setRowCount(0); // Clear table
        String query = "SELECT roll_number, name, grade, class, gender, email, phone_number FROM student";

        try (Connection conn = getConnection()) {
            if (conn != null) {
                PreparedStatement pstm = conn.prepareStatement(query);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    int roll = rs.getInt("roll_number");
                    String name = rs.getString("name");
                    double grade = rs.getDouble("grade");
                    String sClass = rs.getString("class");
                    String gender = rs.getString("gender");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone_number");
                    tableModel.addRow(new Object[]{roll, name, grade, sClass, gender, email, phone});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    private void sortStudents() {
        String selected = (String) sortCriteria.getSelectedItem();
        String column;

        if ("Roll Number".equals(selected)) {
            column = "roll_number";
        } else if ("Grade".equals(selected)) {
            column = "grade";
        } else {
            column = "name";
        }

        String order = ascending.isSelected() ? "ASC" : "DESC";
        String query = "SELECT roll_number, name, grade, class, gender, email, phone_number FROM student ORDER BY " + column + " " + order;

        tableModel.setRowCount(0);
        try (Connection conn = getConnection()) {
            if (conn != null) {
                PreparedStatement pstm = conn.prepareStatement(query);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    int roll = rs.getInt("roll_number");
                    String name = rs.getString("name");
                    double grade = rs.getDouble("grade");
                    String sClass = rs.getString("class");
                    String gender = rs.getString("gender");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone_number");
                    tableModel.addRow(new Object[]{roll, name, grade, sClass, gender, email, phone});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error sorting: " + e.getMessage());
        }
    }

    private void searchStudentByRoll() {
    String rollText = searchRollField.getText();
    if (rollText.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please enter a Roll Number to search.");
        return;
    }

    try (Connection conn = getConnection()) {
        String query = "SELECT roll_number, name, grade, class, gender, email, phone_number FROM student WHERE roll_number = ?";
        PreparedStatement pstm = conn.prepareStatement(query);
        pstm.setInt(1, Integer.parseInt(rollText));
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            loadStudentData(); // Load all data
            // Find and highlight row
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (String.valueOf(tableModel.getValueAt(i, 0)).equals(rollText)) {
                    studentTable.setRowSelectionInterval(i, i);
                    studentTable.scrollRectToVisible(studentTable.getCellRect(i, 0, true));
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No student found with Roll Number: " + rollText);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error searching: " + e.getMessage());
    }
}


private void deleteStudentByRoll() {
    String rollText = searchRollField.getText();
    if (rollText.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please enter a Roll Number to delete.");
        return;
    }

    try (Connection conn = getConnection()) {
        String query = "DELETE FROM student WHERE roll_number = ?";
        PreparedStatement pstm = conn.prepareStatement(query);
        pstm.setInt(1, Integer.parseInt(rollText));
        int rows = pstm.executeUpdate();

        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "Student deleted successfully!");
            loadStudentData();
        } else {
            JOptionPane.showMessageDialog(null, "No student found with Roll Number: " + rollText);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error deleting: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        new StudentManagement();
    }
}

// javac StudentManagement.java , java StudentManagement
// javac -cp ".;D:\Download\mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar" StudentManagement.java  , java -cp ".;D:\Download\mysql-connector-j-9.4.0\mysql-connector-j-9.4.0.jar" StudentManagement   
