import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleCalculatorUI {

    private JFrame frame;
    private JTextField displayField;
    private double result = 0;
    private String operator = "";
    private boolean isOperatorPressed = false;

    public SimpleCalculatorUI() {
       
        frame = new JFrame("Simple Calculator");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.BOLD, 24));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setText("0");


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));

      
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }


        frame.setLayout(new BorderLayout(10, 10));
        frame.add(displayField, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        private String currentInput = "";

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.matches("[0-9]")) {
                currentInput += command;
                displayField.setText(currentInput);
            } else if (command.equals(".")) {
                if (!currentInput.contains(".")) {
                    currentInput += ".";
                    displayField.setText(currentInput);
                }
            } else if (command.matches("[+\-*/]")) {
                if (!currentInput.isEmpty()) {
                    calculate(Double.parseDouble(currentInput));
                }
                operator = command;
                currentInput = "";
                isOperatorPressed = true;
            } else if (command.equals("=")) {
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    calculate(Double.parseDouble(currentInput));
                    operator = "";
                    currentInput = "";
                }
            } else if (command.equals("C")) {
                currentInput = "";
                result = 0;
                operator = "";
                displayField.setText("0");
            }
        }

        private void calculate(double input) {
            switch (operator) {
                case "":
                    result = input;
                    break;
                case "+":
                    result += input;
                    break;
                case "-":
                    result -= input;
                    break;
                case "*":
                    result *= input;
                    break;
                case "/":
                    if (input != 0) {
                        result /= input;
                    } else {
                        displayField.setText("Error");
                        currentInput = "";
                        result = 0;
                        operator = "";
                        return;
                    }
                    break;
            }
            displayField.setText(String.valueOf(result));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleCalculatorUI::new);
    }
}