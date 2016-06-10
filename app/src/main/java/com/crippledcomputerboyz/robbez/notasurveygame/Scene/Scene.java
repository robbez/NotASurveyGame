package com.crippledcomputerboyz.robbez.notasurveygame.Scene;

/**
 * Created by robbez on 5/23/16.
 */
public class Scene {
    private int sceneResource;
    private String decisionTitle;
    private int leftChoice, rightChoice;

    public int getLeftChoice() {
        return leftChoice;
    }

    public void setLeftChoice(int leftChoice) {
        this.leftChoice = leftChoice;
    }

    public int getRightChoice() {
        return rightChoice;
    }

    public void setRightChoice(int rightChoice) {
        this.rightChoice = rightChoice;
    }

    public Scene(String decisionTitle, int sceneResource, int leftChoice, int rightChoice) {
        this.decisionTitle = decisionTitle;
        this.sceneResource = sceneResource;
        this.leftChoice = leftChoice;
        this.rightChoice = rightChoice;
    }

    public int getSceneResource() {
        return sceneResource;
    }

    public void setSceneResource(int sceneResource) {
        this.sceneResource = sceneResource;
    }

    public String getDecisionTitle() {
        return decisionTitle;
    }

    public void setDecisionTitle(String decisionTitle) {
        this.decisionTitle = decisionTitle;
    }
}
