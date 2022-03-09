package com.example.mdp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MapGrid extends View {
    // dimensions of canvas
    private int width;
    private int height;
    private float cellHeight;
    private float cellWidth;
    private float offsetX;
    private float offsetY;
    private float sidebar;
    private float obstacleSideWidth = 5;

    // grid properties
    private static final int numColumns = 20;
    private static final int numRows = 20;
    private static final int padding = 20;
    private static final int border = 5;

    // Paint - coloring
    private final Paint bluePaint = new Paint();
    private final Paint blackPaint = new Paint();
    private final Paint coordinatesPaint = new Paint();
    private final Paint coordNumber = new Paint();
    private final Paint yellowPaint = new Paint();
    private final Paint exploredWhiteNumber = new Paint();

    // Images
    private final Bitmap robotBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.robot);
    private final Bitmap robotBoxBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.robot_blank);
    private final Bitmap obstacleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cone);

    // Handle motion
    private int initialX;
    private int initialY;
    private ICoordinate objectToMove;

    // Sidebar buttons
    public static final int sideBarLeft = 22;
    public static final int sideBarRight = 22 + 2;
    public static final int robotBoxBottom = 18;
    public static final int robotBoxTop = 18 + 2;
    public static final int obstacleBoxBottom = 16 - 2;
    public static final int obstacleBoxTop = 16;

    // BluetoothChatFragment for sending strings via bluetooth
    //private final BluetoothChatFragment fragment = new BluetoothChatFragment();

    public MapGrid(Context context){
        this(context, null);
    }

    public MapGrid(Context context, AttributeSet attrs){
        super(context, attrs);
        bluePaint.setColor(Color.parseColor("#4DBAD8"));
        bluePaint.setShadowLayer(border, 0, 0, Color.GRAY);
        blackPaint.setColor(Color.BLACK);
        coordinatesPaint.setColor(Color.WHITE);
        coordinatesPaint.setTextSize(20);
        coordinatesPaint.setTextAlign(Paint.Align.CENTER);
        coordNumber.setColor(Color.BLACK);
        coordNumber.setTextSize(18);
        coordNumber.setTextAlign(Paint.Align.CENTER);
        yellowPaint.setColor(Color.YELLOW);

        //?
        exploredWhiteNumber.setColor(Color.WHITE);
        exploredWhiteNumber.setTextSize(18);
        exploredWhiteNumber.setTextAlign(Paint.Align.CENTER);
    }
                                                              
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        calculateDimensions();
        // draw white area of canvas
        canvas.drawRoundRect(border, border, width - border - sidebar, height - border, 20, 20, bluePaint);

//        canvas.drawText("Identified:",700,450, blackPaint);
//        canvas.drawText("Obstacle 1 ",700,480, blackPaint);


        // draw obstacle box
        drawObstacleBox(canvas);

        // draw obstacles
        drawObstacles(canvas);

        // draw grid lines and coordinates
        drawCoordinates(canvas);

        // draw robot
        drawRobot(canvas, MainActivity.robot.getX(), MainActivity.robot.getY(), MainActivity.robot.getDirection());

    }

    private void calculateDimensions(){
        int sidebarNumOfCells = 4;
        this.width = getWidth();
        this.height = getHeight();
//        this.cellWidth = (float) (width - padding*2 - border*2) / (numColumns + 1);
        this.cellWidth = (float) (width - padding*2 - border*2) / (numColumns + sidebarNumOfCells + 1);
        this.cellHeight = (float) (height - padding*2 - border*2) / (numRows + 1);
        this.offsetX = padding + border + cellWidth;
        this.offsetY = padding + border;
        this.sidebar = sidebarNumOfCells * cellWidth;
    }

    private void drawCoordinates(Canvas canvas){

        float offsetX = padding + border + cellWidth;
        float offsetY = padding + border;
        for (int i = 0; i <= numColumns; i++){
            canvas.drawLine(offsetX + i * cellWidth, offsetY, offsetX + i * cellWidth, offsetY + cellHeight * numRows, blackPaint);
        }

        for (int i = 0; i <= numRows; i++){
            canvas.drawLine(offsetX, offsetY + i * cellHeight, offsetX + cellWidth * (numColumns), offsetY + i * cellHeight, blackPaint);
        }

        float textSize = this.coordinatesPaint.getTextSize();
//        canvas.drawText("0", offsetX - this.cellWidth/2, offsetY + this.cellHeight * (float) (numRows + 0.5), this.coordinatesPaint);
        for (int i = 1; i <= numColumns; i++){
            canvas.drawText(String.valueOf(i), (float) (offsetX + this.cellWidth * (i - 0.5)), offsetY + this.cellHeight * (float) (numRows + 0.7), this.coordinatesPaint);
        }
        for (int i = 1; i <= numRows; i++){
            canvas.drawText(String.valueOf(i), offsetX - this.cellWidth/2, (float) (offsetY + this.cellHeight * (numRows - i + 0.5) + textSize/2), this.coordinatesPaint);
        }
    }

    private void drawRobot(Canvas canvas, int col, int row, char direction){
        // Draw robot on the mapGrid
        Matrix robotBoxMatrix = new Matrix();
        Bitmap robotBoxBitmap = Bitmap.createBitmap(this.robotBoxBitmap,0,0, this.robotBoxBitmap.getWidth(), this.robotBoxBitmap.getHeight(), robotBoxMatrix, true);
        Log.d("MainActivity", "height:"+this.robotBoxBitmap.getHeight()+", width:"+this.robotBoxBitmap.getWidth());
        canvas.drawBitmap(robotBoxBitmap, null, new RectF(offsetX + (sideBarLeft - 1) * cellWidth, offsetY + (numRows - robotBoxTop) * cellHeight, offsetX + sideBarRight * cellWidth,offsetY + (numRows - robotBoxBottom + 1) * cellHeight), null);

        if (row == -1 || col == -1){
            row = 18;
            col = 22;
        }
        int deg = 0;
        if (direction == 'S'){
            deg = 180;
        }else if (direction == 'E'){
            deg = 90;
        }else if (direction == 'W'){
            deg = 270;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(deg);

        Bitmap rotatedBitmap = Bitmap.createBitmap(robotBitmap,0,0, robotBitmap.getWidth(), robotBitmap.getHeight(), matrix, true);
        canvas.drawBitmap(rotatedBitmap, null, new RectF(offsetX + (col - 1) * cellWidth, offsetY + (numRows - row- 2) * cellHeight, offsetX + (col + 2) * cellWidth,offsetY + (numRows - row + 1) * cellHeight), null);

        //canvas.drawBitmap(robotBitmap, null, new RectF(offsetX + (row - 1) * cellWidth, offsetY + (19 - col+1) * cellHeight, offsetX + (row) * cellWidth,offsetY + ((19 - col)+2) * cellHeight), bluePaint);
    }

    private void drawObstacles(Canvas canvas){
        // Colour obstacle side with yellow colour on the mapGrid

        float textSize = this.coordNumber.getTextSize();
        Map map = Map.getInstance();
        for (Obstacle obstacle: map.getObstacles()){
            int x = obstacle.getX();
            int y = obstacle.getY();
            canvas.drawRect(offsetX + (x - 1) * cellWidth, offsetY + cellHeight * (numRows - y), offsetX + x * cellWidth, offsetY + cellHeight * (numRows - y + 1), blackPaint);

            //?
            if (obstacle.isExplored()){
                int len = obstacle.getTargetID().length();
                if (len >= 3){
                     len = 3;
                }
                String display = obstacle.getTargetID().substring(0,len);
                canvas.drawText(display, offsetX + (float) (x - 1 + 0.5) * cellWidth, offsetY + cellHeight * (numRows - y) + (cellHeight - textSize) / 2 + textSize, exploredWhiteNumber);
            } else{
                canvas.drawText(String.valueOf(obstacle.getNumber()), offsetX + (float) (x - 1 + 0.5) * cellWidth, offsetY + cellHeight * (numRows - y) + (cellHeight - textSize)/2 + textSize, coordNumber);
            }
            switch (obstacle.getSide()){
                case 'N':
                    canvas.drawRect(offsetX + (x - 1) * cellWidth, offsetY + cellHeight * (numRows - y), offsetX + x * cellWidth, offsetY + cellHeight * (numRows - y) + obstacleSideWidth, yellowPaint);
                    break;
                case 'S':
                    canvas.drawRect(offsetX + (x - 1) * cellWidth, offsetY + cellHeight * (numRows - y + 1) - obstacleSideWidth, offsetX + x * cellWidth, offsetY + cellHeight * (numRows - y + 1), yellowPaint);
                    break;
                case 'E':
                    canvas.drawRect(offsetX + x * cellWidth - obstacleSideWidth, offsetY + cellHeight * (numRows - y), offsetX + x * cellWidth, offsetY + cellHeight * (numRows - y + 1), yellowPaint);
                    break;
                case 'W':
                    canvas.drawRect(offsetX + (x - 1) * cellWidth, offsetY + cellHeight * (numRows - y), offsetX + (x - 1) * cellWidth + obstacleSideWidth, offsetY + cellHeight * (numRows - y + 1), yellowPaint);
                    break;
                default:
                    break;
            }
        }
    }

    private void drawObstacleBox(Canvas canvas){
        // Draw obstacle on the mapGrid

        Matrix matrix = new Matrix();
        Bitmap bm = Bitmap.createBitmap(obstacleBitmap,0,0, obstacleBitmap.getWidth(), obstacleBitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bm, null, new RectF(offsetX + (sideBarLeft - 1) * cellWidth, offsetY + (numRows - obstacleBoxTop) * cellHeight, offsetX + sideBarRight * cellWidth,offsetY + (numRows - obstacleBoxBottom + 1) * cellHeight), null);
    }

    // Gesture detector for handling long presses
    final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent event) {
            int selectedX = (int) (((event.getX() - offsetX)/cellWidth) + 1);
            int selectedY = (int) (numRows - ((event.getY() - offsetY)/cellHeight) + 1);
            ICoordinate obstacle = Map.getInstance().findObstacle(selectedX, selectedY);
            if (obstacle != null){
                MainActivity.showObstaclePopup(getContext(), getRootView(), (Obstacle) obstacle);
            }
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("MainActivity", "ROBOT = row:"+robot.getRow()+", col:"+robot.getCol());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                objectToMove = null;
                initialX = (int) (((event.getX() - offsetX)/cellWidth) + 1);
                initialY = (int) (numRows - ((event.getY() - offsetY)/cellHeight) + 1);

                // If touch robot that is on the map
                if (MainActivity.robot.containsCoordinate(initialX, initialY)){
                    objectToMove = MainActivity.robot;
                // Robot not on map and touch robot button
                } else if ((MainActivity.robot.getX() == -1 || MainActivity.robot.getY() == -1) && (sideBarLeft <= initialX && initialX <= sideBarRight && robotBoxBottom <= initialY && initialY <= robotBoxTop)){
                    objectToMove = MainActivity.robot;
                // If touch obstacle button
                } else if (sideBarLeft <= initialX && initialX <= sideBarRight && obstacleBoxBottom <= initialY && initialY <= obstacleBoxTop){
                    objectToMove = Map.getInstance().addObstacle();
                } else {
                    objectToMove = Map.getInstance().findObstacle(initialX, initialY);
                }
                Log.d("MainActivity", "Current coordinates= row:" + initialX + ", col:" + initialY);
                break;
            case MotionEvent.ACTION_MOVE:
                int movingX = (int) (((event.getX() - offsetX)/cellWidth) + 1);
                int movingY = (int) (numRows - ((event.getY() - offsetY)/cellHeight) + 1);
                if (objectToMove instanceof Robot){
                    if ((movingX >= 1 && movingX <= 18) && (movingY >= 1 && movingY <= 18)){

                        // update robot coordinates in textView
                        Log.d("MainActivity", "Moving coordinates= X:" + movingX +", Y:" + movingY);
                        MainActivity.robot.setCoordinates(movingX, movingY);
                        MainActivity.txtX.setText(String.valueOf(MainActivity.robot.getX()));
                        MainActivity.txtY.setText(String.valueOf(MainActivity.robot.getY()));
                        MainActivity.txtDir.setText(String.valueOf(MainActivity.robot.getDirection()));
                        invalidate();
                    }
                } else if (objectToMove instanceof Obstacle){
                    if ((movingX >= 1 && movingX <= numColumns) && (movingY >= 1 && movingY <= numRows)){
                        if (Map.getInstance().findObstacle(movingX, movingY) == null){

                            // set object coordinates to new position after it has been moved
                            objectToMove.setCoordinates(movingX, movingY);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int finalX = (int) (((event.getX() - offsetX)/cellWidth) + 1);
                int finalY = (int) (numRows - ((event.getY() - offsetY)/cellHeight) + 1);
                // when robot is drag and drop
                if (objectToMove instanceof Robot){
                    MainActivity ma = (MainActivity) this.getContext();
                    ma.outgoingMessage("Coordinates " + ": (" + MainActivity.robot.getX() + ", " + MainActivity.robot.getY() + ")");
                    //fragment.sendMsg("Coordinates " + ": (" + MainActivity.robot.getX() + ", " + MainActivity.robot.getY() + ")");
                    if ((initialX == MainActivity.robot.getX() && initialY == MainActivity.robot.getY())
                            || (initialX == MainActivity.robot.getX() && initialY == MainActivity.robot.getY()+1)
                            || (initialX == MainActivity.robot.getX() && initialY == MainActivity.robot.getY()+2)
                            || (initialX == MainActivity.robot.getX()+1 && initialY == MainActivity.robot.getY())
                            || (initialX == MainActivity.robot.getX()+1 && initialY == MainActivity.robot.getY()+1)
                            || (initialX == MainActivity.robot.getX()+1 && initialY == MainActivity.robot.getY()+2)
                            || (initialX == MainActivity.robot.getX()+2 && initialY == MainActivity.robot.getY())
                            || (initialX == MainActivity.robot.getX()+2 && initialY == MainActivity.robot.getY()+1)
                            || (initialX == MainActivity.robot.getX()+2 && initialY == MainActivity.robot.getY()+2)){
                        if ((finalX < 1 || finalX > 18) || (finalY < 1 || finalY > 18)){

                            // reset robot coordinates if it has been placed outside of map
                            MainActivity.robot.reset();
                            MainActivity.txtX.setText("-");
                            MainActivity.txtY.setText("-");
                            MainActivity.txtDir.setText("-");
                        }
                        else{
                            MainActivity.robot.setCoordinates(finalX, finalY);
                            MainActivity.txtX.setText(String.valueOf(MainActivity.robot.getX()));
                            MainActivity.txtY.setText(String.valueOf(MainActivity.robot.getY()));
                            MainActivity.txtDir.setText(String.valueOf(MainActivity.robot.getDirection()));
                            Log.d("MainActivity", "ROBOT = row:"+MainActivity.robot.getX()+", col:"+MainActivity.robot.getY());
                            Log.d("MainActivity", "From ("+initialX+","+initialY+") to ("+finalX+","+finalY+")"+"\n");
                        }
                        invalidate();
                    }
                } else if (objectToMove instanceof Obstacle){
                    if ((finalX < 1 || finalX > numColumns) || (finalY < 1 || finalY > numRows)){
                        Map.getInstance().removeObstacle((Obstacle) objectToMove);
                        MainActivity ma = (MainActivity) this.getContext();
                        //ma.outgoingMessage("Removed Obstacle " + ((Obstacle) objectToMove).getNumber());
                        //fragment.sendMsg("Removed Obstacle " + ((Obstacle) objectToMove).getNumber());
                    } else {
                        // If finger is released at a square
                        if (!Map.getInstance().isOccupied(finalX, finalY, (Obstacle) objectToMove)) {
                            objectToMove.setCoordinates(finalX, finalY);
                        }
                        MainActivity ma = (MainActivity) this.getContext();
                        //ma.outgoingMessage("Obstacle " + ((Obstacle) objectToMove).getNumber() + ": (" + objectToMove.getX() + ", " + objectToMove.getY() + ")");
                        //fragment.sendMsg("Obstacle " + ((Obstacle) objectToMove).getNumber() + ": (" + objectToMove.getX() + ", " + objectToMove.getY() + ")");
                    }
                    invalidate();
                }
                break;
        }
        // handle long presses on obstacles
        gestureDetector.onTouchEvent(event);
        return true;
    }


    public void setObstacleSide(Obstacle obstacle, char side){
        obstacle.setSide(side);
        invalidate();
        MainActivity ma = (MainActivity) this.getContext();
        //ma.outgoingMessage("Obstacle " + obstacle.getNumber() + ": (" + obstacle.getX() + ", " + obstacle.getY() + "), " + obstacle.getSide());
        //fragment.sendMsg("Obstacle " + obstacle.getNumber() + ": (" + obstacle.getX() + ", " + obstacle.getY() + "), " + obstacle.getSide());
    }

}
