/*
 * File Name: SeaPortProgram.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: This class will define and generate the GUI components and instantiate a World object.
 */
package seaport_park;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class SeaPortProgram extends JFrame {

    private World world;
    private File file;

    private Dimension screenSize;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel displayPanel;

    private JButton loadButton;
    private JButton sortButton;

    private JTextArea textBox;
    private JScrollPane scrollPane;
    private JPanel treePanel;
    private JPanel jobPanel;
    private JPanel progressPanel;
    private JScrollPane jobPane;

    private JPanel resourcePanel;
    private JPanel jobUpdatePanel;
    private JScrollPane resourcePane;
    private JScrollPane updatePane;
    private JTextArea resourceText;
    private JTextArea progressText;

    private JTextField searchField;
    private JButton searchBtn;

    private JTree tree;

    private JComboBox<String> searchCombo;
    private JComboBox<String> sortObj;
    private JComboBox<String> sortCombo;

    private JLabel blank;

    private HashMap<Integer, Thing> fullMap;
    private HashMap<Integer, Job> jobMap;

    public boolean isRunning;
    public boolean fileReady;

    //Constructor
    public SeaPortProgram() {
        buildGUI();
    }

    //Choose file
    private void readFile() {
        textBox.append("File load request received.\n\n");
        JFileChooser jfc = new JFileChooser(".");
        int returnVal = jfc.showOpenDialog(displayPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
        }
    }

    //This method will read the provided file line by line and instantiate the objects
    //as indicated by the data file. The objects are added to respective Hashmap to be able to
    //link the different object classes together.
    private void loadWorld(File f) throws FileNotFoundException, IOException {
        //This is to clear the threads from the old file.
        if (world != null) {
            for (SeaPort sp : world.getPorts()) {
                for (Ship sh : sp.getShip()) {
                    for (Job j : sh.getJob()) {
                        j.endJob();
                    }
                }
            }
        }

        BufferedReader selectedFile = new BufferedReader(new FileReader(f));

        Scanner sc = new Scanner(selectedFile);
        world = new World(sc);

        //local instances of hash map
        HashMap<Integer, SeaPort> portMap = new HashMap<>();
        HashMap<Integer, Dock> dockMap = new HashMap<>();
        HashMap<Integer, Ship> shipMap = new HashMap<>();
        jobMap = new HashMap<>();
        fullMap = new HashMap<>(); //using fullMap to add every objects by index to HashMap for searching by index.

        while (sc.hasNextLine()) {

            String str = sc.nextLine().trim();
            if (str.length() == 0) {
                continue;
            }
            Scanner line = new Scanner(str);
            switch (line.next()) {
                case "port":
                    SeaPort sp = new SeaPort(line);
                    portMap.put(sp.getIndex(), sp);
                    fullMap.put(sp.getIndex(), sp);
                    world.assignPort(sp);
                    break;
                case "dock":
                    Dock d = new Dock(line);
                    dockMap.put(d.getIndex(), d);
                    fullMap.put(d.getIndex(), d);
                    world.assignDock(portMap.get(d.getParent()), d);
                    break;
                //New for Project 3. Need to add the Dock/Port information of the ship
                //This way jobs can be properly reassigned to ships in queue
                //Since ships in queue have a port number not a dock number    
                case "pship":
                    Ship ps = new PassengerShip(line, dockMap, portMap);
                    SeaPort p = portMap.get(ps.getParent()); //for ships in queue
                    Dock pd = dockMap.get(ps.getParent());
                    if (p == null) { //if ship is docked, dock parent is found to link with port
                        p = portMap.get(pd.getParent());
                    }
                    fullMap.put(ps.getIndex(), ps);
                    shipMap.put(ps.getIndex(), ps);
                    world.assignShip(p, pd, ps);
                    break;
                case "cship":
                    Ship cs = new CargoShip(line, dockMap, portMap);
                    SeaPort cp = portMap.get(cs.getParent()); //for ships in queue
                    Dock cd = dockMap.get(cs.getParent());
                    if (cp == null) {
                        cp = portMap.get(cd.getParent()); //if ship is docked, dock parent is found to link with port
                    }
                    fullMap.put(cs.getIndex(), cs);
                    shipMap.put(cs.getIndex(), cs);
                    world.assignShip(cp, cd, cs);
                    break;
                case "person":
                    Person pr = new Person(line);
                    world.assignPerson(portMap.get(pr.getParent()), pr);
                    fullMap.put(pr.getIndex(), pr);
                    break;
                case "job":
                    Job jb = new Job(line, shipMap);
                    fullMap.put(jb.getIndex(), jb);
                    jobMap.put(jb.getIndex(), jb);
                    world.assignJob((Ship) fullMap.get(jb.getParent()), jb); //typecasting
                default:
                    break;
            }
        }
        textBox.append(world.toString());
    }

    public void updateJob() {
        textBox.append("\n");
        progressPanel.removeAll();
        for (SeaPort sp : this.world.getPorts()) {

            //For initial removal of ships with no jobs that are docked
            for (Dock d : sp.getDock()) {
                if (d.getShip().getJob().isEmpty()) {
                    progressText.append(d.getShip().getName() + " has no jobs. Leaving dock: " + d.getName() + "\n\n");
                    
                    //If the docked ship has no jobs and the queue is not empty, the next ship in queue is pulled
                    while (!sp.getQue().isEmpty()) {
                        Ship newShip = sp.getQue().remove(0);
                        if (!newShip.getJob().isEmpty()) {
                            d.setShip(newShip); //set as the new ship
                            progressText.append(newShip.getName() + " docked at: " + d.getName() + "\n\n");
                            break;
                        } else {
                            if (sp.getQue().isEmpty()) {
                                d.setShip(newShip); //This is in case the last ship in queue has no jobs. This prevents nullpointerException.
                            }
                        }
                    }
                }
                d.getShip().setDock(d); //sets the dock information for the new ship
            }

            
            //Display all available workers at port
            for (Person p : sp.getPerson()) {
                resourceText.append("Name:" + p.getName() + " / Skill:" + p.getSkill() + " available at port\n");
            }
            resourceText.append("\n");
                    
            //This section iterates thorugh all ships to put them in the GUI
            //The queued ships will have the status of waiting
            //While docked ships wil start progress.
            for (Ship ship : sp.getShip()) {
                if (!ship.getJob().isEmpty()) {
                    for (Job jb : ship.getJob()) {
                        jb.jobGUI(progressPanel); //add ProgressBar component
                        jb.setWorkerTextArea(progressText);
                        jb.setResourceTextArea(resourceText);
                        if (!sp.checkForWorkers(jb.getRequirements())) { //Any jobs that cannot be completed is cancelled whether the ship is docked or in queue.
                            jb.setKillFlag();
                            jb.getWorkerTextArea().append("No available workers at " + sp.getName() + ". Cancelling " + jb.getName() + ". \n"); 
                            if (ship.getDock() != null) { //If the ship that's cancelled is a docked ship, ship leaving is logged.
                                progressText.append(ship.getName() + " cannot complete it's job. Leaving dock: " + ship.getDock().getName() + ".\n\n");
                            }
                        } else {
                            jb.startJob(); // Begin thread
                        }

                    }
                }
            }
        }
    }

//Search method to search the object linked lists for a match
    public void search() {
        String searchInput = searchField.getText().toLowerCase();
        String comboOption = searchCombo.getSelectedItem().toString();
        ArrayList<Thing> result = new ArrayList<>();

        textBox.append("\n--Searching for " + searchField.getText() + " by "
                + comboOption + "\n\n");

        switch (comboOption) {
            case "Name":
                result = world.searchByName(searchInput);
                break;
            case "Index": //updated searching by Index from Project1 by using fullMap hashMap
                try {
                    int i = Integer.parseInt(searchInput);
                    result.add(fullMap.get(i));
                } catch (NumberFormatException e) {
                    textBox.append("--Invalid index used. Integeres only.");
                    return;
                }
                break;
            case "Skill":
                result = world.searchBySkill(searchInput);
                break;
            default:
                break;
        }
        if (result.size() == 0) {
            textBox.append("--No result found. \n");
            return;
        }
        for (Thing t : result) {
            textBox.append(t + "\n");
        }
    }

    //Method for building the GUI
    private void buildGUI() {
        isRunning = true;
        Toolkit t = Toolkit.getDefaultToolkit();
        screenSize = t.getScreenSize();

        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel();

        loadButton = new JButton("Load");
        sortButton = new JButton("Sort");

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 25));
        searchBtn = new JButton("Search");

        searchCombo = new JComboBox<>();
        searchCombo.addItem("Name");
        searchCombo.addItem("Index");
        searchCombo.addItem("Skill");

        sortObj = new JComboBox<>();
        sortObj.addItem("SeaPorts");
        sortObj.addItem("Docks");
        sortObj.addItem("Ships");
        sortObj.addItem("Ship Que");
        sortObj.addItem("Person");
        sortObj.addItem("Job");

        sortCombo = new JComboBox<>();
        updateSort();

        blank = new JLabel("       ");

        topPanel.add(loadButton);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(searchCombo);
        topPanel.add(blank);
        topPanel.add(sortObj);
        topPanel.add(sortCombo);
        topPanel.add(sortButton);

        displayPanel = new JPanel(new GridLayout(3, 1));

        treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        treePanel.setPreferredSize(new Dimension(screenSize.width / 8, 600));

        textBox = new JTextArea();
        textBox.setLayout(new BorderLayout());
        textBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        textBox.setEditable(false);

        scrollPane = new JScrollPane(textBox);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        displayPanel.add(scrollPane);

        jobPanel = new JPanel(new BorderLayout());
        jobPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        jobPanel.setPreferredSize(new Dimension(screenSize.width / 4, 600));

        progressPanel = new JPanel();
        progressPanel.setLayout(new GridLayout(0, 3, 0, 10));

        //Adding resource pool and thread resource status update GUI
        progressText = new JTextArea();
        resourceText = new JTextArea();

        progressText.setLayout(new BorderLayout());
        progressText.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        progressText.setEditable(false);
        resourceText.setLayout(new BorderLayout());
        resourceText.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        resourceText.setEditable(false);

        resourcePane = new JScrollPane(resourceText);
        updatePane = new JScrollPane(progressText);

        resourcePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resourcePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        updatePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        updatePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        displayPanel.add(resourcePane);
        displayPanel.add(updatePane);
        displayPanel.setPreferredSize(new Dimension(screenSize.width / 3, 600));

        mainPanel.add(treePanel, BorderLayout.WEST);
        mainPanel.add(displayPanel, BorderLayout.CENTER);
        mainPanel.add(jobPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setTitle("Sea Port Program");
        setVisible(true);
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFile();
                try {
                    loadWorld(file);
                    drawTree();
                    jobPanel.removeAll();
                    resourceText.setText("");
                    progressText.setText("");
                    updateJob();
                    jobPane = new JScrollPane(progressPanel);
                    jobPanel.add(jobPane, BorderLayout.CENTER);
                    jobPanel.validate();

                } catch (IOException ex) {
                    Logger.getLogger(SeaPortProgram.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        sortObj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSort();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sort();
            }
        });
    }

    //sorts the ArrayList then adds the result to a new variable
    private void sort() {
        String sortObjStr = sortObj.getSelectedItem().toString();
        String sortComboStr = sortCombo.getSelectedItem().toString();
        ArrayList<?> resultArray = new ArrayList<>();

        textBox.append("\n" + "Sorting by: " + sortObjStr + "-" + sortComboStr + "\n\n");

        if (sortObjStr.equals("SeaPorts")) {
            ArrayList<SeaPort> p = world.getPorts();
            Collections.sort(p);
            world.setPorts(p);
            resultArray = world.getPorts();
            for (Object t : resultArray) {
                textBox.append(t + "\n");
            }
        }

        for (SeaPort sp : world.getPorts()) {
            switch (sortObjStr) {
                case "Docks":
                    Collections.sort(sp.getDock());
                    resultArray = sp.getDock();
                    break;
                case "Ships":
                    Collections.sort(sp.getShip());
                    resultArray = sp.getShip();
                    break;
                case "Ship Que":
                    if (sp.getQue().size() == 0) {
                        textBox.append("No ships in Queue");
                    } else {
                        Collections.sort(sp.getQue(), new ShipComparator(sortComboStr));
                        resultArray = sp.getQue();
                    }
                    break;
                case "Person":
                    Collections.sort(sp.getPerson());
                    resultArray = sp.getPerson();
                    break;
                case "Job":
                    for (Ship sh : sp.getShip()) {
                        Collections.sort(sh.getJob());
                        resultArray = sh.getJob();
                    }
                    break;
                default:
                    break;
            }
            textBox.append("\nPort " + sp.getName() + ":\n\n");
            for (Object t : resultArray) {
                textBox.append(t + "\n");
            }
        }
    }

    //updating subcategory sort box when Ship Que is selected
    private void updateSort() {
        sortCombo.removeAllItems();
        if (sortObj.getSelectedItem().equals("Ship Que")) {
            sortCombo.addItem("Draft");
            sortCombo.addItem("Length");
            sortCombo.addItem("Weight");
            sortCombo.addItem("Width");
        }
        sortCombo.addItem("Name");
        validate();
    }

    //method for drawing JTree
    //Creates world root node then iterates through the objects
    //parent node is refreshed for each class to add itself to the SeaPort
    //then to add its sub branches underneath it
    private void drawTree() {
        treePanel.removeAll();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("World");
        tree = new JTree(root);

        DefaultMutableTreeNode parentNode, portNode, dockNode, shipNode, queueNode, personNode;
        for (SeaPort sp : world.getPorts()) {
            portNode = new DefaultMutableTreeNode("Port: " + sp.getName());
            root.add(portNode);
            parentNode = new DefaultMutableTreeNode("Docks");
            portNode.add(parentNode);
            for (Dock d : sp.getDock()) {
                dockNode = new DefaultMutableTreeNode(d.getName());
                dockNode.add(new DefaultMutableTreeNode(d.getShip().getName()));
                parentNode.add(dockNode);
            }
            parentNode = new DefaultMutableTreeNode("Ships");
            portNode.add(parentNode);
            for (Ship sh : sp.getShip()) {
                shipNode = new DefaultMutableTreeNode(sh.getName());
                parentNode.add(shipNode);
            }
            parentNode = new DefaultMutableTreeNode("Person");
            portNode.add(parentNode);
            for (Person p : sp.getPerson()) {
                personNode = new DefaultMutableTreeNode(p.getName());
                parentNode.add(personNode);
            }
        }
        JScrollPane treePane = new JScrollPane(tree);
        treePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        treePanel.add(treePane, BorderLayout.CENTER);
        validate();
    }

    public static void main(String[] args) {
        SeaPortProgram sp = new SeaPortProgram();

    }//end main
}
