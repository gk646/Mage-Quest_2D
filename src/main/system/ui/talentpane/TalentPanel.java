package main.system.ui.talentpane;


import main.MainGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class TalentPanel {
    private static final int TALENT_SIZE = 45;
    private final Color normalColor = new Color(143, 143, 140, 255), epicColor = new Color(168, 93, 218), legendaryColor = new Color(239, 103, 3);
    private final Color rareColor = new Color(26, 111, 175), lightBackgroundAlpha = new Color(192, 203, 220, 190), darkBackground = new Color(90, 105, 136);
    private final TalentNode[] talent_Nodes;
    private final int stringY = 0;
    private final Rectangle talentPanelCloser;
    private final Rectangle talentPanelMover;
    private final Rectangle wholeTalentWindow;
    private final MainGame mg;
    private final BasicStroke width2 = new BasicStroke(2);
    private final BasicStroke width5 = new BasicStroke(5);
    private final BasicStroke width1 = new BasicStroke(1);
    private int talentPanelX = 300;
    private int talentPanelY = 300;
    private final Point lastTalentPosition = new Point(talentPanelX, talentPanelY);
    private int pointsToSpend;
    private Point previousMousePosition = new Point(300, 300);

    public TalentPanel(MainGame mg) {
        this.mg = mg;
        this.talent_Nodes = new TalentNode[1];
        createTalentNodes();
        int talentPanelWidth = 1200;
        int talentPanelHeight = 700;
        talentPanelMover = new Rectangle(talentPanelX, talentPanelY, talentPanelWidth, talentPanelHeight);
        talentPanelCloser = new Rectangle(talentPanelX, talentPanelY, 30, 30);
        wholeTalentWindow = new Rectangle(talentPanelX - 20, talentPanelY - 20, talentPanelWidth + 20, talentPanelHeight + 20);
        hideTalentCollision();
    }

    public void drawTalentWindow(Graphics2D g2) {
        g2.setFont(mg.ui.maruMonica);
        drawTalentPane(g2);

    }

    private void drawTalentPane(Graphics2D g2) {
        drawTalentBackground(g2, talentPanelX, talentPanelY);
        drawTalentNodes(g2, talentPanelX, talentPanelY);
        lastTalentPosition.x = talentPanelX;
        lastTalentPosition.y = talentPanelY;
    }

    public void drawTooltip(Graphics2D g2) {
        if (!mg.mouseH.mouse1Pressed) {
            for (TalentNode talentN : talent_Nodes) {
                if (talentN.toolTipTimer >= 40) {
                    getToolTip(g2, talentN);
                }
                if (talentN.boundBox.contains(mg.motionH.lastMousePosition)) {
                    talentN.toolTipTimer++;
                    break;
                } else {
                    talentN.toolTipTimer = 0;
                }
            }
        }
    }

    private void drawTalentBackground(Graphics2D g2, int startX, int startY) {
        wholeTalentWindow.x = startX - 65;
        wholeTalentWindow.y = startY - 65;
        //drawing window background
        g2.setColor(lightBackgroundAlpha);
        g2.fillRoundRect(startX - 50, startY - 65, 1200, 700, 35, 35);
        //outline
        g2.setColor(darkBackground);
        g2.setStroke(width5);
        g2.drawRoundRect(startX - 50 + 5, startY - 65 + 5, 1200 - 10, 700 - 10, 30, 30);
        g2.setStroke(width1);
        //window mover
        g2.setColor(darkBackground);
        g2.fillRoundRect(startX - 50, startY - 65 + 2, 1200, 30, 15, 15);
        talentPanelMover.x = startX - 50;
        talentPanelMover.y = startY - 65 + 2 - 10;
        //window close button
        g2.setColor(Color.red);
        g2.drawRoundRect(startX + 1200 - 50 - 30, startY - 65 + 2, 30, 30, 5, 5);
        talentPanelCloser.x = startX - 50 + 500 - 30;
        talentPanelCloser.y = startY - 65 + 2;
    }

    private void drawTalentNodes(Graphics2D g2, int startX, int startY) {
        for (TalentNode node : talent_Nodes) {
            node.drawNode(g2, startX, startY, TALENT_SIZE);
        }
    }


    private void getToolTip(Graphics2D g2, TalentNode talentNode) {

    }


    public void interactWithWindows() {
        if (mg.mouseH.mouse1Pressed && talentPanelMover.contains(mg.motionH.lastMousePosition)) {
            talentPanelX += mg.motionH.lastMousePosition.x - previousMousePosition.x;
            talentPanelY += mg.motionH.lastMousePosition.y - previousMousePosition.y;
        }
        if (mg.mouseH.mouse1Pressed && talentPanelCloser.contains(mg.motionH.lastMousePosition)) {
            mg.showChar = false;
            hideTalentCollision();
        }
        previousMousePosition = mg.motionH.lastMousePosition;
    }


    private void createTalentNodes() {
        talent_Nodes[0] = new TalentNode(new Talent(1, "Increase maximum mana", "ManaTalentIcon.png", "Increases maximum mana by 5%", 23, 21), 300, 300);

    }

    public void resetTalentCollision() {
        wholeTalentWindow.x = lastTalentPosition.x;
        wholeTalentWindow.y = lastTalentPosition.y;
    }

    public void hideTalentCollision() {
        wholeTalentWindow.x = -1000;
        wholeTalentWindow.y = -1000;
    }
}
