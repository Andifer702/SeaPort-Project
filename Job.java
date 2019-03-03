/*
 * File Name: SeaPort.java
 * Date: 01/24/2019
 * Author: Joon Park
 * Purpose: Job class is used for retaining jobs required by the parent ship.
 * It also implements runnable for multithreading operations.
 */
package seaport_park;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Job extends Thing implements Runnable {

    private double duration;
    private ArrayList<String> requirements;

    private enum Status {
        RUNNING, SUSPENDED, WAITING, DONE
    };

    private boolean pauseFlag;
    private boolean killFlag;
    private Ship parentShip;
    private SeaPort parentPort;
    private boolean isSuspended, isCancelled, isComplete, isEnd;

    private JProgressBar jp;
    private JButton stopButton;
    private JButton killButton;
    private JPanel jobBarPanel;

    private Status status;
    private Thread jobThread;

    public Job(Scanner s, HashMap<Integer, Ship> shipMap) {
        super(s);
        duration = (s.hasNextDouble()) ? s.nextDouble() : 0.0;
        requirements = new ArrayList<>();
        while (s.hasNext()) {
            requirements.add(s.next());
        }

        pauseFlag = false;
        killFlag = false;
        isComplete = false;
        isSuspended = false;
        isEnd = false;
        isCancelled = false;

        this.setParentShip(shipMap.get(this.getParent()));
        this.setParentPort(this.getParentShip().getPort());

        jp = new JProgressBar();
        jp.setStringPainted(true);

        stopButton = new JButton("Stop");
        killButton = new JButton("Cancel");

        status = Status.SUSPENDED;
        jobThread = new Thread(this);
    }

    public void jobGUI(JPanel panel) {
        jobBarPanel = panel;
        panel.add(jp);
        panel.add(stopButton);
        panel.add(killButton);

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                togglePauseFlag();
            }
        });

        killButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setKillFlag();
            }
        });
    }

    public void togglePauseFlag() {
        pauseFlag = !pauseFlag;
    }

    public void setKillFlag() {
        killFlag = true;
        killButton.setBackground(Color.RED);
    }

    public synchronized void startJob() {
        isComplete = false;
        jobThread.start();
    }

    public void endJob() {
        jp.setVisible(false);
        this.setIsComplete(true);
        this.setIsEnd(true);
        stopButton.setVisible(false);
        killButton.setVisible(false);
        jobBarPanel.remove(jp);
        jobBarPanel.remove(stopButton);
        jobBarPanel.remove(killButton);
    }

    public void showStatus(Status st) {
        status = st;
        switch (status) {
            case RUNNING:
                stopButton.setBackground(Color.GREEN);
                stopButton.setText("Running");
                break;
            case SUSPENDED:
                stopButton.setBackground(Color.YELLOW);
                stopButton.setText("Suspended");
                break;
            case WAITING:
                stopButton.setBackground(Color.ORANGE);
                stopButton.setText("Waiting");
                break;
            case DONE:
                stopButton.setBackground(Color.RED);
                stopButton.setText("Done");
                break;
        }
    }

    //First synchronized method. This checks to make sure that the ships in queue are waiting to begin
    private synchronized boolean waitingForDock() {
        if (this.getParentPort().getQue().contains(this.getParentShip())) {
            return true; //ships in queue are returning true for still waiting
        } else {
            return false; //ship docked
        }
    }

    @Override
    public void run() {
        ArrayList<Boolean> statusList;
        Ship nsToDock;
        Dock parentDock;

        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * (long) duration;
        double reqTime = stopTime - time;

        //This block is for ships in queue. They are forced to wait for notifyAll()
        //from any running threads completing
        synchronized (this.getParentPort()) {
            while (this.waitingForDock()) {
                try {
                    this.showStatus(Status.WAITING);
                    this.getParentPort().wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }
            }
        }

        //Code snippet from Project 3 rubric
        while (time < stopTime && !killFlag) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (!pauseFlag) {
                showStatus(Status.RUNNING);
                time += 100;
                jp.setValue((int) (((time - startTime) / reqTime) * 100));
            } else {
                showStatus(Status.SUSPENDED);
            }
        }

        //Completion status
        jp.setValue(100);
        showStatus(Status.DONE);
        isComplete = true;

        //When the job is completed, this block of code will add the job status
        //into an ArrayList. This is to check for any ships with multiple jobs.
        //Once all jobs are completed, queue is checked for the next ship.
        //if the new ship has a job it is added to this thread's original dock
        
        synchronized (this.getParentPort()) {
            statusList = new ArrayList<>();
            this.getParentShip().getJob().forEach((job) -> {
                statusList.add(job.getIsComplete());
            });

            if (!statusList.contains(false)) {
                while (!this.getParentPort().getQue().isEmpty()) {
                    nsToDock = this.getParentPort().getQue().remove(0);

                    if (!nsToDock.getJob().isEmpty()) { //new ship has job
                        parentDock = this.getParentShip().getDock(); //obtain the doc information for the ship whose job just finished
                        parentDock.setShip(nsToDock); //set ship to dock
                        nsToDock.setDock(parentDock); //set dock to ship
                        break;
                    }
                }
            }
            this.getParentPort().notifyAll();
        }
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double d) {
        if (d >= 0.0) {
            this.duration = d;
        }
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<String> r) {
        this.requirements = r;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public Thread getThread() {
        return this.jobThread;
    }

    public void setParentPort(SeaPort parentPort) {
        this.parentPort = parentPort;
    }

    public SeaPort getParentPort() {
        return this.parentPort;
    }

    public void setParentShip(Ship parentShip) {
        this.parentShip = parentShip;
    }

    public Ship getParentShip() {
        return this.parentShip;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public boolean getIsSuspended() {
        return this.isSuspended;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean getIsCancelled() {
        return this.isCancelled;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setJobThread(Thread jobThread) {
        this.jobThread = jobThread;
    }

    public Thread getJobThread() {
        return this.jobThread;
    }

    public String toString() {
        String st = "Job: " + super.toString() + "\n";
        st += "Duration: " + duration + " Requirements: ";
        for (String s : requirements) {
            st += s + " ";
        }
        return st;
    }
}
