
import java.util.Random;

public class Node
{
    private static int count = 0;
////////////i changed the count
    // Each node will store a costs vector, its own distance vector
    // and a distance vector for each of its neighbors
    private int[] neighbors;
    private int[] cost = new int[DVSimulator.NUMNODES];
    private int[] myDV = new int[DVSimulator.NUMNODES];
    private int[][] neighborDV = new int[DVSimulator.NUMNODES][DVSimulator.NUMNODES];
    private int id;
    // fwd table specifies for reaching each destination (index) from current node
    // which neighbor node we should visit first
    private int[] fwdTable = new int[DVSimulator.NUMNODES];
    // bestPath is a temporary changing forwarding table
    private int[] bestPath = new int[DVSimulator.NUMNODES];
    private int numUpdates = 0;
    private Packet[] packets;
    public Node() {
        this.id = count++;
        //we didn't have to do this in the loop
        neighbors=DVSimulator.neighbors[id];
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            // reading from the DVSimulator variables,
            // for each node:
            // 1. initialize its cost and myDV value
            cost[i]=DVSimulator.cost[id][i];
            myDV[i]=cost[i];
            // 2. specify the neighbors
            // 3. Initialize the forwarding table (bestPath variable)

            // BestPath to any node should be initialized as follows:
            // If node has id = this node's id, use id
            // Else if node is a direct neighbor, use the neighbor id
            // Otherwise, choose a random neighbor (see randomNeighbor method)
            // WRITE YOUR CODE HERE

            //best path has three different states
            if(i==id) {
                bestPath[i]=id;
            }
            else if(isneighbour(i)){
                bestPath[i]=i;
            }
            else{
                bestPath[i]=randomNeighbor();
            }
        }
        // send initial DV to neighbors
        notifyNeighbors();
    }

    public int getId() {
        return id;
    }

    public boolean isneighbour(int i){
        for (int j = 0; j < neighbors.length; j++) {
            if(i==neighbors[j])
                return true;
            else
                continue;
        }
        return false;
    }


    public void printDV() {
        System.out.print("i            " );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.print(i + "      ");
        }
        System.out.println();
        System.out.print("cost         " );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.print(myDV[i] + "      ");
        }
        System.out.println();
    }

    public void printFwdTable() {
        System.out.println("dest         next Node" );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.println(i + "            " + fwdTable[i]);
        }
    }

    public int randomNeighbor() {
        int rnd = new Random().nextInt(neighbors.length);
        return neighbors[rnd];
    }

    public void notifyNeighbors() {
        // for each neighbor, create a new packet (see Packet class)
        // with current node id as source, neighbor id as destination
        // and current node's DV as the dv
        // then send packet using helper method sendPacket in DVSimulator

        // WRITE YOUR CODE HERE
        for (int i = 0; i <neighbors.length ; i++) {
            Packet p= new Packet(id,neighbors[i],myDV);
            DVSimulator.sendPacket(p);
        }
    }

    public void updateDV(Packet p) {
        // this method is called by the simulator each time a packet is received from a neighbor
        int neighbor_id = p.getSource();
        neighborDV[neighbor_id] = p.getDV();


        // for each value in the DV received from neighbor, see if it provides a cheaper path to
        // the corresponding node. If it does, update myDV and bestPath accordingly
        // current DV of i is min { current DV of i, cost to neighbor + neighbor's DV to i  }

        // If you do any changes to myDV:
        // 1. Notify neighbors about the new myDV using notifyNeighbors() method
        // 2. Increment the convergence measure numUpdates variable once

        // WRITE YOUR CODE HERE
        boolean changed = false;
        final int INF = 999;

        // If the neighbor isn't directly reachable, ignore this update.
        if (cost[neighbor_id] >= INF) {
            return;
        }

        for (int dest = 0; dest < DVSimulator.NUMNODES; dest++) {
            if (dest == id) {
                myDV[dest] = 0;
                bestPath[dest] = id;
                continue;
            }

            int neighborToDest = neighborDV[neighbor_id][dest];
            if (neighborToDest >= INF) {
                continue;
            }

            int candidate = cost[neighbor_id] + neighborToDest;
            if (candidate < myDV[dest]) {
                myDV[dest] = candidate;
                bestPath[dest] = neighbor_id;
                changed = true;
            }
        }

        if (changed) {
            numUpdates++;
            notifyNeighbors();
        }
    }

    public void buildFwdTable() {
        // just copy the final values of bestPath vector
        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            fwdTable[i] = bestPath[i];
        }
    }

    public int getNumUpdates() {
        return numUpdates;
    }
}
