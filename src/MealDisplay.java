import javax.swing.*;
import java.awt.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.*;
import java.io.*;

public class MealDisplay {
    private static JFrame frame;
    private static JPanel mealPanel;
    private static JTextArea recipeTextArea;
    private static JWindow recipeWindow;

    public static void main(String[] args) {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Meal Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        mealPanel = new JPanel(new BorderLayout());
        frame.add(mealPanel, BorderLayout.CENTER);

        JButton generateMealButton = new JButton("Generate Meal");
        generateMealButton.addActionListener(e -> displayRandomMeal());
        frame.add(generateMealButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize the recipe window
        recipeWindow = new JWindow();
        recipeTextArea = new JTextArea();
        recipeWindow.add(new JScrollPane(recipeTextArea));
        recipeWindow.setSize(400, 300);
        recipeWindow.setLocationRelativeTo(null);
    }

    private static void displayRandomMeal() {
        try {
            // Fetching data from API
            URL url = new URL("https://www.themealdb.com/api/json/v1/1/random.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            // Parsing JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());
            JSONArray mealsArray = (JSONArray) jsonObject.get("meals");
            JSONObject mealObject = (JSONObject) mealsArray.get(0);

            // Extracting meal details
            String mealName = (String) mealObject.get("strMeal");
            String mealThumbUrl = (String) mealObject.get("strMealThumb");
            String recipeInstructions = (String) mealObject.get("strInstructions");

            // Displaying meal details
            JLabel mealNameLabel = new JLabel(mealName);
            ImageIcon mealThumb = new ImageIcon(new URL(mealThumbUrl));
            JLabel mealThumbLabel = new JLabel(mealThumb);
            mealPanel.removeAll(); // Clear previous content
            mealPanel.add(mealNameLabel, BorderLayout.NORTH);
            mealPanel.add(mealThumbLabel, BorderLayout.CENTER);
            mealPanel.revalidate();
            mealPanel.repaint();

            // Displaying recipe in the recipe window
            recipeTextArea.setText(recipeInstructions);
            recipeWindow.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
