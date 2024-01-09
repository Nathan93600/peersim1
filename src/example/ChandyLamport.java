package example;

import peersim.core.*;
import peersim.edsim.*;

public class ChandyLamport implements EDProtocol {

    private enum Color { GREEN, RED }
    private Color color;
    private boolean[] closed;
    private String[] channelState;

    public ChandyLamport(String prefix) {
        int numChannels = Network.size();
        color = Color.GREEN;
        closed = new boolean[numChannels];
        channelState = new String[numChannels];
        for (int k = 0; k < numChannels; k++) {
            closed[k] = false;
            channelState[k] = "";
        }
    }

    public Object clone() {
        ChandyLamport cl = null;
        try { 
            cl = (ChandyLamport) super.clone(); 
        } catch(CloneNotSupportedException e) { /* Handle exception */ }
        cl.color = this.color;
        cl.closed = this.closed.clone();
        cl.channelState = this.channelState.clone();
        return cl;
    }

    public void processEvent(Node node, int pid, Object event) {
        if (event instanceof MarkerMessage) {
            receiveMarker((MarkerMessage) event, node, pid);
        } else if (event instanceof ApplicationMessage) {
            receiveApplicationMessage((ApplicationMessage) event, node, pid);
        }
    }

    private void receiveMarker(MarkerMessage marker, Node node, int pid) {
        int channel = marker.getChannel();
        if (color == Color.GREEN) {
            turnRed(node, pid);
        }
        closed[channel] = true;
    }

    private void receiveApplicationMessage(ApplicationMessage msg, Node node, int pid) {
        int channel = msg.getChannel();
        if (color == Color.RED && !closed[channel]) {
            channelState[channel] += msg.getContent();
        }
    }

    private void turnRed(Node node, int pid) {
        saveLocalState(node);
        color = Color.RED;
        for (int k = 0; k < closed.length; k++) {
            sendMarker(node, pid, k);
        }
    }

    private void saveLocalState(Node node) {
        // Example: Saving a simple string representation of the node's state
        String state = "State of Node " + node.getID();
        // You can add more complex state capturing logic here
        System.out.println("Saving state of node " + node.getID() + ": " + state);
        // If needed, store the state in a class variable or a data structure
    }

    private void sendMarker(Node node, int pid, int channel) {
        Node targetNode = Network.get(channel); // Assuming 'channel' corresponds to node index
        EDProtocol targetProtocol = (EDProtocol) targetNode.getProtocol(pid);
        MarkerMessage marker = new MarkerMessage(channel);
        
        // Simulating sending a marker message
        targetProtocol.processEvent(targetNode, pid, marker);
        System.out.println("Node " + node.getID() + " sends marker to node " + targetNode.getID());
    }
}

class MarkerMessage {
    private final int channel;

    public MarkerMessage(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }
}

class ApplicationMessage {
    private final int channel;
    private final String content;

    public ApplicationMessage(int channel, String content) {
        this.channel = channel;
        this.content = content;
    }

    public int getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }
}

