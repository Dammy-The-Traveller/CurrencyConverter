package com.damilare.currencyconverter;

// Imports visual tools to organize components into sections (North, South, East, West, Center)
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory; // Tool to draw borders and frames around sections
import javax.swing.JButton; // Tool to create clickable push buttons
import javax.swing.JComboBox; // Tool to create drop-down pick lists
import javax.swing.JFrame; // Tool to create the main application window frame
import javax.swing.JLabel; // Tool to write text messages on the screen
import javax.swing.JPanel; // Tool to create invisible container boxes for grouping items
import javax.swing.JScrollPane; // Tool to add scrollbars when lists or tables grow large
import javax.swing.JTable; // Tool to display data organized in rows and columns
import javax.swing.JTextField; // Tool to create text boxes where users type numbers
import javax.swing.SwingWorker; // Tool to run tasks in the background so the window doesn't freeze
import javax.swing.UIManager; // Tool to match app visual look with the host operating system
import javax.swing.border.EmptyBorder; // Tool to add invisible padding margins inside panels
import javax.swing.table.DefaultTableModel; // Tool to manage data rows and columns shown in a table

// Defines the main desktop window class, extending JFrame to make it a real window
public class CurrencyConverterGUI extends JFrame {

    // Defines constant background color (light grey-blue) using Red, Green, Blue values
    private static final Color BACKGROUND = new Color(245, 247, 251);
    // Defines constant white color for section background cards
    private static final Color CARD = Color.WHITE;
    // Defines constant primary blue color for buttons and highlights
    private static final Color PRIMARY = new Color(37, 99, 235);
    // Defines constant dark blue color for button hover states
    private static final Color PRIMARY_DARK = new Color(29, 78, 216);
    // Defines constant dark grey color for readable main text
    private static final Color TEXT = new Color(31, 41, 55);
    // Defines constant muted grey color for secondary subtitle text
    private static final Color MUTED = new Color(107, 114, 128);
    // Defines constant light grey color for subtle outline borders
    private static final Color BORDER = new Color(229, 231, 235);
    // Defines constant green color for successful status messages
    private static final Color SUCCESS = new Color(5, 150, 105);

    // Creates the math engine object that calculates conversion values
    private final CurrencyConverter converter;
    // Creates the online service object that fetches live rates from the internet
    private final ExchangeRateService exchangeRateService;
    // Creates the history manager object that tracks previous conversions
    private final ConversionHistory history;

    // Drop-down selector for picking the source currency
    private final JComboBox<Currency> fromCurrency;
    // Drop-down selector for picking the destination currency
    private final JComboBox<Currency> toCurrency;
    // Input text box for typing money amounts
    private final JTextField amountField;
    // Large text display showing the converted monetary result
    private final JLabel resultAmountLabel;
    // Text description explaining the conversion summary
    private final JLabel resultDescriptionLabel;
    // Text label displaying unit conversion exchange rate
    private final JLabel rateLabel;
    // Text label showing app status at the bottom of the window
    private final JLabel statusLabel;
    // Main action button clicked to execute conversion
    private final JButton convertButton;
    // Data manager that holds table rows for recent conversion history
    private final DefaultTableModel historyTableModel;

    // Constructor method that runs when the GUI window is launched
    public CurrencyConverterGUI(List<Currency> currencies) {
        // Initializes the math calculation engine
        converter = new CurrencyConverter();
        // Initializes the API client for fetching exchange rates
        exchangeRateService = new ExchangeRateService();
        // Initializes the conversion history list tracker
        history = new ConversionHistory();

        // Populates the "From" drop-down menu with supported currency options
        fromCurrency = new JComboBox<>(currencies.toArray(new Currency[0]));
        // Populates the "To" drop-down menu with supported currency options
        toCurrency = new JComboBox<>(currencies.toArray(new Currency[0]));

        // Pre-selects USD as the default source currency
        fromCurrency.setSelectedItem(Currency.USD);
        // Pre-selects GHS as the default target currency
        toCurrency.setSelectedItem(Currency.GHS);

        // Pre-fills amount input box with "100"
        amountField = new JTextField("100");
        // Sets default placeholder text for the result display
        resultAmountLabel = new JLabel("Enter an amount to get started");
        // Sets default descriptive instruction text
        resultDescriptionLabel = new JLabel("Your converted amount will appear here.");
        // Sets default text for rate information
        rateLabel = new JLabel("Exchange rate will appear here");
        // Sets initial status message
        statusLabel = new JLabel("Ready to convert");
        // Creates main convert button
        convertButton = new JButton("Convert");

        // Configures table column titles and starting row count (0)
        historyTableModel = new DefaultTableModel(
                new String[]{"Date & Time", "Amount", "From", "Converted Amount", "To", "Rate"},
                0
        ) {
            // Overrides table default settings to lock cells from manual editing
            @Override
            public boolean isCellEditable(int row, int column) {
                // Returns false so users cannot edit history table entries directly
                return false;
            }
        };

        // Calls method to set window dimensions and operating system styles
        initializeWindow();
        // Calls method to build visual panels, cards, buttons, and layouts
        buildInterface();
        // Calls method to connect click actions and keyboard triggers to functions
        configureActions();
    }

    // Helper method setting window parameters (size, title, position)
    private void initializeWindow() {
        // Sets the title text on the top frame bar of the desktop app
        setTitle("Currency Converter");
        // Ensures the application fully exits when the window close (X) button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Sets ideal starting window size (1050px wide by 760px tall)
        setSize(1050, 760);
        // Prevents users from resizing window smaller than 800x650 pixels
        setMinimumSize(new Dimension(800, 650));
        // Centers window in middle of desktop screen
        setLocationRelativeTo(null);

        // Colors main frame background with light grey-blue theme
        getContentPane().setBackground(BACKGROUND);

        // Attempts to apply system-native window theme styles
        try {
            // Asks Java to use native Windows or Mac user interface styles
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep the default Swing appearance if the system theme is unavailable.
        }
    }

    // Helper method constructing the visual hierarchy layout
    private void buildInterface() {
        // Creates master layout container panel with vertical gap spacing of 22px
        JPanel root = new JPanel(new BorderLayout(0, 22));
        // Sets background color of master container panel
        root.setBackground(BACKGROUND);
        // Adds outer padding margins around the whole app content
        root.setBorder(new EmptyBorder(28, 32, 22, 32));

        // Adds application header section to the top region
        root.add(buildHeader(), BorderLayout.NORTH);

        // Create the top and bottom cards
    JPanel converterCard = buildConverterCard();
    JPanel historyCard = buildHistoryCard();

    // Create a vertical Split Pane holding both cards
    javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(
            javax.swing.JSplitPane.VERTICAL_SPLIT,
            converterCard,
            historyCard
    );

    // Style the Split Pane to blend seamlessly
    splitPane.setOpaque(false);
    splitPane.setBackground(BACKGROUND);
    splitPane.setBorder(null);
    splitPane.setDividerSize(8); // Drag handle thickness
    splitPane.setContinuousLayout(true); // Smooth resizing while dragging
    splitPane.setResizeWeight(0.4); // Gives 40% height to top, 60% to bottom on resize

    root.add(splitPane, BorderLayout.CENTER);
    root.add(buildFooter(), BorderLayout.SOUTH);

    setContentPane(root);
    }

    // Builds app title and description header section
    private JPanel buildHeader() {
        // Creates header container panel with 7px spacing between title lines
        JPanel header = new JPanel(new BorderLayout(0, 7));
        // Makes header panel transparent
        header.setOpaque(false);

        // Creates main headline label
        JLabel title = new JLabel("Currency Converter");
        // Sets bold headline font style and size 29
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        // Sets dark font color for main title
        title.setForeground(TEXT);

        // Creates descriptive subtitle message
        JLabel subtitle = new JLabel(
                "Convert currencies using current reference exchange rates"
        );
        // Sets plain font style and size 14 for subtitle
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        // Sets muted grey color for subtitle text
        subtitle.setForeground(MUTED);

        // Places title at top of header panel
        header.add(title, BorderLayout.NORTH);
        // Places subtitle directly below title
        header.add(subtitle, BorderLayout.CENTER);

        // Returns constructed header panel
        return header;
    }

    // Builds the main conversion input card section
    private JPanel buildConverterCard() {
        // Creates styled white card container box
        JPanel card = createCard();
        // Sets border layout with 22px spacing inside card
        card.setLayout(new BorderLayout(0, 22));

        // Creates section heading panel
        JPanel heading = new JPanel(new BorderLayout(0, 5));
        // Sets section heading panel to transparent
        heading.setOpaque(false);

        // Creates card section header title label
        JLabel sectionTitle = new JLabel("Make a conversion");
        // Sets bold size 18 font for card section header
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        // Sets main text color for section title
        sectionTitle.setForeground(TEXT);

        // Creates card section instructions subtitle
        JLabel sectionSubtitle = new JLabel(
                "Choose your currencies and enter the amount."
        );
        // Sets plain size 13 font for card subtitle
        sectionSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // Sets muted text color for card subtitle
        sectionSubtitle.setForeground(MUTED);

        // Adds section title to top of heading panel
        heading.add(sectionTitle, BorderLayout.NORTH);
        // Adds section subtitle to bottom of heading panel
        heading.add(sectionSubtitle, BorderLayout.SOUTH);

        // Creates grid container for input fields
        JPanel fields = new JPanel(new GridBagLayout());
        // Makes grid container transparent
        fields.setOpaque(false);

        // Creates layout configuration rules object
        GridBagConstraints gbc = new GridBagConstraints();
        // Sets 14px right margin spacing between input fields
        gbc.insets = new Insets(0, 0, 0, 14);
        // Makes fields stretch horizontally across available width
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Gives horizontal expanding weight to input fields
        gbc.weightx = 1;
        // Sets grid placement row coordinate to 0
        gbc.gridy = 0;

        // Adds Amount input field to grid column 0
        addField(fields, gbc, 0, "Amount", amountField);
        // Adds From Currency drop-down selector to grid column 1
        addField(fields, gbc, 1, "From currency", fromCurrency);
        // Adds To Currency drop-down selector to grid column 2
        addField(fields, gbc, 2, "To currency", toCurrency);

        // Sets swap button column coordinate position to 3
        gbc.gridx = 3;
        // Sets fixed width weight so swap button does not stretch excessively
        gbc.weightx = 0;
        // Sets top spacing margin of 23px to align swap button with input fields
        gbc.insets = new Insets(23, 0, 0, 0);

        // Creates styled secondary button for swapping currencies
        JButton swapButton = createSecondaryButton("⇄  Swap");
        // Attaches event handler to swap dropdown selections on click
        swapButton.addActionListener(event -> {
            // Stores selected value of source currency dropdown
            Object selectedFrom = fromCurrency.getSelectedItem();
            // Stores selected value of destination currency dropdown
            Object selectedTo = toCurrency.getSelectedItem();

            // Moves target currency value into source dropdown
            fromCurrency.setSelectedItem(selectedTo);
            // Moves source currency value into target dropdown
            toCurrency.setSelectedItem(selectedFrom);
        });
        // Adds swap button into grid panel using constraints
        fields.add(swapButton, gbc);

        // Applies custom styling to text input box
        styleInput(amountField);
        // Applies custom styling to source currency drop-down
        styleComboBox(fromCurrency);
        // Applies custom styling to destination currency drop-down
        styleComboBox(toCurrency);

        // Creates panel container for main action convert button
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // Sets action panel to transparent
        actions.setOpaque(false);

        // Sets bold size 14 font for convert button
        convertButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        // Sets white text color for convert button
        convertButton.setForeground(Color.WHITE);
        // Sets primary blue background color for convert button
        convertButton.setBackground(PRIMARY);
        // Removes default dotted focus ring around button text
        convertButton.setFocusPainted(false);
        // Removes default 3D border outline
        convertButton.setBorderPainted(false);
        // Makes background color paint properly across OS platforms
        convertButton.setOpaque(true);
        // Changes cursor pointer to hand icon on mouse hover over button
        convertButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Sets inner padding padding inside convert button
        convertButton.setBorder(new EmptyBorder(12, 25, 12, 25));

        // Places convert button into actions panel
        actions.add(convertButton);

        // Creates form container panel combining inputs and convert button
        JPanel form = new JPanel(new BorderLayout(0, 20));
        // Sets form panel to transparent
        form.setOpaque(false);
        // Places input fields in center of form
        form.add(fields, BorderLayout.CENTER);
        // Places action button at bottom of form
        form.add(actions, BorderLayout.SOUTH);

        // Creates highlighted result display card box
        JPanel resultCard = new JPanel(new BorderLayout(0, 10));
        // Sets light blue background color for result display card
        resultCard.setBackground(new Color(239, 246, 255));
        // Creates outline border and inner margins around result card
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(219, 234, 254)),
                new EmptyBorder(18, 20, 18, 20)
        ));

        // Creates uppercase small header tag for result section
        JLabel resultHeading = new JLabel("CONVERSION RESULT");
        // Sets small, bold size 11 font for tag label
        resultHeading.setFont(new Font("SansSerif", Font.BOLD, 11));
        // Colors tag text with primary blue accent color
        resultHeading.setForeground(PRIMARY);

        // Sets prominent large bold size 27 font for output conversion amount
        resultAmountLabel.setFont(new Font("SansSerif", Font.BOLD, 27));
        // Colors output conversion number with rich dark green
        resultAmountLabel.setForeground(new Color(22, 101, 52));

        // Sets plain size 13 font for description text
        resultDescriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // Sets main text color for description text
        resultDescriptionLabel.setForeground(TEXT);

        // Sets plain size 13 font for exchange rate details
        rateLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // Sets muted grey text color for exchange rate text
        rateLabel.setForeground(MUTED);

        // Creates container holding text elements of conversion results
        JPanel resultText = new JPanel(new BorderLayout(0, 8));
        // Sets result text panel transparent
        resultText.setOpaque(false);
        // Places conversion result number at top
        resultText.add(resultAmountLabel, BorderLayout.NORTH);
        // Places description explanation in middle
        resultText.add(resultDescriptionLabel, BorderLayout.CENTER);
        // Places exchange rate info at bottom
        resultText.add(rateLabel, BorderLayout.SOUTH);

        // Places header tag at top of result box card
        resultCard.add(resultHeading, BorderLayout.NORTH);
        // Places result text block in center of result box card
        resultCard.add(resultText, BorderLayout.CENTER);

        // Places section heading at top of main converter card
        card.add(heading, BorderLayout.NORTH);
        // Places input form in center of main converter card
        card.add(form, BorderLayout.CENTER);
        // Places result box card at bottom of main converter card
        card.add(resultCard, BorderLayout.SOUTH);

        // Returns completed conversion card panel
        return card;
    }

    // Builds conversion history card containing table display
    private JPanel buildHistoryCard() {
        // Creates styled white card container box
        JPanel card = createCard();
        // Sets border layout with 15px gap inside card
        card.setLayout(new BorderLayout(0, 15));

        // Creates header title for history card section
        JLabel heading = new JLabel("Recent conversions");
        // Sets bold size 18 font for history section heading
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        // Sets main text color for history heading
        heading.setForeground(TEXT);

        // Creates secondary action button to clear conversion logs
        JButton clearHistoryButton = createSecondaryButton("Clear history");
        // Attaches click event to execute clear history function
        clearHistoryButton.addActionListener(event -> clearHistory());

        // Creates top bar container panel for history section
        JPanel historyHeader = new JPanel(new BorderLayout());
        // Makes history header panel transparent
        historyHeader.setOpaque(false);
        // Places section title on left side of header bar
        historyHeader.add(heading, BorderLayout.WEST);
        // Places clear history button on right side of header bar
        historyHeader.add(clearHistoryButton, BorderLayout.EAST);

        // Creates visual table component connected to table data model
        JTable table = new JTable(historyTableModel);
        // Sets plain size 12 font for table row cells
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // Sets text color for table cells
        table.setForeground(TEXT);
        // Sets white background color for table cells
        table.setBackground(Color.WHITE);
        // Sets row height to 30px for comfortable reading space
        table.setRowHeight(40);
        // Hides vertical grid lines between table columns
        table.setShowVerticalLines(false);
        // Sets light grey color for horizontal grid dividers
        table.setGridColor(BORDER);
        // Makes table fill full height of scrolling viewport window
        table.setFillsViewportHeight(true);
        // Enables automatic column sorting when header titles are clicked
        table.setAutoCreateRowSorter(true);

        // Sets bold size 12 font for table column headers
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        // Sets muted grey text color for column headers
        table.getTableHeader().setForeground(MUTED);
        // Sets light grey background color for table header row
        table.getTableHeader().setBackground(new Color(249, 250, 251));
        // Sets fixed height of 34px for table header row
        table.getTableHeader().setPreferredSize(new Dimension(0, 34));
        // Prevents users from reordering table columns by dragging
        table.getTableHeader().setReorderingAllowed(false);

        // Wraps table inside a scroll pane container so scrolling works automatically
        JScrollPane scrollPane = new JScrollPane(table);
        // Draws outline border around table viewport
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        // Sets white background color for table scroll background
        scrollPane.getViewport().setBackground(Color.WHITE);
        // Sets default size dimension for table scrolling panel
        scrollPane.setPreferredSize(new Dimension(700, 190));

        // Places history header bar at top of history card
        card.add(historyHeader, BorderLayout.NORTH);
        // Places scrollable table in center of history card
        card.add(scrollPane, BorderLayout.CENTER);

        // Returns completed history card panel
        return card;
    }

    // Builds footer section displaying app status messages
    private JPanel buildFooter() {
        // Creates footer container panel
        JPanel footer = new JPanel(new BorderLayout());
        // Makes footer panel transparent
        footer.setOpaque(false);

        // Sets plain size 12 font for status label
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // Sets muted grey color for status label
        statusLabel.setForeground(MUTED);

        // Creates static legal/disclaimer note
        JLabel note = new JLabel("Rates are indicative and may change.");
        // Sets plain size 11 font for disclaimer note
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        // Sets muted grey color for disclaimer note
        note.setForeground(MUTED);

        // Places status bar message on left side of footer
        footer.add(statusLabel, BorderLayout.WEST);
        // Places disclaimer note on right side of footer
        footer.add(note, BorderLayout.EAST);

        // Returns completed footer panel
        return footer;
    }

    // Helper factory method creating standard white card panels with padded borders
    private JPanel createCard() {
        // Creates base panel container
        JPanel panel = new JPanel();
        // Sets clean white background color
        panel.setBackground(CARD);
        // Creates outer border line and inner 22px padding margins
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(22, 22, 22, 22)
        ));
        // Returns styled base card panel
        return panel;
    }

    // Helper method adding input label and component into grid layout
    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int column,
            String labelText,
            java.awt.Component component
    ) {
        // Creates stacked container box with 9px vertical spacing
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 9));
        // Sets field container box transparent
        fieldPanel.setOpaque(false);

        // Creates field descriptor label text
        JLabel label = new JLabel(labelText);
        // Sets bold size 12 font for input label
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        // Sets dark text color for input label
        label.setForeground(TEXT);

        // Places label at top of stacked field box
        fieldPanel.add(label, BorderLayout.NORTH);
        // Places input component (textbox/dropdown) in center of stacked box
        fieldPanel.add(component, BorderLayout.CENTER);

        // Sets column index position in grid layout
        gbc.gridx = column;
        // Adds field box to parent panel using grid configuration rules
        panel.add(fieldPanel, gbc);
    }

    // Helper method setting visual styles for text input boxes
    private void styleInput(JTextField field) {
        // Sets plain size 14 font inside text box
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        // Sets text color for typed characters
        field.setForeground(TEXT);
        // Sets background color of text box to white
        field.setBackground(Color.WHITE);
        // Colors blinking typing cursor with primary blue
        field.setCaretColor(PRIMARY);
        // Creates light border line and inner padding margins around typed text
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 11, 10, 11)
        ));
        // Sets fixed width (150px) and height (42px) for input box
        field.setPreferredSize(new Dimension(150, 42));
    }

    // Helper method setting visual styles for dropdown pickers
    private void styleComboBox(JComboBox<Currency> comboBox) {
        // Sets plain size 13 font for dropdown menu items
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // Sets background color of dropdown to white
        comboBox.setBackground(Color.WHITE);
        // Sets text color for dropdown menu items
        comboBox.setForeground(TEXT);
        // Sets fixed width (150px) and height (42px) for dropdown menu
        comboBox.setPreferredSize(new Dimension(150, 42));
        // Disables focus rectangle highlight around dropdown text
        comboBox.setFocusable(false);
    }

    // Helper factory method creating styled secondary action buttons
    private JButton createSecondaryButton(String text) {
        // Creates new push button containing given text
        JButton button = new JButton(text);
        // Sets bold size 12 font for button text
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        // Colors button text dark blue
        button.setForeground(PRIMARY_DARK);
        // Sets background color of secondary button to white
        button.setBackground(Color.WHITE);
        // Disables default dotted focus highlight box around text
        button.setFocusPainted(false);
        // Changes cursor icon to hand symbol on mouse hover
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Draws soft blue border outline and internal padding inside button
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                new EmptyBorder(10, 13, 10, 13)
        ));
        // Returns styled secondary button
        return button;
    }

    // Attaches event listeners and handlers to buttons and input boxes
    private void configureActions() {
        // Executes conversion calculation when convert button is clicked
        convertButton.addActionListener(event -> convertCurrency());
        // Executes conversion calculation when "Enter" key is pressed in input field
        amountField.addActionListener(event -> convertCurrency());
    }

    // Core method validating inputs, running API request in background, and updating GUI
    private void convertCurrency() {
        // Reads typed text from input field and trims whitespace
        String amountText = amountField.getText().trim();

        // Checks if user left the amount input empty
        if (amountText.isEmpty()) {
            // Displays input error warning popup
            showError("Please enter an amount.");
            // Exits method early
            return;
        }

        // Declares variable to store parsed numeric amount
        BigDecimal amount;

        // Attempts to convert typed text string into a numeric decimal object
        try {
            // Parses string into a BigDecimal number
            amount = new BigDecimal(amountText);

            // Checks if typed number is zero or negative
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                // Displays error warning if amount is not positive
                showError("The amount must be greater than zero.");
                // Exits method early
                return;
            }
        // Catches exception if user typed letters or symbols instead of digits
        } catch (NumberFormatException e) {
            // Displays warning requiring numeric entry
            showError("Please enter a valid numeric amount.");
            // Exits method early
            return;
        }

        // Retrieves currently chosen currency object from source dropdown
        Currency from = (Currency) fromCurrency.getSelectedItem();
        // Retrieves currently chosen currency object from target dropdown
        Currency to = (Currency) toCurrency.getSelectedItem();

        // Verifies that both source and target currencies are selected
        if (from == null || to == null) {
            // Displays warning asking user to select currencies
            showError("Please select both currencies.");
            // Exits method early
            return;
        }

        // Temporarily disables convert button to prevent multiple rapid submit clicks
        convertButton.setEnabled(false);
        // Displays loading status text in result display number field
        resultAmountLabel.setText("Converting...");
        // Displays loading status message in description label
        resultDescriptionLabel.setText("Retrieving the latest available reference rate.");
        // Clears old rate message text
        rateLabel.setText("");
        // Updates status bar text to show active operation
        statusLabel.setText("Conversion in progress...");

        // Creates background thread handler so web requests don't freeze app user interface
        new SwingWorker<Conversion, Void>() {
            // Work executed strictly on a background thread
            @Override
            protected Conversion doInBackground() {
                // Fetches live exchange rate from API service
                BigDecimal rate = exchangeRateService.getRate(from, to);
                // Calculates target output result money amount
                BigDecimal result = converter.convert(amount, rate);

                // Instantiates and returns a new conversion transaction record
                return new Conversion(amount, from, to, rate, result);
            }

            // Automatically runs on the UI main thread once background task finishes
            @Override
            protected void done() {
                try {
                    // Retrieves output result object calculated by background thread
                    Conversion conversion = get();
                    // Appends completed conversion record to history manager
                    history.addConversion(conversion);

                    // Formats and displays final converted result money value (e.g. GH₵ 1,180.00 GHS)
                    resultAmountLabel.setText(
                            conversion.getTo().getSymbol() + " "
                                    + conversion.getResult()
                                            .setScale(2, RoundingMode.HALF_UP)
                                            .toPlainString()
                                    + " " + conversion.getTo().getCode()
                    );

                    // Formats and displays human-readable summary message explaining calculation
                    resultDescriptionLabel.setText(
                            amount.setScale(2, RoundingMode.HALF_UP).toPlainString()
                                    + " " + from.getCode()
                                    + " converts to "
                                    + conversion.getTo().getCode()
                    );

                    // Formats and displays individual unit conversion exchange rate (e.g. 1 USD = 11.8000 GHS)
                    rateLabel.setText(
                            "1 " + from.getCode() + " = "
                                    + conversion.getExchangeRate()
                                            .setScale(4, RoundingMode.HALF_UP)
                                            .toPlainString()
                                    + " " + to.getCode()
                    );

                    // Updates status bar to show conversion succeeded
                    statusLabel.setText("Conversion successful.");
                    // Sets status bar text color to success green
                    statusLabel.setForeground(SUCCESS);
                    // Reloads history table rows to show newest record
                    refreshHistory();

                // Catches network failures or mathematical errors thrown during conversion
                } catch (Exception e) {
                    // Gets root cause exception object
                    Throwable cause = e.getCause();
                    // Extracts specific error message text string
                    String message = cause != null
                            ? cause.getMessage()
                            : e.getMessage();

                    // Sets result display header to show failure state
                    resultAmountLabel.setText("Conversion failed");
                    // Explains error troubleshooting advice in result description box
                    resultDescriptionLabel.setText(
                            "Check your connection or selected currency pair."
                    );
                    // Clears rate text
                    rateLabel.setText("");
                    // Updates status bar text to display failure message
                    statusLabel.setText("Unable to complete conversion.");
                    // Colors status bar text red to indicate error
                    statusLabel.setForeground(new Color(185, 28, 28));

                    // Pops up warning dialog window detailing error cause
                    javax.swing.JOptionPane.showMessageDialog(
                            CurrencyConverterGUI.this,
                            message == null
                                    ? "An unexpected error occurred."
                                    : message,
                            "Conversion Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                // Code inside finally always executes whether calculation succeeded or failed
                } finally {
                    // Re-enables convert button so user can perform next calculation
                    convertButton.setEnabled(true);
                }
            }
        }.execute(); // Launches background worker thread execution
    }

    // Helper method re-populating table model rows from stored conversion history
    private void refreshHistory() {
        // Resets current visible row count to zero (clears existing table view)
        historyTableModel.setRowCount(0);

        // Iterates through every recorded conversion object in history tracker
        for (Conversion conversion : history.getConversions()) {
            // Adds a formatted array of conversion values as a new row in table model
            historyTableModel.addRow(new Object[]{
                    conversion.getFormattedTimestamp(), // Displays formatted timestamp string
                    conversion.getAmount().setScale(2, RoundingMode.HALF_UP), // Displays source amount rounded to 2 decimal places
                    conversion.getFrom().getCode(), // Displays source currency ISO code
                    conversion.getResult().setScale(2, RoundingMode.HALF_UP), // Displays target output rounded to 2 decimal places
                    conversion.getTo().getCode(), // Displays target currency ISO code
                    conversion.getExchangeRate().setScale(4, RoundingMode.HALF_UP) // Displays unit exchange rate rounded to 4 decimal places
            });
        }
    }

    // Helper method clearing conversion log entries from memory and user interface
    private void clearHistory() {
        // Checks if conversion history is already empty
        if (history.isEmpty()) {
            // Updates status bar to inform user no history exists
            statusLabel.setText("There is no history to clear.");
            // Exits method early
            return;
        }

        // Pops up a confirmation dialog box asking user to confirm clearing history
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear all conversion history?",
                "Clear Conversion History",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        // Checks if user clicked "YES" on confirmation popup
        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            try {
                // Clears history data records from memory/storage
                history.clear();
                // Removes all rows from visual table UI
                historyTableModel.setRowCount(0);
                // Updates status bar text confirming history was cleared
                statusLabel.setText("Conversion history cleared.");
                // Resets status bar text color back to muted grey
                statusLabel.setForeground(MUTED);
            // Catches storage errors if history clearance fails
            } catch (RuntimeException e) {
                // Displays error dialog window explaining clearance failure
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Could not clear saved history: " + e.getMessage(),
                        "History Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Helper method launching standard input error alert popup boxes
    private void showError(String message) {
        // Displays visual dialog popup showing warning message and warning icon
        javax.swing.JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );
    }
}