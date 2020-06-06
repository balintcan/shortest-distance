package proba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.RequestScoped;
//import org.hibernate.Session;
import pojos.Line;
import pojos.Node;
import pojos.Results;


/**
 *
 * @author Balint Toth <www.deeno.co.uk>
 */
@ManagedBean
@RequestScoped
public class engine {
    
   
 
    private Node nextNode;
    private Node startingNode;
    private Node previousNode;
    private Node nextNodeProspectiveNeighbour;
    private Node kisnode;
    
    private Results destinationResultNode;
    private Results currentResultNode; 
    private Results resultToUpdate;
    
    private int distanceSoFar;
    private int currentDistance;
    private int totalDistance; 
    private int nodeTotal;
    private int j;
    private int d = 5000; //initial distance for calculations
    
    private String l = "stroke:rgb(255,0,0);stroke-width:2";
    private String startingNodeName;
    private String destinationNodeName;
    private String nextNodeName;
    private String neighbourName;
    private String resultText="";
    private String namePair="";
    private String namePairRev="";
    private String mapSize;
    private String nodeNull;
    
    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<String, Line> lineStyle = new HashMap<>();   
    private Map<String, Node> visitedNodes = new HashMap<>();
    private Map<String, Results> resultsMap = new HashMap<>();
    
    private List<String> route = new ArrayList<>();
    private List<String> routeInOrder = new ArrayList<>();
    private List<Node> nodeList;
    private List<Line> lineList;
    private List<Results> resultsList;
    

    
    //create node objects
    Node Berlin = new Node("Berlin", new ArrayList<String>(Arrays.asList("London","Paris", "Vienna","Budapest","Edinburgh","Warsaw","Oslo")),new ArrayList<Integer>(Arrays.asList(930,880,520,690,1160,510,830)));        
    Node Budapest = new Node("Budapest", new ArrayList<String>(Arrays.asList("Vienna","Berlin","Warsaw","Rome")),new ArrayList<Integer>(Arrays.asList(220,690,530,800)));        
    Node London = new Node("London", new ArrayList<String>(Arrays.asList("Berlin","Paris","Edinburgh","Rome")),new ArrayList<Integer>(Arrays.asList(930,350,540,1430)));        
    Node Paris = new Node("Paris", new ArrayList<String>(Arrays.asList("London","Berlin","Vienna","Oslo","Rome")),new ArrayList<Integer>(Arrays.asList(350,880,1040,1330,1100)));        
    Node Vienna = new Node("Vienna", new ArrayList<String>(Arrays.asList("Paris","Berlin","Budapest","Rome")),new ArrayList<Integer>(Arrays.asList(1040,520,220,760)));
        
    Node Edinburgh = new Node("Edinburgh", new ArrayList<String>(Arrays.asList("London","Berlin","Warsaw","Oslo")),new ArrayList<Integer>(Arrays.asList(540,1160,1640,940)));
    Node Oslo = new Node("Oslo", new ArrayList<String>(Arrays.asList("Edinburgh", "Paris","Berlin","Warsaw")),new ArrayList<Integer>(Arrays.asList(940,1330,830,1050)));
    Node Warsaw = new Node("Warsaw", new ArrayList<String>(Arrays.asList("Edinburgh","Berlin","Budapest","Oslo")),new ArrayList<Integer>(Arrays.asList(1640,510,530,1050)));
    Node Rome = new Node("Rome", new ArrayList<String>(Arrays.asList("Paris","London","Vienna","Budapest")),new ArrayList<Integer>(Arrays.asList(1100,1430,760,800)));


    
    //create Result objects
    Results resultBerlin = new Results("Berlin",d,"");
    Results resultBudapest = new Results("Budapest",d,"");
    Results resultLondon = new Results("London",d,"");
    Results resultParis = new Results("Paris",d,"");
    Results resultVienna = new Results("Vienna",d,"");
    
    Results resultEdinburgh = new Results("Edinburgh",d,"");
    Results resultOslo = new Results("Oslo",d,"");
    Results resultWarsaw = new Results("Warsaw",d,"");
    Results resultRome = new Results("Rome",d,"");
    
    //create Line objects
    Line EdinburghLondon = new Line("EdinburghLondon",20,30,50,180,l);
    Line EdinburghOslo = new Line("EdinburghOslo",20,30,260,7,l);
    Line EdinburghBerlin = new Line("EdinburghBerlin",20,30,270,180,l);
    Line EdinburghWarsaw = new Line("EdinburghWarsaw",20,30,360,195,l);
        
    Line OsloParis = new Line("OsloParis",260,7,80,280,l);
    Line OsloBerlin = new Line("OsloBerlin",260,7,270,180,l);
    Line OsloWarsaw = new Line("OsloWarsaw",260,7,360,195,l);
        
    Line LondonBerlin = new Line("LondonBerlin",50,180,260,180,l);
    Line LondonRome = new Line("LondonRome",50,180,250,540,l);
    Line LondonParis = new Line("LondonParis",50,180,80,280,l);
        
    Line ParisBerlin = new Line("ParisBerlin",80,280,270,180,l);
    Line ParisVienna = new Line("ParisVienna",80,280,290,310,l);
    Line ParisRome = new Line("ParisRome",80,280,250,540,l);
        
    Line ViennaBudapest = new Line("ViennaBudapest",290,310,330,320,l);
    Line ViennaRome = new Line("ViennaRome",290,310,250,540,l);
    Line ViennaBerlin = new Line("ViennaBerlin",290,310,270,180,l);
        
    Line BudapestBerlin = new Line("BudapestBerlin",330,320,270,180,l);
    Line BudapestRome = new Line("BudapestRome",330,320,250,540,l);
    Line BudapestWarsaw = new Line("BudapestWarsaw",330,320,360,195,l);
        
    Line WarsawBerlin = new Line("WarsawBerlin",360,195,270,180,l);
     

    public engine() {
        
        //Process Line objects into a Map
        //*********************************        
        lineList = new ArrayList<Line>(Arrays.asList(EdinburghBerlin,EdinburghOslo,EdinburghLondon,EdinburghWarsaw,
                 OsloParis,OsloBerlin,OsloWarsaw,
                 LondonParis,LondonBerlin,LondonRome,
                 ParisBerlin,ParisVienna,ParisRome,
                 ViennaBerlin,ViennaBudapest,ViennaRome,
                 BudapestBerlin,BudapestRome,BudapestWarsaw,WarsawBerlin));
         
         //put all Lines into a map
         for (Line l : lineList) {
            lineStyle.put(l.getLineName(), l);
        }
       
        
       //Process Node objects into a Map
       //***********************************
        nodeList = new ArrayList<Node>(Arrays.asList(Berlin,Budapest,London,Paris,Vienna,Edinburgh,Oslo,Warsaw,Rome));
        nodeTotal = nodeList.size();
        
        //put Nodes into a map
        for (Node n : nodeList) {
            nodeMap.put(n.getCity(), n);
        }
       
        
         
        //proceess Results objects into a Map
        //***********************************
        resultsList = new ArrayList<Results>(Arrays.asList(resultBerlin,resultBudapest,resultLondon,
                resultParis,resultVienna,resultEdinburgh,resultOslo,resultWarsaw,resultRome));
        
        for (Results r : resultsList) {
            resultsMap.put(r.getNodeName(), r);
        }
        //***********************************

    }
    
    //plan route button clicked
    public void planRoute(){

       
        //value input by user is put into corresponding line of results
        //get resultToUpdate object from map
        //it gets the initial name as previous node and gets a distance of zero
        //to work with this node in the loop we assign startingNodeName to nextNodeName
        
        
               
        resultToUpdate = resultsMap.get(startingNodeName);  // get london result line
        resultToUpdate.setPreviousNode(startingNodeName);   //from london
        resultToUpdate.setShortestDistanceFromStart(0);   //0 km
        
        for(int o=0; o<2;o++){
            
            for (Node nod : nodeList) {
                nodeMap.put(nod.getCity(), nod);  //ujratolti a node map-et.
                        
        }
            
            
        nextNodeName = startingNodeName;
        
        nextNode = nodeMap.get(nextNodeName);     
        
        mapSize = Integer.toString(nodeMap.size()) ;
        
        for(j = 0; j<nodeTotal; j++){  //annyiszor ahany node van, mindegyikre egyszer kerul sor

                                         
            for (int i = 0; i < nextNode.getNeighbourList().size(); i++) {  //if edinburgh do it 1x
            neighbourName = nextNode.getNeighbourList().get(i);  //vedd ki a listabol az i-dik nevet
            resultToUpdate = resultsMap.get(neighbourName); //most london sorba irjunk bele
                
                                  
                    distanceSoFar = resultsMap.get(nextNode.getCity()).getShortestDistanceFromStart();//ITT
                    
                    currentDistance = nextNode.getDistanceList().get(i);
                    
                    int x = distanceSoFar + currentDistance;
                    int y = resultToUpdate.getShortestDistanceFromStart();
                    
                    
                        if (x < y) {
                            resultToUpdate.setShortestDistanceFromStart(distanceSoFar+currentDistance); 
                            resultToUpdate.setPreviousNode(nextNode.getCity()); //ITT
                        
                    }
                               
            }
            nodeMap.remove(nextNode.getCity());//ITT  //map-bol kiveszi a bevizsgalt Node-ot
            mapSize = mapSize + Integer.toString(nodeMap.size()) ;
           // visitedNodes.put(nextNodeName, nextNode);
            previousNode = nextNode;
            //this is where we jump to examine the next node
            
            
            for (String n : nextNode.getNeighbourList()) {  //egyenkent megnezi a szomszedait
                
                if (nodeMap.containsKey(n)) {   //ha a szomszed benne van a Map-ben akkor 
                    nextNode = nodeMap.get(n); //az lesz nextNode
 
 
                }     
                
                if (nextNode==previousNode && (!nodeMap.isEmpty())) {
                       // nodeNull="ugyanaz";
                       // zarvany = true;    
                        for (Node no : nodeList) {
                            if (nodeMap.containsKey(no.getCity())) {
                                nextNode=no;
                                
 
                            }
                    }
                    
                    } 
            } 
        }
       // }//ez a plusz
        }  
        
       //Process results so that it can be ordered and written on screen
        //put all thru-city names into a List
        destinationResultNode = resultsMap.get(destinationNodeName);
        currentResultNode = destinationResultNode;
        
        do{
            route.add(currentResultNode.getNodeName());
            
            currentResultNode=resultsMap.get(currentResultNode.getPreviousNode());
        }while(!(currentResultNode.getNodeName().equals(startingNodeName)));
        
        totalDistance = destinationResultNode.getShortestDistanceFromStart();
        for (int i = route.size()-1; i >= 0; i--) {
            
            routeInOrder.add(route.get(i));
            
        }
        
        //create String with names of thru cities from route List        
        for (String s : routeInOrder) {            
            resultText = resultText + ", " +s;                        
        }        
        resultText = startingNodeName + resultText; //add place name where journey starts
        
       
        
        routeInOrder.add(0, startingNodeName);
        for (int i = 0; i < routeInOrder.size(); i++) {
            
            if (i<routeInOrder.size()-1) {
                namePair = routeInOrder.get(i)+routeInOrder.get(i+1);
                namePairRev = routeInOrder.get(i+1)+routeInOrder.get(i);
            }
            
            
            
            if (lineStyle.containsKey(namePair)) {
                lineStyle.get(namePair).setStyl("stroke:blue;stroke-width:4");
            } else if (lineStyle.containsKey(namePairRev)){
                lineStyle.get(namePairRev).setStyl("stroke:blue;stroke-width:4");
            }
        }
        
        

    }
    
    //getters and setters
    public String getStartingNodeName() {
        return startingNodeName;
    }

    public void setStartingNodeName(String startingNodeName) {
        this.startingNodeName = startingNodeName;
    }

    public String getDestinationNodeName() {
        return destinationNodeName;
    }

    public void setDestinationNodeName(String destinationNodeName) {
        this.destinationNodeName = destinationNodeName;
    }   

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Line getEdinburghLondon() {
        return EdinburghLondon;
    }

    public void setEdinburghLondon(Line EdinburghLondon) {
        this.EdinburghLondon = EdinburghLondon;
    }

    public Line getEdinburghOslo() {
        return EdinburghOslo;
    }

    public void setEdinburghOslo(Line EdinburghOslo) {
        this.EdinburghOslo = EdinburghOslo;
    }

    public Line getEdinburghBerlin() {
        return EdinburghBerlin;
    }

    public void setEdinburghBerlin(Line EdinburghBerlin) {
        this.EdinburghBerlin = EdinburghBerlin;
    }

    public Line getEdinburghWarsaw() {
        return EdinburghWarsaw;
    }

    public void setEdinburghWarsaw(Line EdinburghWarsaw) {
        this.EdinburghWarsaw = EdinburghWarsaw;
    }

    public Line getOsloParis() {
        return OsloParis;
    }

    public void setOsloParis(Line OsloParis) {
        this.OsloParis = OsloParis;
    }

    public Line getOsloBerlin() {
        return OsloBerlin;
    }

    public void setOsloBerlin(Line OsloBerlin) {
        this.OsloBerlin = OsloBerlin;
    }

    public Line getOsloWarsaw() {
        return OsloWarsaw;
    }

    public void setOsloWarsaw(Line OsloWarsaw) {
        this.OsloWarsaw = OsloWarsaw;
    }

    public Line getLondonBerlin() {
        return LondonBerlin;
    }

    public void setLondonBerlin(Line LondonBerlin) {
        this.LondonBerlin = LondonBerlin;
    }

    public Line getLondonRome() {
        return LondonRome;
    }

    public void setLondonRome(Line LondonRome) {
        this.LondonRome = LondonRome;
    }

    public Line getLondonParis() {
        return LondonParis;
    }

    public void setLondonParis(Line LondonParis) {
        this.LondonParis = LondonParis;
    }

    public Line getParisBerlin() {
        return ParisBerlin;
    }

    public void setParisBerlin(Line ParisBerlin) {
        this.ParisBerlin = ParisBerlin;
    }

    public Line getParisVienna() {
        return ParisVienna;
    }

    public void setParisVienna(Line ParisVienna) {
        this.ParisVienna = ParisVienna;
    }

    public Line getParisRome() {
        return ParisRome;
    }

    public void setParisRome(Line ParisRome) {
        this.ParisRome = ParisRome;
    }

    public Line getViennaBudapest() {
        return ViennaBudapest;
    }

    public void setViennaBudapest(Line ViennaBudapest) {
        this.ViennaBudapest = ViennaBudapest;
    }

    public Line getViennaRome() {
        return ViennaRome;
    }

    public void setViennaRome(Line ViennaRome) {
        this.ViennaRome = ViennaRome;
    }

    public Line getViennaBerlin() {
        return ViennaBerlin;
    }

    public void setViennaBerlin(Line ViennaBerlin) {
        this.ViennaBerlin = ViennaBerlin;
    }

    public Line getBudapestBerlin() {
        return BudapestBerlin;
    }

    public void setBudapestBerlin(Line BudapestBerlin) {
        this.BudapestBerlin = BudapestBerlin;
    }

    public Line getBudapestRome() {
        return BudapestRome;
    }

    public void setBudapestRome(Line BudapestRome) {
        this.BudapestRome = BudapestRome;
    }

    public Line getBudapestWarsaw() {
        return BudapestWarsaw;
    }

    public void setBudapestWarsaw(Line BudapestWarsaw) {
        this.BudapestWarsaw = BudapestWarsaw;
    }

    public Line getWarsawBerlin() {
        return WarsawBerlin;
    }

    public void setWarsawBerlin(Line WarsawBerlin) {
        this.WarsawBerlin = WarsawBerlin;
    }

    public Node getKisnode() {
        return kisnode;
    }

    public void setKisnode(Node kisnode) {
        this.kisnode = kisnode;
    }

    public String getMapSize() {
        return mapSize;
    }

    public void setMapSize(String mapSize) {
        this.mapSize = mapSize;
    }

    public String getNodeNull() {
        return nodeNull;
    }

    public void setNodeNull(String nodeNull) {
        this.nodeNull = nodeNull;
    }

    
    
}
