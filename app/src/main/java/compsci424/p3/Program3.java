package compsci424.p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Program3 {
    static int[] available;
    static int[][] max;
    static int[][] allocation;
    static int[][] need;
    static int numResources;
    static int numProcesses;
    static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Right click 'src' and then click 'Open in Integrated Terminal'");
            System.err.println("Usage: java main/java/compsci424/p3/Program3.java <mode> <path_to_setup_file>");
            return;
        }

        String mode = args[0];
        String setupFilePath = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(setupFilePath))) {
            parseSetupFile(reader);
        } catch (FileNotFoundException e) {
            System.err.println("Setup file not found: " + setupFilePath);
            return;
        } catch (IOException e) {
            System.err.println("Error reading setup file: " + e.getMessage());
            return;
        }

        initializeNeedMatrix();

        if (!isSystemSafe()) {
            System.err.println("System starts in an unsafe state, exiting.");
            return;
        }

        if (mode.equalsIgnoreCase("auto")) {
            runAutomaticMode();
        } else if (mode.equalsIgnoreCase("manual")) {
            runManualMode();
        } else {
            System.err.println("Invalid mode. Use 'auto' or 'manual'.");
        }
    }

    private static void parseSetupFile(BufferedReader reader) throws IOException {
        numResources = Integer.parseInt(reader.readLine().trim().split(" ")[0]);
        numProcesses = Integer.parseInt(reader.readLine().trim().split(" ")[0]);
        available = new int[numResources];
        max = new int[numProcesses][numResources];
        allocation = new int[numProcesses][numResources];

        reader.readLine();  
        String[] availResources = reader.readLine().split("\\s+");
        for (int i = 0; i < numResources; i++) {
            available[i] = Integer.parseInt(availResources[i]);
        }

        reader.readLine();  
        for (int i = 0; i < numProcesses; i++) {
            String[] maxResources = reader.readLine().split("\\s+");
            for (int j = 0; j < numResources; j++) {
                max[i][j] = Integer.parseInt(maxResources[j]);
            }
        }

        reader.readLine();  
        for (int i = 0; i < numProcesses; i++) {
            String[] allocResources = reader.readLine().split("\\s+");
            for (int j = 0; j < numResources; j++) {
                allocation[i][j] = Integer.parseInt(allocResources[j]);
            }
        }
    }

    private static void initializeNeedMatrix() {
        need = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    private static boolean isSystemSafe() {
        int[] work = available.clone();
        boolean[] finish = new boolean[numProcesses];
        boolean progress;

        do {
            progress = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i] && canProcessFinish(i, work)) {
                    for (int j = 0; j < numResources; j++) {
                        work[j] += allocation[i][j];
                    }
                    finish[i] = true;
                    progress = true;
                }
            }
        } while (progress);

        for (boolean f : finish) {
            if (!f) return false;
        }
        return true;
    }

    private static boolean canProcessFinish(int process, int[] work) {
        for (int j = 0; j < numResources; j++) {
            if (need[process][j] > work[j]) return false;
        }
        return true;
    }

    private static void runAutomaticMode() {
        Thread[] threads = new Thread[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            final int processId = i;
            threads[i] = new Thread(() -> processSimulation(processId));
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread was interrupted, failed to complete execution");
            }
        }
    }

    private static void processSimulation(int processId) {
        Random random = new Random();
        try {
            for (int i = 0; i < 3; i++) {  
                int resourceType = random.nextInt(numResources);
                int requestAmount = random.nextInt(need[processId][resourceType] + 1);
                if (requestResources(processId, resourceType, requestAmount)) {
                    Thread.sleep(random.nextInt(1000)); 
                    int releaseAmount = random.nextInt(requestAmount + 1);
                    releaseResources(processId, resourceType, releaseAmount);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Process " + processId + " was interrupted during execution.");
        }
    }

    private static boolean requestResources(int processId, int resourceId, int amount) {
        if (amount > need[processId][resourceId]) {
            System.out.println("Process " + processId + " requests " + amount + " units of resource " + resourceId + ": denied (exceeds maximum claim)");
            return false;
        }
        if (amount > available[resourceId]) {
            System.out.println("Process " + processId + " requests " + amount + " units of resource " + resourceId + ": denied (resources not available)");
            return false;
        }
        allocation[processId][resourceId] += amount;
        need[processId][resourceId] -= amount;
        available[resourceId] -= amount;

        if (isSystemSafe()) {
            System.out.println("Process " + processId + " requests " + amount + " units of resource " + resourceId + ": granted");
            return true;
        } else {
            allocation[processId][resourceId] -= amount;
            need[processId][resourceId] += amount;
            available[resourceId] += amount;
            System.out.println("Process " + processId + " requests " + amount + " units of resource " + resourceId + ": denied (system would be left in unsafe state)");
            return false;
        }
    }

    private static void releaseResources(int processId, int resourceId, int amount) {
        if (amount > allocation[processId][resourceId]) {
            System.out.println("Process " + processId + " releases " + amount + " units of resource " + resourceId + ": denied (release amount exceeds current allocation)");
        } else {
            allocation[processId][resourceId] -= amount;
            need[processId][resourceId] += amount;
            available[resourceId] += amount;
            System.out.println("Process " + processId + " releases " + amount + " units of resource " + resourceId + ": completed");
        }
    }

    private static void runManualMode() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter command ('request', 'release', 'end'):");
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");

            if (parts[0].equalsIgnoreCase("end")) {
                break;
            } else if (parts.length == 6 && (parts[0].equalsIgnoreCase("request") || parts[0].equalsIgnoreCase("release"))) {
                try {
                    String commandType = parts[0];
                    int amount = Integer.parseInt(parts[1]);
                    String ofWord = parts[2];
                    int resourceId = Integer.parseInt(parts[3]);
                    String forKey = parts[4];
                    int processId = Integer.parseInt(parts[5]);

                    if (!ofWord.equals("of") || !forKey.equals("for")) {
                        System.out.println("Invalid format. Please follow the example: 'request 3 of 1 for 0'");
                        continue;
                    }

                    if (commandType.equalsIgnoreCase("request")) {
                        if (requestResources(processId, resourceId, amount)) {
                            System.out.println("Request granted.");
                        } else {
                            System.out.println("Request denied.");
                        }
                    } else if (commandType.equalsIgnoreCase("release")) {
                        releaseResources(processId, resourceId, amount);
                        System.out.println("Resource released.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Make sure all numbers are correctly formatted.");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Incomplete command. Please provide all necessary parameters.");
                }
            } else {
                System.out.println("Invalid command. Please use 'request', 'release', or 'end'.");
            }
        }
        scanner.close();
    }
}
