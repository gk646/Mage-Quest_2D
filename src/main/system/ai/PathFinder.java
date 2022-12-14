package main.system.ai;

import gameworld.world.maps.OverWorld;
import main.MainGame;

import java.util.ArrayList;

public class PathFinder {
    public final ArrayList<Node> pathList = new ArrayList<>();
    private final MainGame mg;
    private final ArrayList<Node> openList = new ArrayList<>();
    private Node[][] nodes;
    private Node startNode;
    private Node goalNode;
    private Node currentNode;
    private boolean goalReached = false;
    private int step = 0;

    public PathFinder(MainGame mg) {
        this.mg = mg;

    }

    public void instantiateNodes() {
        nodes = new Node[OverWorld.worldSize.x][OverWorld.worldSize.y];
        for (int i = 0; i < OverWorld.worldSize.x; i++) {
            for (int b = 0; b < OverWorld.worldSize.y; b++) {
                nodes[i][b] = new Node(i, b);
            }
        }
    }

    //todo check for end of map
    private void resetNodes(int startCol, int startRow) {
        for (int i = Math.max(0, startCol - 16); i < Math.min(mg.wRender.worldSize.x - 1, startCol + 16); i++) {
            for (int b = Math.max(0, startRow - 16); b < Math.min(mg.wRender.worldSize.y - 1, startRow + 16); b++) {
                nodes[i][b].open = false;
                nodes[i][b].checked = false;
                nodes[i][b].solid = false;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes(startCol, startRow);
        startNode = nodes[startCol][startRow];
        currentNode = startNode;
        goalNode = nodes[goalCol][goalRow];
        for (int i = Math.max(0, startCol - 16); i < Math.min(mg.wRender.worldSize.x - 1, startCol + 16); i++) {
            for (int b = Math.max(0, startRow - 16); b < Math.min(mg.wRender.worldSize.y - 1, startRow + 16); b++) {
                int tileNum = OverWorld.worldData[i][b];
                if (mg.wRender.tileStorage[tileNum].collision) {
                    nodes[i][b].solid = true;
                }
                getCost(nodes[i][b]);
            }
        }
    }


    private void getCost(Node node) {
        //G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        //H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        //F cost
        node.fCost = node.gCost + node.hCost;

    }

    public boolean search() {
        while (!goalReached && step < 2000) {
            if (playerTooFar()) {
                return false;
            }
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                openNode(nodes[col][row - 1]);
            }
            if (col - 1 >= 0) {
                openNode(nodes[col - 1][row]);
            }
            if (row + 1 < OverWorld.worldSize.y) {
                openNode(nodes[col][row + 1]);
            }
            if (col + 1 < OverWorld.worldSize.x) {
                openNode(nodes[col + 1][row]);
            }

            int bestNodesIndex = 0;
            int bestNodesFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodesFCost) {
                    bestNodesIndex = i;
                    bestNodesFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodesFCost) {
                    if (openList.get(i).gCost < openList.get(bestNodesIndex).gCost) bestNodesIndex = i;
                }
            }
            if (openList.size() == 0) {
                return false;
            }
            currentNode = openList.get(bestNodesIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
                return true;
            }
        }
        return false;
    }

    private void trackPath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }

    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private boolean playerTooFar() {
        return Math.abs(currentNode.col - goalNode.col) >= 15 || Math.abs(currentNode.row - goalNode.row) >= 15;
    }
}
