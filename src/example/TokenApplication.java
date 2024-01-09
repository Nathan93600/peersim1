package example;

import peersim.core.*;
import peersim.cdsim.CDProtocol;
import java.util.Random;

public class TokenApplication implements CDProtocol {
    private boolean hasToken;

    public TokenApplication(String prefix) {
        hasToken = false; // Initially, no token. Set to true for initial token holders.
    }

    public void nextCycle(Node node, int protocolID) {
        if (hasToken) {
            int numNodes = Network.size();
            Random rnd = new Random();
            int randomNeighborIndex = rnd.nextInt(numNodes);
            Node randomNeighbor = Network.get(randomNeighborIndex);
            // Logic to send token to randomNeighbor
            // Example: randomNeighbor.getProtocol(protocolID).receiveToken();
            hasToken = false; // After sending the token
        }
    }

    public Object clone() {
        TokenApplication tp = null;
        try {
            tp = (TokenApplication) super.clone();
        } catch (CloneNotSupportedException e) { /* Handle exception */ }
        tp.hasToken = this.hasToken;
        return tp;
    }

    // Additional methods if needed, like receiveToken()
}
