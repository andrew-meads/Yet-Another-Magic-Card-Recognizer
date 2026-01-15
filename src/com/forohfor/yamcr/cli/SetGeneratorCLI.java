package com.forohfor.yamcr.cli;

import com.forohfor.yamcr.config.SavedConfig;
import com.forohfor.yamcr.generator.SetGenerator;
import com.forohfor.yamcr.service.ConsoleProgressTracker;
import com.forohfor.yamcr.service.ProgressTracker;
import forohfor.scryfall.api.MTGCardQuery;
import forohfor.scryfall.api.Set;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Command-line interface for generating MTG card set recognition databases.
 * 
 * Usage:
 *   java -jar SetGeneratorCLI.jar <set-code-1> <set-code-2> ...
 *   java -jar SetGeneratorCLI.jar --all
 *   java -jar SetGeneratorCLI.jar --type expansion
 * 
 * Examples:
 *   java -jar SetGeneratorCLI.jar DOM MID NEO
 *   java -jar SetGeneratorCLI.jar --type core
 *   java -jar SetGeneratorCLI.jar --all
 */
public class SetGeneratorCLI {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }
        
        try {
            // Initialize configuration in headless mode
            SavedConfig.initHeadless("config.json");
            System.out.println("Configuration loaded from config.json");
            System.out.println("Set data path: " + SavedConfig.PATH);
            System.out.println();
            
            // Create console-based progress tracker
            ProgressTracker tracker = new ConsoleProgressTracker();
            
            // Handle different command modes
            if (args[0].equals("--all")) {
                // Generate all sets
                System.out.println("Generating all sets...");
                SetGenerator.bulkGenSets("all", tracker);
            } 
            else if (args[0].equals("--type")) {
                // Generate sets by type
                if (args.length < 2) {
                    System.err.println("Error: --type requires a set type argument");
                    printUsage();
                    System.exit(1);
                }
                String setType = args[1];
                System.out.println("Generating sets of type: " + setType);
                SetGenerator.bulkGenSets(setType, tracker);
            } 
            else {
                // Generate specific sets by code
                ArrayList<Set> allSets = MTGCardQuery.getSets();
                ArrayList<Set> toGenerate = new ArrayList<>();
                
                for (String code : args) {
                    Set foundSet = findSetByCode(allSets, code);
                    if (foundSet != null) {
                        toGenerate.add(foundSet);
                        System.out.println("Found set: " + foundSet.getName() + " (" + code.toUpperCase() + ")");
                    } else {
                        System.err.println("Warning: Set code '" + code + "' not found");
                    }
                }
                
                if (toGenerate.isEmpty()) {
                    System.err.println("Error: No valid sets found");
                    System.exit(1);
                }
                
                System.out.println();
                SetGenerator.generateSets(toGenerate, tracker);
            }
            
            System.out.println();
            System.out.println("Set generation complete!");
            System.exit(0);
            
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Error: Config file not found - " + e.getMessage());
            System.err.println("Please create a config.json file with the required settings.");
            System.err.println("Example config.json:");
            System.err.println("{");
            System.err.println("  \"path\": \"/path/to/set/data/\",");
            System.err.println("  \"debug\": false,");
            System.err.println("  \"write_basics_to_sets\": false");
            System.err.println("}");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static Set findSetByCode(ArrayList<Set> allSets, String code) {
        for (Set set : allSets) {
            if (set.getCode().equalsIgnoreCase(code)) {
                return set;
            }
        }
        return null;
    }
    
    private static void printUsage() {
        System.out.println("MTG Card Set Generator - Command Line Interface");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar SetGeneratorCLI.jar <set-code-1> <set-code-2> ...");
        System.out.println("  java -jar SetGeneratorCLI.jar --all");
        System.out.println("  java -jar SetGeneratorCLI.jar --type <set-type>");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  <set-code>     Three-letter set code (e.g., DOM, MID, NEO)");
        System.out.println("  --all          Generate all available sets");
        System.out.println("  --type <type>  Generate sets of a specific type");
        System.out.println();
        System.out.println("Available set types:");
        System.out.println("  " + String.join(", ", Arrays.copyOfRange(SetGenerator.SET_TYPES, 0, 
                                Math.min(10, SetGenerator.SET_TYPES.length))));
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar SetGeneratorCLI.jar DOM MID NEO");
        System.out.println("  java -jar SetGeneratorCLI.jar --type expansion");
        System.out.println("  java -jar SetGeneratorCLI.jar --all");
        System.out.println();
        System.out.println("Note: Requires config.json file in the current directory");
    }
}
