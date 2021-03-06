package com.example.mdp;

import android.util.Log;

public class Robot implements ICoordinate{
    private int x;
    private int y;
    private String status;
    private char direction;

    // the default robot direction and coordinates
    public Robot(){
        direction = 'N';
        x = -1;
        y = -1;
    }

    @Override
    public int getX() {
        return this.x;
    }

    public void setX(int x){
        this.x = x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    public void setY(int y){ this.y = y; }

    @Override
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean containsCoordinate(int x, int y) {
        Log.d("ROBOT:", "" + this.x + ", " + this.y);
        if (this.x <= x && x <= (this.x + 2) && this.y <= y && y <= (this.y + 2)){
            return true;
        }
        return false;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public char getDirection(){
        return direction;
    }

    public void setDirection(char direction){
        this.direction = direction;
    }

    public void moveRobotForward(){
        // update robot position one block forward in the mapGrid

        char robotDir = getDirection();
        if (this.x != -1 && this.y != -1){
            if(robotDir == 'N'){
                int newY = this.y + 1;
                if (newY <= 18){
                    this.setY(newY);
                }
            }else if (robotDir == 'S'){
                int newY = this.y - 1;
                if (newY >= 1 ){
                    this.setY(newY);
                }
            }else if (robotDir == 'E'){
                int newX = this.x + 1;
                if (newX <= 18){
                    this.setX(newX);
                }
            }else{
                // robotDir == 'W'
                int newX = this.x - 1;
                if (newX >= 1){
                    this.setX(newX);
                }
            }
        }
    }
    public void moveRobotBackward(){
        // update robot position one block forward in the mapGrid

        char robotDir = getDirection();
        if (this.x != -1 && this.y != -1){
            if(robotDir == 'S'){
                int newY = this.y + 1;
                if (newY <= 18){
                    this.setY(newY);
                }
            }else if (robotDir == 'N'){
                int newY = this.y - 1;
                if (newY >= 1 ){
                    this.setY(newY);
                }
            }else if (robotDir == 'W'){
                int newX = this.x + 1;
                if (newX <= 18){
                    this.setX(newX);
                }
            }else{
                // robotDir == 'W'
                int newX = this.x - 1;
                if (newX >= 1){
                    this.setX(newX);
                }
            }
        }
    }
    public void moveRobotTurnLeft(){
        if (this.x != -1 && this.y != -1){
            char robotDir = this.getDirection();
            if (robotDir == 'N'){
                this.setDirection('W');
            }
            else if (robotDir == 'S'){
                this.setDirection('E');
            }
            else if (robotDir == 'E'){
                this.setDirection('N');
            }
            else{
                // robotDir == 'W'
                this.setDirection('S');
            }
        }
    }

    public void moveRobotTurnRight(){
        if (this.x != -1 && this.y != -1){
            char robotDir = this.getDirection();
            if (robotDir == 'N'){
                this.setDirection('E');
            }
            else if (robotDir == 'S'){
                this.setDirection('W');
            }
            else if (robotDir == 'E'){
                this.setDirection('S');
            }
            else{
                // robotDir == 'W'
                this.setDirection('N');
            }
        }
    }

    public void reset(){
        setCoordinates(-1,-1);
        this.direction = 'N';
    }

}
