import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class TextStatistics implements ActionListener {
    
    // constructors

    // Report Implementation (Sarah's Code)
    static int wordCount(String string){
        //counts the amount of words in given string
        int count = 0;
        count = string.split("\\s").length;
        return count;
    }
    
    static int sentenceCount(String string){
        //counts the amount of words in given string
        int count = 0;
        count = string.split("[!?.:]+").length;
        return count;
    }
    
    static int letterCount(String string){
        int count = 0;
        // traverse the string and check if char is a letter
        for (int i = 0; i < string.length(); i++) {
            
            if(Character.isLetter(string.charAt(i)))
            {
                count++;
            }
        }
        return count;
    }
    
    static int punctCount(String string){
        int count = 0;
        // traverse the string and check if char is not a letter digit or whitspace
        for (int i = 0; i < string.length(); i++) {
            
            if(!Character.isLetter(string.charAt(i)) && !Character.isDigit(string.charAt(i)) && !Character.isWhitespace(string.charAt(i)))
            {
                count++;
            }
        }
        return count;
    }
    
    // (Wen Jie Long)
    static int countStartWord(String string, String match) {
        int count = 0;
        for (String word : string.split("\\s+")) {
            // Split each string into seperate words. \\s+ is for EVERY whitespace.
            if (word.startsWith(match)){
                count ++;
            }
        }
        return count;
    }
    
    static int countStartSentence(String string, String match) {
        int count = 0;
        for (String sentence : string.split("[!?.:]+")) {
            // Split each string into sentences. Assume a period marks the end of each sentence.
            // If that's not acceptable, can replace the code with a BreakIterator class...
            // Sentences are split with space in the front, so need to trim...
            if (sentence.trim().startsWith(match)){
                count ++;
            }
        }
        return count;
    }
    
    static int countEndWord(String string, String match) {
        int count = 0;
        for (String word : string.split("\\s+")) {
            if (word.endsWith(match) || word.endsWith(match + ".")){
                count ++;
            }
        }
        return count;
    }
    
    static int countEndSentence(String string, String match) {
        int count = 0;
        for (String sentence : string.split("[!?.:]+")) {
            if (sentence.endsWith(match)){
                count ++;
            }
        }
        return count;
    }
    
    static int countContains(String string, String match) {
        int count = 0;
        int j;
        // Iterate through string index by index.
        // If searching "aa" in "aaa", this will return 2. Overlap is allowed.
        int length = match.length();
        int diff = string.length() - length;
        for (int i = 0; i < diff; i++) {
            j = 0;
            while (j < length){
                // While loop until it stop matching or seen entire input 
                if (string.charAt(i+j) != match.charAt(j)){
                    break;
                }
                j ++;
            }
            // if j == length, then there's a input
            if (j == length) {
                count ++;
            } 
        }
        return count;
    
    }

    // Filter Report Implementation (Edited Sarah's test code)
    public static String wordSearch(String text, String options, String userInput)  {
        String wordOccur = null;

        // filters words based on options given i.e. begins with, ends with, contains
        if(options.equals("begins"))
        {
            wordOccur = countStartWord(text, userInput) + " words begin with " + userInput;
        }
        if(options.equals("ends"))
        {
            wordOccur = countEndWord(text, userInput) + " words end with " + userInput;
        }
        if(options.equals("contains"))
        {
            wordOccur = countContains(text, userInput) + " words contain " + userInput;
        }
        
        return wordOccur;
    }

    public static String sentenceSearch(String text, String options, String userInput)   {
        String sentenceOccur = null;

        //filters sentences based on options given i.e. begins with, ends with, contains
        if(options.equals("begins"))
        {
            sentenceOccur = countStartSentence(text, userInput) + " sentences begin with " + userInput;
        }
        if(options.equals("ends"))
        {
            sentenceOccur = countEndSentence(text, userInput) + " sentences end with " + userInput;
        }
        if(options.equals("contains"))
        {
            sentenceOccur = countContains(text, userInput) + " sentences contain " + userInput;
        }
        
        return sentenceOccur;
    }

    // GUI Implementation (Huong's Code)
    // global components
    private JPanel panel_report, panel_display;
    private JButton button_text1, button_text2, button_text3, button_search, button_input, button_update, button_upload;
    private JLabel label_words, label_sentences, label_letters, label_wordCount, label_sentenceCount, label_letterCount, label_punctCount, label_wordOccur, label_sentenceOccur;
    private JTextArea display;
    private JTextField tf_search;
    private JScrollPane scroll;
    private JCheckBox cb_begins, cb_ends, cb_contain;
    private ButtonGroup filters;
    private Highlighter highlight;

    // scans in and outputs text from given text file
    static String readTexts(String userInput) {
        FileReader reader;
        String textOut = "";
        try {
            reader = new FileReader(userInput);
            Scanner scnr = new Scanner(reader);
            textOut = scnr.nextLine();

            reader.close();
        } catch (FileNotFoundException e)  {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return textOut;
    }

    // reset report label so data displays neatly
    public void AlignDataDisplay()   {
        label_words.setText("Words: ");
        label_sentences.setText("Sentences: ");
        label_letters.setText("Letters: ");
    }

    // displays data from given report neatly below each other
    public void DisplayReport() {
        label_wordCount.setText(String.valueOf(wordCount(display.getText())) + "          ");
        label_sentenceCount.setText(String.valueOf(sentenceCount(display.getText())) + "          ");
        label_letterCount.setText(String.valueOf(letterCount(display.getText())) + "          ");
        label_punctCount.setText(String.valueOf(punctCount(display.getText())));
    }

    // hide search labels if there is no input for search
    public void NoInput()   {
        if (tf_search.getText().equals(""))   {
            label_wordOccur.setText(null);
            label_sentenceOccur.setText(null);
        }
    }

    // highlight words/sentences the user is searching for with filters
    public void Select () {
        highlight = display.getHighlighter();
        highlight.removeAllHighlights();
        String searchFor = tf_search.getText();
       try {
        String text = display.getText(0, display.getText().length());
        int position = 0;

        // highlight all the words/sentences in the text based on the filter selected
        if (cb_begins.isSelected())   {
            for (String word : text.split("\\s+")) {
                // Split each string into seperate words. \\s+ is for EVERY whitespace.
                if (word.toUpperCase().startsWith(searchFor.toUpperCase())){
                    highlight.addHighlight(position, position + searchFor.length(), DefaultHighlighter.DefaultPainter);
                }
                position += word.length() + 1;
            }
        }
        else if (cb_ends.isSelected())    {
            for (String word : text.split("\\s+")) {
                // Split each string into seperate words. \\s+ is for EVERY whitespace.
                if (Character.isLetter(word.charAt(word.length() - 1)) == false) {
                    // removes all punctuation
                    word = word.substring(0, word.length() - 1);
                }

                if (word.toUpperCase().endsWith(searchFor.toUpperCase())){
                    // makes everything uppercase and highlight words searching for
                    int n = text.indexOf(word, position) + word.length();
                    highlight.addHighlight(n - searchFor.length(), n, DefaultHighlighter.DefaultPainter);
                }
                // search through the whole text
                position += word.length() + 1;
            }
        }
        else    {
            while(((position = text.toUpperCase().indexOf(searchFor.toUpperCase(), position)) >= 0))   {
                // search through text and highlight everything that matches searchFor word
                highlight.addHighlight(position, position + searchFor.length(), DefaultHighlighter.DefaultPainter);
                position += searchFor.length();
            }
        }   
       } catch (Exception e) {
        e.printStackTrace();
        }
    }

    public TextStatistics() 
    {
        // set UI texts to the same font and size

        UIManager.put("Button.font", new Font("Times New Roman", Font.PLAIN, 20));
        UIManager.put("Label.font", new Font("Times New Roman", Font.PLAIN, 20));
        UIManager.put("CheckBox.font", new Font("Times New Roman", Font.PLAIN, 20));

        // creates selectable check box

        cb_begins = new JCheckBox("begins with");
        cb_begins.setFocusable(false);
        cb_ends = new JCheckBox("ends with");
        cb_ends.setFocusable(false);
        cb_contain = new JCheckBox("contains");
        cb_contain.setFocusable(false);
        filters = new ButtonGroup();            // only one check box selected at a time
        filters.add(cb_begins);
        filters.add(cb_ends);
        filters.add(cb_contain);
        
        // creates text field to read input

        tf_search = new JTextField();
        tf_search.setPreferredSize(new Dimension(335, 30));
        tf_search.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        
        // creates text box to display scanned text

        display = new JTextArea();
        display.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setEditable(false);
        // add scrolling feature to text box
        scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(445,290));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // creates clickable button to perform action

        button_text1 = new JButton();
        button_text1.setText("Text 1");
        button_text1.setFocusable(false);
        button_text1.addActionListener(this);

        button_text2 = new JButton();
        button_text2.setText("Text 2");
        button_text2.setFocusable(false);
        button_text2.addActionListener(this);
        
        button_text3 = new JButton();
        button_text3.setText("Text 3");
        button_text3.setFocusable(false);
        button_text3.addActionListener(this);

        button_input = new JButton();
        button_input.setText("Input");
        button_input.setFocusable(false);
        button_input.addActionListener(this);

        button_update = new JButton();
        button_update.setText("Get report");
        button_update.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        button_update.setFocusable(false);
        button_update.addActionListener(this);
        button_update.setVisible(false);

        button_upload = new JButton();
        button_upload.setText("Upload");
        button_upload.setFocusable(false);
        button_upload.addActionListener(this);

        button_search = new JButton();
        button_search.setText("Search");
        button_search.setFocusable(false);
        button_search.setPreferredSize(new Dimension(100, 30));
        button_search.addActionListener(this);

        // creates display areas for a string of text and/or an image

        JLabel label_select = new JLabel();
        label_select.setText("Select One of the Following: ");

        label_words = new JLabel();
        label_words.setText("Words:          ");
        label_wordCount = new JLabel();
        label_wordCount.setText("     ");
        label_sentences = new JLabel();
        label_sentences.setText("Sentences:          ");
        label_sentenceCount = new JLabel();
        label_sentenceCount.setText("     ");
        label_letters = new JLabel();
        label_letters.setText("Letters:          ");
        label_letterCount = new JLabel();
        label_letterCount.setText("     ");
        JLabel label_punctuations = new JLabel();
        label_punctuations.setText("Punctuation Signs: ");
        label_punctCount = new JLabel();
        label_punctCount.setText("     ");
        label_wordOccur = new JLabel();
        label_sentenceOccur = new JLabel();

        JLabel label_text = new JLabel();
        label_text.setText("Text: ");
        JLabel label_filter = new JLabel();
        label_filter.setText("Search filters: ");

        // creates panels to contain other components
        
        JPanel panel_selection = new JPanel();
        panel_selection.setBounds(0, 0, 750, 50);
        panel_selection.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel_selection.add(label_select);
        panel_selection.add(button_text1);
        panel_selection.add(button_text2);
        panel_selection.add(button_text3);
        panel_selection.add(button_input);
        panel_selection.add(button_upload);

        panel_report = new JPanel();
        panel_report.setBounds(0, 50, 220, 450);
        panel_report.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel_report.add(label_words);
        panel_report.add(label_wordCount);
        panel_report.add(label_sentences);
        panel_report.add(label_sentenceCount);
        panel_report.add(label_letters);
        panel_report.add(label_letterCount);
        panel_report.add(label_punctuations);
        panel_report.add(label_punctCount);
        panel_report.add(label_wordOccur);
        panel_report.add(label_sentenceOccur);

        panel_display = new JPanel();
        panel_display.setBounds(237, 50, 475, 450);
        panel_display.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel_display.add(label_text);
        panel_display.add(button_update);
        panel_display.add(scroll);
        panel_display.add(label_filter);
        panel_display.add(cb_begins);
        panel_display.add(cb_ends);
        panel_display.add(cb_contain);
        panel_display.add(tf_search);
        panel_display.add(button_search);

        // creates GUI window

        JFrame frame = new JFrame();
        frame.setTitle("Text Statistics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       // exit window
        //frame.setResizable(false);         // cannot resize window
        frame.setLayout(null);
        frame.setSize(710, 500);        // dimension of frame
        frame.setLocationRelativeTo(null);         // centers GUI window
        frame.add(panel_selection);
        frame.add(panel_report);
        frame.add(panel_display);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) 
    {
        // button functions
        if(e.getSource() == button_text1) {
            button_text1.setBackground(Color.LIGHT_GRAY);       // show that text is selected
            display.setText(readTexts("SampleText1.txt"));
            button_update.setVisible(false);
            display.setEditable(false);                       // makes textarea uneditable

            AlignDataDisplay();
            DisplayReport();
        }
        else    {
            button_text1.setBackground(null);
        }
        
        if (e.getSource() == button_text2) {
            button_text2.setBackground(Color.LIGHT_GRAY);       // show that text is selected
            display.setText(readTexts("SampleText2.txt"));
            button_update.setVisible(false);
            display.setEditable(false);                       // makes textarea uneditable

            AlignDataDisplay();
            DisplayReport();
        }
        else {
            button_text2.setBackground(null);
        }
        
        if (e.getSource() == button_text3) {
            button_text3.setBackground(Color.LIGHT_GRAY);       // show that text is selected
            display.setText(readTexts("SampleText3.txt"));
            button_update.setVisible(false);
            display.setEditable(false);                       // makes textarea uneditable

            AlignDataDisplay();
            DisplayReport();
        }
        else {
            button_text3.setBackground(null);
        }
        
        if (e.getSource() == button_input)  {
            button_input.setBackground(Color.LIGHT_GRAY);       // show that text is selected
            display.setText(null);
            display.setEditable(true);                        // makes text edidable
            button_update.setText("Get report");
            button_update.setVisible(true);               // make update button visible

            AlignDataDisplay();

            // clear all data for input text
            label_wordCount.setText("           ");
            label_sentenceCount.setText("           ");
            label_letterCount.setText("           ");
            label_punctCount.setText(null);
        }
        else {
            button_input.setBackground(null);
        }

        if (e.getSource() == button_update)  {
            // gets report when button is clicked
            readTexts(display.getText());

            AlignDataDisplay();
            DisplayReport();
        }
        
        // option to upload text file from user device

        if (e.getSource() == button_upload)  {
            button_upload.setBackground(Color.LIGHT_GRAY);      // show that text is selected
            display.setText(null);
            display.setEditable(true);                        // makes text edidable
            button_update.setVisible(true);               // make update button visible
            button_update.setText("Update");
            
            // add text file uploader from user's device
            JFileChooser file_upload = new JFileChooser();
            int log = file_upload.showSaveDialog(null);
            
            if (log == JFileChooser.APPROVE_OPTION)    {
                File file = new File(file_upload.getSelectedFile().getAbsolutePath());
                String path = file.getPath();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(path));
                    String line = "", textFile = "";

                    while((line = reader.readLine()) != null)   {
                        textFile += line + "\n";
                    }

                    display.setText(textFile);
                    reader.close();
                    DisplayReport();
                }
                catch (Exception ex)   {
                    ex.printStackTrace();
                }
            }
        }
        else {
            button_upload.setBackground(null);
        }

        // search selected text

        if (e.getSource() == (button_search)) {
            // runs word search with filter function to find selected word(s) and highlight it for easy viewing
            if (!tf_search.getText().equals(""))   {
                Select();
            }
            else    {
                highlight.removeAllHighlights();
            }

            if (cb_begins.isSelected()) { 
                label_wordOccur.setText(wordSearch(display.getText(), "begins", tf_search.getText()));
                label_sentenceOccur.setText(sentenceSearch(display.getText(), "begins", tf_search.getText()));
                panel_report.add(label_wordOccur);
                panel_report.add(label_sentenceOccur);

                NoInput();
            }
            
            if (cb_ends.isSelected()) {
                label_wordOccur.setText(wordSearch(display.getText(), "ends", tf_search.getText()));
                label_sentenceOccur.setText(sentenceSearch(display.getText(), "ends", tf_search.getText()));
                panel_report.add(label_wordOccur);
                panel_report.add(label_sentenceOccur);

                NoInput();
            }
            
            if (cb_contain.isSelected()) {
                label_wordOccur.setText(wordSearch(display.getText(), "contains", tf_search.getText()));
                label_sentenceOccur.setText(null);
                panel_report.add(label_wordOccur);

                NoInput();
            }
        }
    }

    public static void main(String[] args) {
        new TextStatistics();
    }
}