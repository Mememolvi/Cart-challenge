package com.shopping.cart.challange.db;

import com.shopping.cart.challange.db.cart.item.CartItemEntry;
import com.shopping.cart.challange.db.cart.item.Item;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class for managing shopping cart data using an in-memory HashMap
 * with Append-Only File (AOF) persistence.
 */
@Slf4j
@Service
public class CartRepository {
    private static Map<Item, CartItemEntry> database = new ConcurrentHashMap<>();

    @Value("${cart.aof.file:/home/neo/Documents/cart_data.aof}")
    private String aofFile; // Injected from application.properties

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Loads data from the AOF file on initialization.
     * If the file doesn't exist, creates an empty one.
     * If it exists, reads and reconstructs the cart state.
     */
    @PostConstruct
    void loadDataFromFile() {
        log.info("Loading data from AOF file");
        File file = new File(aofFile);
        if (!file.exists()) {
            try {
                log.info("AOF file not found, creating empty file");
                file.createNewFile(); // Create empty file if it doesn't exist
                log.info("AOF file created");
            } catch (IOException e) {
                log.error("Error creating AOF file: " + e.getMessage());
            }
            return;
        }

        // Read data from file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2); // Split into item name and timestamp
                if (parts.length == 2) {
                    Item item = Item.valueOf(parts[0].toUpperCase()); // Convert to enum
                    LocalDateTime timestamp = LocalDateTime.parse(parts[1], TIMESTAMP_FORMATTER);
                    log.info("Replaying item: {}, timestamp: {}", item, timestamp);
                    addItem(item, timestamp, false);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading AOF file: " + e.getMessage());
        }
    }

    /**
     * Adds an item to the cart and appends the action to the AOF file.
     *
     * @param item the Item to add
     * @return success message
     */
    private String addItem(Item item, LocalDateTime timestamp, boolean writeToFile) {
        log.info("Adding item: {}, timestamp: {}", item, timestamp);
        CartItemEntry entry = database.get(item);
        LocalDateTime now = timestamp;
        if (entry == null) {
            entry = new CartItemEntry();
            entry.setNetQuantity(1);
            entry.setInsertionTimestamps(new ArrayList<>(List.of(now)));
            database.put(item, entry);
        } else {
            entry.setNetQuantity(entry.getNetQuantity() + 1);
            entry.getInsertionTimestamps().add(now);
        }

        // Write to AOF [Skipped when replaying]
        if (writeToFile) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(aofFile, true))) {
                String line = item.name() + " " + now.format(TIMESTAMP_FORMATTER);
                writer.write(line);
                log.info("Appended to AOF file: {}", line);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error appending to AOF file: " + e.getMessage());
                return "Item added but failed to persist: " + e.getMessage();
            }
        }
        return "Item added successfully";
    }

    public void addItem(Item item) {
        addItem(item, LocalDateTime.now(), true);
    }

    /**
     * Retrieves all items in the cart.
     *
     * @return Map of Item to CartItemEntry
     */
    public Map<Item, CartItemEntry> getAllItems() {
        return new HashMap<>(database);
    }

    /**
     * Calculates the total cost of all items in the cart.
     *
     * @return the total price in pounds
     */
    public double calculateTotal() {
        double total = 0.0;
        for (Map.Entry<Item, CartItemEntry> entry : database.entrySet()) {
            Integer quantity = entry.getValue().getNetQuantity();
            if (quantity != null) {
                total += entry.getKey().calculatePrice(quantity);
            }
        }
        return total;
    }
}