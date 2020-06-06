/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author Balint Toth <www.deeno.co.uk>
 */
public class Results {
    
    private String nodeName;
    private int shortestDistanceFromStart;
    private String previousNode;

    public Results() {
    }

    public Results(String nodeName, int shortestDistanceFromStart, String previousNode) {
        this.nodeName = nodeName;
        this.shortestDistanceFromStart = shortestDistanceFromStart;
        this.previousNode = previousNode;
    }

    public String getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(String previousNode) {
        this.previousNode = previousNode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getShortestDistanceFromStart() {
        return shortestDistanceFromStart;
    }

    public void setShortestDistanceFromStart(int shortestDistanceFromStart) {
        this.shortestDistanceFromStart = shortestDistanceFromStart;
    }
    
    
    
}
