package edu.travelplanner.gui;

import edu.travelplanner.decorator.BasicCityPlan;
import edu.travelplanner.decorator.CityCenterVisit;
import edu.travelplanner.decorator.MuseumVisit;
import edu.travelplanner.decorator.ParkVisit;
import edu.travelplanner.decorator.ShoppingMallVisit;
import edu.travelplanner.decorator.TravelPlanComponent;
import edu.travelplanner.iterator.WeatherCityIterator;
import edu.travelplanner.iterator.WeatherIteratorFactory;
import edu.travelplanner.model.City;
import edu.travelplanner.model.WeatherState;
import edu.travelplanner.observer.WeatherObserver;
import edu.travelplanner.observer.WeatherReportProvider;
import edu.travelplanner.repository.CityRepository;
import edu.travelplanner.strategy.AreaSortStrategy;
import edu.travelplanner.strategy.CitySortStrategy;
import edu.travelplanner.strategy.CitySorter;
import edu.travelplanner.strategy.NameSortStrategy;
import edu.travelplanner.strategy.PopulationSortStrategy;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TravelPlannerFrame extends JFrame implements WeatherObserver {
    private static final Color APP_BACKGROUND = new Color(238, 243, 248);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color NAVY = new Color(31, 55, 78);
    private static final Color BLUE = new Color(52, 111, 153);
    private static final Color GREEN = new Color(86, 153, 112);
    private static final Color PURPLE = new Color(142, 101, 178);
    private static final Color ORANGE = new Color(205, 132, 74);
    private static final Color BORDER = new Color(210, 222, 232);
    private static final Color TEXT_MUTED = new Color(85, 96, 110);

    private final CityRepository cityRepository;
    private final CitySorter citySorter;
    private final WeatherReportProvider weatherReportProvider;

    private final DefaultListModel<City> sortedCityListModel;
    private final DefaultListModel<City> weatherFilteredCityListModel;

    private final JList<City> sortedCityList;
    private final JList<City> weatherFilteredCityList;

    private final JComboBox<String> sortComboBox;
    private final JComboBox<WeatherState> weatherComboBox;
    private final JTextField searchField;

    private final JCheckBox museumCheckBox;
    private final JCheckBox shoppingMallCheckBox;
    private final JCheckBox parkCheckBox;
    private final JCheckBox cityCenterCheckBox;

    private final JTextArea planDescriptionArea;
    private final JLabel totalCostLabel;
    private final JLabel totalTimeLabel;
    private final JLabel selectedCityLabel;
    private final JLabel lastUpdateLabel;

    private final DefaultCategoryDataset temperatureDataset;
    private final DefaultPieDataset weatherPieDataset;

    public TravelPlannerFrame() {
        this.cityRepository = CityRepository.getInstance();
        this.citySorter = new CitySorter(new NameSortStrategy());
        this.weatherReportProvider = new WeatherReportProvider(cityRepository.getCities());

        this.sortedCityListModel = new DefaultListModel<>();
        this.weatherFilteredCityListModel = new DefaultListModel<>();

        this.sortedCityList = new JList<>(sortedCityListModel);
        this.weatherFilteredCityList = new JList<>(weatherFilteredCityListModel);

        this.sortComboBox = new JComboBox<>(new String[]{
                "Sort by Name",
                "Sort by Population",
                "Sort by Area"
        });

        this.weatherComboBox = new JComboBox<>(WeatherState.values());
        this.searchField = new JTextField();

        this.museumCheckBox = new JCheckBox("<html><b>Museum Visit</b><br><span style='font-size:9px;color:#666666;'>350 TL | 2.5 h</span></html>");
        this.shoppingMallCheckBox = new JCheckBox("<html><b>Shopping Mall</b><br><span style='font-size:9px;color:#666666;'>750 TL | 3 h</span></html>");
        this.parkCheckBox = new JCheckBox("<html><b>Park Visit</b><br><span style='font-size:9px;color:#666666;'>150 TL | 1.5 h</span></html>");
        this.cityCenterCheckBox = new JCheckBox("<html><b>City Center</b><br><span style='font-size:9px;color:#666666;'>250 TL | 2 h</span></html>");

        this.planDescriptionArea = new JTextArea(10, 28);
        this.totalCostLabel = new JLabel();
        this.totalTimeLabel = new JLabel();
        this.selectedCityLabel = new JLabel();
        this.lastUpdateLabel = new JLabel("Live simulation is running. Weather data updates every 3 seconds.");

        this.temperatureDataset = new DefaultCategoryDataset();
        this.weatherPieDataset = new DefaultPieDataset();

        configureFrame();
        configureLists();
        registerEvents();

        setContentPane(buildMainPanel());
        updateAllViews();

        this.weatherReportProvider.addObserver(this);
        this.weatherReportProvider.start();
    }

    private void configureFrame() {
        setTitle("SENG 324 Smart Travel Planner Dashboard");
        setSize(1540, 900);
        setMinimumSize(new Dimension(1280, 760));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                weatherReportProvider.stopProvider();
                dispose();
                System.exit(0);
            }
        });
    }

    private void configureLists() {
        CityListCellRenderer renderer = new CityListCellRenderer();

        sortedCityList.setCellRenderer(renderer);
        weatherFilteredCityList.setCellRenderer(renderer);

        sortedCityList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        weatherFilteredCityList.setFont(new Font("SansSerif", Font.PLAIN, 12));

        sortedCityList.setFixedCellHeight(32);
        weatherFilteredCityList.setFixedCellHeight(32);

        sortedCityList.setSelectionBackground(BLUE);
        weatherFilteredCityList.setSelectionBackground(GREEN);

        sortedCityList.setSelectionForeground(Color.WHITE);
        weatherFilteredCityList.setSelectionForeground(Color.WHITE);

        sortedCityList.setBackground(CARD_BACKGROUND);
        weatherFilteredCityList.setBackground(CARD_BACKGROUND);
    }

    private JPanel buildMainPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout(8, 8));
        rootPanel.setBackground(APP_BACKGROUND);
        rootPanel.setBorder(new EmptyBorder(6, 6, 6, 6));

        JPanel topSection = new JPanel(new BorderLayout(10, 10));
        topSection.setOpaque(false);
        topSection.add(buildDashboardHeader(), BorderLayout.NORTH);
        topSection.add(buildControlPanel(), BorderLayout.CENTER);

        rootPanel.add(topSection, BorderLayout.NORTH);

        JSplitPane centerAndPlannerSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildChartsPanel(),
                buildPlannerPanel()
        );
        centerAndPlannerSplit.setResizeWeight(0.60);
        centerAndPlannerSplit.setOneTouchExpandable(true);
        centerAndPlannerSplit.setBorder(null);

        JSplitPane mainSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildCityListsPanel(),
                centerAndPlannerSplit
        );
        mainSplitPane.setResizeWeight(0.37);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setBorder(null);

        rootPanel.add(mainSplitPane, BorderLayout.CENTER);

        lastUpdateLabel.setOpaque(true);
        lastUpdateLabel.setBackground(NAVY);
        lastUpdateLabel.setForeground(Color.WHITE);
        lastUpdateLabel.setBorder(new EmptyBorder(8, 12, 8, 12));
        lastUpdateLabel.setFont(lastUpdateLabel.getFont().deriveFont(Font.BOLD, 12f));

        rootPanel.add(lastUpdateLabel, BorderLayout.SOUTH);

        return rootPanel;
    }

    private JPanel buildDashboardHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(NAVY);
        headerPanel.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel titleLabel = new JLabel("Smart Travel Planner Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel subtitleLabel = new JLabel("Singleton | Strategy | Iterator | Observer | Decorator");
        subtitleLabel.setForeground(new Color(215, 230, 242));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        JLabel badgeLabel = new JLabel("SENG 324 Individual Project");
        badgeLabel.setForeground(Color.WHITE);
        badgeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        badgeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(badgeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel buildControlPanel() {
        JPanel controlPanel = createCardPanel("Dashboard Controls");
        controlPanel.setLayout(new GridBagLayout());

        styleComboBox(sortComboBox);
        styleWeatherComboBox(weatherComboBox);
        styleTextField(searchField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        controlPanel.add(createControlLabel("Sorting criteria"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        controlPanel.add(sortComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        controlPanel.add(createControlLabel("Weather filter"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        controlPanel.add(weatherComboBox, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0;
        controlPanel.add(createControlLabel("Search city"), gbc);

        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        controlPanel.add(searchField, gbc);

        return controlPanel;
    }

    private JPanel buildCityListsPanel() {
        JPanel listsPanel = new JPanel(new GridLayout(2, 1, 12, 12));
        listsPanel.setBackground(APP_BACKGROUND);

        listsPanel.add(createListContainer(
                "All Cities",
                "Strategy Pattern: list order changes by selected sorting algorithm.",
                sortedCityList,
                BLUE
        ));

        listsPanel.add(createListContainer(
                "Weather Filtered Cities",
                "Iterator Pattern: list is generated by the selected weather iterator.",
                weatherFilteredCityList,
                GREEN
        ));

        return listsPanel;
    }

    private JPanel createListContainer(String title, String description, JList<City> list, Color accentColor) {
        JPanel panel = createCardPanel("");
        panel.setLayout(new BorderLayout(8, 8));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(accentColor);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        text.add(titleLabel);
        text.add(descLabel);

        header.add(text, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(new LineBorder(BORDER, 1, true));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        chartsPanel.setBackground(APP_BACKGROUND);

        ChartPanel temperatureChartPanel = new ChartPanel(createTemperatureBarChart());
        ChartPanel pieChartPanel = new ChartPanel(createWeatherPieChart());

        styleChartPanel(temperatureChartPanel);
        styleChartPanel(pieChartPanel);

        temperatureChartPanel.setPreferredSize(new Dimension(540, 300));
        pieChartPanel.setPreferredSize(new Dimension(540, 300));

        chartsPanel.add(wrapChart(
                "Live Temperature Monitor",
                "Observer Pattern: bar chart refreshes after each weather update.",
                temperatureChartPanel
        ));

        chartsPanel.add(wrapChart(
                "Weather Distribution",
                "Observer Pattern: pie chart shows current weather percentages.",
                pieChartPanel
        ));

        return chartsPanel;
    }

    private JPanel wrapChart(String title, String description, ChartPanel chartPanel) {
        JPanel panel = createCardPanel("");
        panel.setLayout(new BorderLayout(8, 8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(PURPLE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 2));
        header.setOpaque(false);
        header.add(titleLabel);
        header.add(descLabel);

        panel.add(header, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JFreeChart createTemperatureBarChart() {
        JFreeChart chart = ChartFactory.createBarChart(
                "Current Temperatures of Cities",
                "City",
                "Temperature (C)",
                temperatureDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new RectangleInsets(8, 8, 8, 8));
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(252, 252, 252));
        plot.setRangeGridlinePaint(new Color(220, 226, 232));
        plot.setDomainGridlinesVisible(false);
        plot.setOutlineVisible(false);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 9));
        domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 11));
        domainAxis.setMaximumCategoryLabelWidthRatio(0.85f);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setCategoryMargin(0.15);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 9));
        rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 11));
        rangeAxis.setAutoRangeIncludesZero(true);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setMaximumBarWidth(0.06);
        renderer.setShadowVisible(false);
        renderer.setDefaultToolTipGenerator(
                new StandardCategoryToolTipGenerator("{1}: {2} C", new DecimalFormat("0.0"))
        );

        return chart;
    }

    private JFreeChart createWeatherPieChart() {
        JFreeChart chart = ChartFactory.createPieChart(
                "Weather Distribution",
                weatherPieDataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new RectangleInsets(8, 8, 8, 8));
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(null);
        plot.setSectionOutlinesVisible(false);
        plot.setInteriorGap(0.05);
        plot.setNoDataMessage("No weather data available");

        return chart;
    }

    private JPanel buildPlannerPanel() {
        JPanel plannerPanel = new JPanel(new BorderLayout(10, 10));
        plannerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        plannerPanel.setBackground(APP_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(NAVY);
        headerPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        headerPanel.setPreferredSize(new Dimension(420, 58));

        selectedCityLabel.setFont(selectedCityLabel.getFont().deriveFont(Font.BOLD, 15f));
        selectedCityLabel.setForeground(Color.WHITE);
        headerPanel.add(selectedCityLabel, BorderLayout.CENTER);

        JPanel activityPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        activityPanel.setBorder(BorderFactory.createTitledBorder("Choose Activities"));
        activityPanel.setPreferredSize(new Dimension(420, 145));
        activityPanel.setBackground(APP_BACKGROUND);

        activityPanel.add(createActivityCard(museumCheckBox, BLUE));
        activityPanel.add(createActivityCard(shoppingMallCheckBox, PURPLE));
        activityPanel.add(createActivityCard(parkCheckBox, GREEN));
        activityPanel.add(createActivityCard(cityCenterCheckBox, ORANGE));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(APP_BACKGROUND);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(activityPanel, BorderLayout.CENTER);

        planDescriptionArea.setEditable(false);
        planDescriptionArea.setLineWrap(false);
        planDescriptionArea.setWrapStyleWord(false);
        planDescriptionArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        planDescriptionArea.setBackground(Color.WHITE);
        planDescriptionArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane planScrollPane = new JScrollPane(planDescriptionArea);
        planScrollPane.setBorder(BorderFactory.createTitledBorder("Generated Travel Itinerary"));
        planScrollPane.setPreferredSize(new Dimension(420, 340));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        summaryPanel.setBackground(APP_BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Trip Summary"));
        summaryPanel.setPreferredSize(new Dimension(420, 78));

        styleSummaryLabel(totalCostLabel, BLUE);
        styleSummaryLabel(totalTimeLabel, GREEN);

        summaryPanel.add(totalCostLabel);
        summaryPanel.add(totalTimeLabel);

        JButton clearButton = new JButton("Reset Activities");
        clearButton.addActionListener(e -> clearPlannerSelections());

        JButton saveButton = new JButton("Save Plan as TXT");
        saveButton.addActionListener(e -> saveCurrentPlanToFile());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(APP_BACKGROUND);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBackground(APP_BACKGROUND);
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        plannerPanel.add(topPanel, BorderLayout.NORTH);
        plannerPanel.add(planScrollPane, BorderLayout.CENTER);
        plannerPanel.add(bottomPanel, BorderLayout.SOUTH);

        return plannerPanel;
    }

    private JPanel createActivityCard(JCheckBox checkBox, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accentColor, 2, true),
                new EmptyBorder(6, 6, 6, 6)
        ));

        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setVerticalAlignment(SwingConstants.TOP);

        card.add(checkBox, BorderLayout.CENTER);

        return card;
    }

    private void styleSummaryLabel(JLabel label, Color accentColor) {
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15f));
        label.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accentColor, 2, true),
                new EmptyBorder(6, 6, 6, 6)
        ));
    }

    private void styleChartPanel(ChartPanel chartPanel) {
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        chartPanel.setMouseWheelEnabled(true);
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        if (title != null && !title.isEmpty()) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(new LineBorder(BORDER, 1, true), title),
                    new EmptyBorder(6, 6, 6, 6)
            ));
        }

        return panel;
    }

    private JLabel createControlLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(NAVY);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(BORDER, 1, true));
    }

    private void styleWeatherComboBox(JComboBox<WeatherState> comboBox) {
        styleComboBox(comboBox);
        comboBox.setRenderer(new WeatherStateComboBoxRenderer());
    }
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void registerEvents() {
        sortComboBox.addActionListener(e -> updateSortedCityList());
        weatherComboBox.addActionListener(e -> updateWeatherFilteredCityList());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSortedCityList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSortedCityList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSortedCityList();
            }
        });

        sortedCityList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updatePlannerPanel();
            }
        });

        weatherFilteredCityList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                City selectedCity = weatherFilteredCityList.getSelectedValue();
                if (selectedCity != null) {
                    selectCityInSortedList(selectedCity);
                }
            }
        });

        museumCheckBox.addActionListener(e -> updatePlannerPanel());
        shoppingMallCheckBox.addActionListener(e -> updatePlannerPanel());
        parkCheckBox.addActionListener(e -> updatePlannerPanel());
        cityCenterCheckBox.addActionListener(e -> updatePlannerPanel());
    }

    private void updateAllViews() {
        updateSortedCityList();
        updateWeatherFilteredCityList();
        updateCharts();
        updatePlannerPanel();
    }

    private void updateSortedCityList() {
        City previouslySelectedCity = sortedCityList.getSelectedValue();
        String previouslySelectedCityName = previouslySelectedCity == null ? null : previouslySelectedCity.getName();

        citySorter.setStrategy(resolveSelectedSortStrategy());

        List<City> visibleCities = applySearchFilter(cityRepository.getCities());
        List<City> sortedCities = citySorter.sortCities(visibleCities);

        sortedCityListModel.clear();

        for (City city : sortedCities) {
            sortedCityListModel.addElement(city);
        }

        restoreSelection(sortedCityList, sortedCityListModel, previouslySelectedCityName);

        if (sortedCityList.getSelectedIndex() == -1 && !sortedCityListModel.isEmpty()) {
            sortedCityList.setSelectedIndex(0);
        }
    }

    private CitySortStrategy resolveSelectedSortStrategy() {
        String selectedSort = (String) sortComboBox.getSelectedItem();

        if ("Sort by Population".equals(selectedSort)) {
            return new PopulationSortStrategy();
        }

        if ("Sort by Area".equals(selectedSort)) {
            return new AreaSortStrategy();
        }

        return new NameSortStrategy();
    }

    private List<City> applySearchFilter(List<City> cities) {
        String searchText = searchField.getText().trim().toLowerCase();
        List<City> result = new ArrayList<>();

        for (City city : cities) {
            if (searchText.isEmpty() || city.getName().toLowerCase().contains(searchText)) {
                result.add(city);
            }
        }

        return result;
    }

    private void updateWeatherFilteredCityList() {
        City previouslySelectedCity = weatherFilteredCityList.getSelectedValue();
        String previouslySelectedCityName = previouslySelectedCity == null ? null : previouslySelectedCity.getName();

        WeatherState selectedWeatherState = (WeatherState) weatherComboBox.getSelectedItem();
        WeatherCityIterator iterator = WeatherIteratorFactory.createIterator(
                selectedWeatherState,
                cityRepository.getCities()
        );

        weatherFilteredCityListModel.clear();

        while (iterator.hasNext()) {
            weatherFilteredCityListModel.addElement(iterator.next());
        }

        restoreSelection(weatherFilteredCityList, weatherFilteredCityListModel, previouslySelectedCityName);
    }

    private void updateCharts() {
        updateTemperatureChart();
        updateWeatherPieChart();
    }

    private void updateTemperatureChart() {
        temperatureDataset.clear();

        for (City city : cityRepository.getCities()) {
            temperatureDataset.addValue(
                    city.getCurrentTemperature(),
                    "Temperature",
                    city.getName()
            );
        }
    }

    private void updateWeatherPieChart() {
        weatherPieDataset.clear();

        int totalCityCount = cityRepository.getCities().size();

        for (WeatherState state : WeatherState.values()) {
            int count = 0;

            for (City city : cityRepository.getCities()) {
                if (city.getCurrentWeatherState() == state) {
                    count++;
                }
            }

            double percentage = totalCityCount == 0 ? 0 : (count * 100.0) / totalCityCount;
            String legendLabel = String.format("%s (%.0f%%)", state.name(), percentage);

            weatherPieDataset.setValue(legendLabel, count);
        }
    }

    private void updatePlannerPanel() {
        City selectedCity = sortedCityList.getSelectedValue();

        if (selectedCity == null) {
            selectedCityLabel.setText("<html><span style='color:white;'><b>No city selected</b></span><br>"
                    + "<span style='color:#DDEAF3;'>Choose a city to build a smart itinerary.</span></html>");
            planDescriptionArea.setText("Select a city from the sorted city list to start building an itinerary.");
            totalCostLabel.setText("<html>Estimated Budget<br><b>0.00 TL</b></html>");
            totalTimeLabel.setText("<html>Estimated Duration<br><b>0.00 hours</b></html>");
            return;
        }

        TravelPlanComponent plan = new BasicCityPlan(selectedCity);
        List<String> selectedActivities = new ArrayList<>();

        if (museumCheckBox.isSelected()) {
            plan = new MuseumVisit(plan);
            selectedActivities.add("Museum Visit              | 350 TL | 2.5 hours");
        }

        if (shoppingMallCheckBox.isSelected()) {
            plan = new ShoppingMallVisit(plan);
            selectedActivities.add("Shopping Mall Visit       | 750 TL | 3.0 hours");
        }

        if (parkCheckBox.isSelected()) {
            plan = new ParkVisit(plan);
            selectedActivities.add("Park Visit                | 150 TL | 1.5 hours");
        }

        if (cityCenterCheckBox.isSelected()) {
            plan = new CityCenterVisit(plan);
            selectedActivities.add("Historic City Center      | 250 TL | 2.0 hours");
        }

        selectedCityLabel.setText(
                "<html><span style='color:white;font-size:13px;'><b>" + selectedCity.getName() + " Travel Plan</b></span><br>"
                        + "<span style='color:#DDEAF3;font-size:9px;'>"
                        + selectedCity.getCurrentWeatherState()
                        + " | " + selectedCity.getCurrentTemperature() + " C"
                        + " | Pop: " + selectedCity.getPopulation()
                        + "</span></html>"
        );

        StringBuilder planText = new StringBuilder();

        planText.append("SMART TRAVEL ITINERARY\n");
        planText.append("============================================================\n");
        planText.append("Destination     : ").append(selectedCity.getName()).append("\n");
        planText.append("Current weather : ").append(selectedCity.getCurrentWeatherState()).append("\n");
        planText.append("Temperature     : ").append(selectedCity.getCurrentTemperature()).append(" C\n");
        planText.append("Population      : ").append(selectedCity.getPopulation()).append("\n");
        planText.append("Area            : ").append(selectedCity.getArea()).append(" km2\n");
        planText.append("============================================================\n\n");

        if (selectedActivities.isEmpty()) {
            planText.append("No activity selected yet.\n");
            planText.append("Use the activity cards above to decorate the base city plan.\n");
        } else {
            planText.append("Selected activities:\n");
            planText.append("------------------------------------------------------------\n");

            for (int i = 0; i < selectedActivities.size(); i++) {
                planText.append(String.format("%02d. %s%n", i + 1, selectedActivities.get(i)));
            }
        }

        planText.append("\n------------------------------------------------------------\n");
        planText.append(String.format("Estimated budget   : %.2f TL%n", plan.getTotalCost()));
        planText.append(String.format("Estimated duration : %.2f hours%n", plan.getTotalTimeInHours()));
        planText.append("------------------------------------------------------------\n\n");

        planText.append("Design Pattern Explanation:\n");
        planText.append("The selected city starts as a BasicCityPlan. Each chosen\n");
        planText.append("activity wraps this base plan using the Decorator Pattern.\n");
        planText.append("Therefore, new planning features are added without modifying\n");
        planText.append("the original City class.");

        planDescriptionArea.setText(planText.toString());
        planDescriptionArea.setCaretPosition(0);

        totalCostLabel.setText(String.format("<html>Estimated Budget<br><b>%.2f TL</b></html>", plan.getTotalCost()));
        totalTimeLabel.setText(String.format("<html>Estimated Duration<br><b>%.2f hours</b></html>", plan.getTotalTimeInHours()));
    }

    private void clearPlannerSelections() {
        museumCheckBox.setSelected(false);
        shoppingMallCheckBox.setSelected(false);
        parkCheckBox.setSelected(false);
        cityCenterCheckBox.setSelected(false);
        updatePlannerPanel();
    }

    private void saveCurrentPlanToFile() {
        City selectedCity = sortedCityList.getSelectedValue();

        if (selectedCity == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a city before saving a plan.",
                    "No City Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(selectedCity.getName() + "-travel-plan.txt"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(planDescriptionArea.getText());
                

                JOptionPane.showMessageDialog(
                        this,
                        "Travel plan saved successfully.",
                        "Plan Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        "Plan could not be saved: " + exception.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private String toPlainText(String htmlText) {
        if (htmlText == null) {
            return "";
        }

        return htmlText
                .replace("<html>", "")
                .replace("</html>", "")
                .replace("<br>", ": ")
                .replace("<b>", "")
                .replace("</b>", "");
    }
    private void restoreSelection(JList<City> list, DefaultListModel<City> model, String cityName) {
        if (cityName == null) {
            return;
        }

        for (int i = 0; i < model.size(); i++) {
            City city = model.getElementAt(i);

            if (city.getName().equals(cityName)) {
                list.setSelectedIndex(i);
                list.ensureIndexIsVisible(i);
                return;
            }
        }
    }

    private void selectCityInSortedList(City selectedCity) {
        if (selectedCity == null) {
            return;
        }

        restoreSelection(sortedCityList, sortedCityListModel, selectedCity.getName());
    }

    @Override
    public void onWeatherUpdated(List<City> updatedCities) {
        SwingUtilities.invokeLater(() -> {
            updateSortedCityList();
            updateWeatherFilteredCityList();
            updateCharts();
            updatePlannerPanel();

            String updateTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            lastUpdateLabel.setText("Last live weather update: " + updateTime
                    + " | Observer pattern refreshed city lists, planner and charts.");
        });
    }

    private static String formatCityForList(City city) {
        String color = weatherColor(city.getCurrentWeatherState());

        return "<html><b>" + city.getName() + "</b>"
                + " &nbsp; <span style='color:" + color + ";'><b>" + city.getCurrentWeatherState() + "</b></span>"
                + " &nbsp; " + city.getCurrentTemperature() + " C"
                + " &nbsp; <span style='color:#667085;'>Pop: " + city.getPopulation()
                + " | Area: " + city.getArea() + " km2</span></html>";
    }

    private static String weatherColor(WeatherState state) {
        switch (state) {
            case SUNNY:
                return "#D89B00";
            case CLOUDY:
                return "#667085";
            case RAINY:
                return "#2274A5";
            case SNOWY:
                return "#2AA7A9";
            default:
                return "#000000";
        }
    }

    private static class WeatherStateComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            Component component = super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus
            );

            if (value instanceof WeatherState) {
                WeatherState state = (WeatherState) value;
                setText(state.name());
                setFont(getFont().deriveFont(Font.BOLD));

                if (!isSelected) {
                    setForeground(Color.decode(weatherColor(state)));
                    setBackground(Color.WHITE);
                } else {
                    setForeground(Color.WHITE);
                }
            }

            return component;
        }
    }
    private static class CityListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            Component component = super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus
            );

            if (value instanceof City) {
                setText(formatCityForList((City) value));
                setBorder(new EmptyBorder(4, 8, 4, 8));
            }

            return component;
        }
    }
}