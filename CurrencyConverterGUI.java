import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterGUI extends JFrame implements ActionListener {

    private final JTextField amountField;
    private final JComboBox<String> fromCurrency;
    private final JComboBox<String> toCurrency;
    private final JLabel resultLabel;
    private final JButton convertButton;
    private final JButton swapButton;

    // Exchange rates relative to 1 USD (Base Currency)
    private final Map<String, Double> exchangeRates = new HashMap<>();

    public CurrencyConverterGUI() {
        // 1. Initialize exchange rates (Base: USD = 1.0)
        exchangeRates.put("USD - US Dollar", 1.00);
        exchangeRates.put("INR - Indian Rupee", 84.05);
        exchangeRates.put("EUR - Euro", 0.92);
        exchangeRates.put("GBP - British Pound", 0.77);
        exchangeRates.put("JPY - Japanese Yen", 152.30);
        exchangeRates.put("AUD - Australian Dollar", 1.51);
        exchangeRates.put("CAD - Canadian Dollar", 1.38);

        // 2. Frame Setup
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null); // Center window on screen
        setResizable(false);

        // 3. Main Panel with Padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 4. Form Panel (Inputs & Dropdowns)
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));

        String[] currencies = exchangeRates.keySet().toArray(new String[0]);

        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField("1.00");
        amountField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(amountField);

        formPanel.add(new JLabel("From Currency:"));
        fromCurrency = new JComboBox<>(currencies);
        fromCurrency.setSelectedItem("USD - US Dollar");
        formPanel.add(fromCurrency);

        formPanel.add(new JLabel("To Currency:"));
        toCurrency = new JComboBox<>(currencies);
        toCurrency.setSelectedItem("INR - Indian Rupee");
        formPanel.add(toCurrency);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 5. Bottom Panel (Buttons & Result Display)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        convertButton = new JButton("Convert");
        convertButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        convertButton.addActionListener(this);

        swapButton = new JButton("⇅ Swap");
        swapButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        swapButton.addActionListener(e -> swapCurrencies());

        buttonPanel.add(convertButton);
        buttonPanel.add(swapButton);

        resultLabel = new JLabel("Converted Amount: --", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        resultLabel.setForeground(new Color(0, 102, 51));

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == convertButton) {
            performConversion();
        }
    }

    private void performConversion() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount < 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            double fromRate = exchangeRates.get(from);
            double toRate = exchangeRates.get(to);

            // Convert "From" currency to USD base, then multiply by "To" currency rate
            double amountInUSD = amount / fromRate;
            double convertedAmount = amountInUSD * toRate;

            String toCode = to.substring(0, 3);
            resultLabel.setText(String.format("Converted Amount: %.2f %s", convertedAmount, toCode));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void swapCurrencies() {
        int fromIndex = fromCurrency.getSelectedIndex();
        fromCurrency.setSelectedIndex(toCurrency.getSelectedIndex());
        toCurrency.setSelectedIndex(fromIndex);
        performConversion();
    }

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            new CurrencyConverterGUI().setVisible(true);
        });
    }
}
