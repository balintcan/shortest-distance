/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.util.List;

/**
 *
 * @author Balint Toth <www.deeno.co.uk>
 */
public class Node {
    
    private String city;
    private List<String> neighbourList;
    private List<Integer> distanceList;

    public Node() {
    }

    public Node(String city, List<String> neighbourList, List<Integer> distanceList) {
        this.city = city;
        this.neighbourList = neighbourList;
        this.distanceList = distanceList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getNeighbourList() {
        return neighbourList;
    }

    public void setNeighbourList(List<String> neighbourList) {
        this.neighbourList = neighbourList;
    }

    public List<Integer> getDistanceList() {
        return distanceList;
    }

    public void setDistanceList(List<Integer> distanceList) {
        this.distanceList = distanceList;
    }

    
    
    
    
}
