import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List; // <-- Added the missing import for List
import java.util.stream.Collectors;

/**
 * An Artificial Intelligence Chatbot implemented using rule-based logic
 * and basic Natural Language Processing (NLP) techniques for keyword matching.
 * This class includes the main method and is fully self-contained for
 * execution on an online Java compiler.
 */
public class AIChatbot {

    // The knowledge base: A map of core keywords (keys) to predefined responses (values).
    private static final Map<String, String> KNOWLEDGE_BASE = new HashMap<>();

    /**
     * Initializes the chatbot's knowledge base (FAQ).
     */
    private static void initializeKnowledgeBase() {
        // Greetings
        KNOWLEDGE_BASE.put("hello", "Hello! I am a simple Java chatbot. How can I help you?");
        KNOWLEDGE_BASE.put("hi", "Hi there! Feel free to ask me anything about my knowledge base.");
        KNOWLEDGE_BASE.put("hey", "Hey! Ready to chat? What's on your mind?");

        // Status/Wellbeing
        KNOWLEDGE_BASE.put("how are you", "I am a Java program, so I am always running perfectly! How about you?");
        KNOWLEDGE_BASE.put("status", "System nominal. I am running on a JVM.");

        // Java Topic FAQs
        KNOWLEDGE_BASE.put("java", "Java is a robust, object-oriented programming language designed for versatility.");
        KNOWLEDGE_BASE.put("oop", "OOP stands for Object-Oriented Programming, a paradigm based on the concept of 'objects'.");
        KNOWLEDGE_BASE.put("compiler", "A compiler translates your human-readable source code into machine code or bytecode (like Java's .class files).");
        KNOWLEDGE_BASE.put("class", "In Java, a class is a blueprint for creating objects, defining their data and behavior.");
        
        // General Inquiries
        KNOWLEDGE_BASE.put("time", "I don't track the current time, but I can answer your questions about Java and development.");
        KNOWLEDGE_BASE.put("thank", "You're very welcome! I'm here to help.");
    }

    /**
     * Simulates basic Natural Language Processing (NLP) to process user input.
     * 1. Converts input to lowercase.
     * 2. Removes punctuation and special characters.
     * 3. Tokenizes (splits into words).
     * @param input The raw string input from the user.
     * @return A list of cleaned tokens (words).
     */
    private static List<String> preprocessInput(String input) {
        // Convert to lowercase
        String cleaned = input.toLowerCase();
        
        // Remove all characters that are not letters or spaces
        cleaned = cleaned.replaceAll("[^a-z\\s]", "");
        
        // Split into tokens (words) and filter out empty strings
        return Arrays.stream(cleaned.split("\\s+"))
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toList());
    }

    /**
     * Determines the appropriate response based on the processed user tokens.
     * This uses a rule-based approach for keyword matching.
     * @param tokens The list of cleaned words from the user's input.
     * @return The chatbot's response string.
     */
    private static String getResponse(List<String> tokens) {
        
        // Simple Rule 1: Direct Keyword Match
        for (String token : tokens) {
            if (KNOWLEDGE_BASE.containsKey(token)) {
                return KNOWLEDGE_BASE.get(token);
            }
        }
        
        // Simple Rule 2: Multi-word phrase matching (e.g., "how are you")
        String fullInput = String.join(" ", tokens);
        if (KNOWLEDGE_BASE.containsKey(fullInput)) {
             return KNOWLEDGE_BASE.get(fullInput);
        }

        // Simple Rule 3: Search for keywords in the response keys
        for (String key : KNOWLEDGE_BASE.keySet()) {
            if (fullInput.contains(key) && key.contains(" ")) {
                // This catches phrases that might not be exact matches but contain the key
                return KNOWLEDGE_BASE.get(key);
            }
        }

        // Fallback Rule
        return "I'm sorry, I don't have a specific answer for that. Try asking about 'Java', 'OOP', or say 'hello'.";
    }

    /**
     * Main method to run the interactive console chatbot.
     */
    public static void main(String[] args) {
        initializeKnowledgeBase();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("   🤖 Java Rule-Based Chatbot 🤖");
        System.out.println("=====================================");
        System.out.println("Bot: Hello! I am a simple Java chatbot. Type 'quit' to exit.");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("exit")) {
                System.out.println("Bot: Goodbye! Have a great day.");
                break;
            }

            // 1. NLP Processing: Clean and tokenize the input
            List<String> tokens = preprocessInput(userInput);
            
            if (tokens.isEmpty()) {
                System.out.println("Bot: Please say something!");
                continue;
            }

            // 2. Logic: Get the response based on tokens
            String response = getResponse(tokens);

            // 3. Output
            System.out.println("Bot: " + response);
        }

        scanner.close();
    }
}
