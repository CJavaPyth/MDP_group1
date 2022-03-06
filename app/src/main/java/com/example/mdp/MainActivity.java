package com.example.mdp;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mdp.bluetoothchat.BluetoothChatFragment;
import com.example.mdp.bluetoothchat.BluetoothChatService;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static Robot robot = new Robot();
    MutableLiveData<String> listen = new MutableLiveData<>();
    public static TextView txtX;
    public static TextView txtY;
    public static TextView txtDir;
    public static TextView txtRobotStatus;
    public static TextView txtImage;
    public static Button obstacleBtn;
    private static MapGrid mapGrid;
    BluetoothChatFragment fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listen.setValue("Default");
        // Drawing of map grid
        mapGrid = findViewById(R.id.map);

        // Update Robot Pos
        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txtY);

        // Update Robot Direction
        txtDir = findViewById(R.id.txtDirection);

        // Update Robot Status
        txtRobotStatus = findViewById(R.id.txtRobotStatus);

        txtImage = findViewById(R.id.txtImage);

        obstacleBtn = findViewById(R.id.btnSendObstacleData);

        // Remove shadow of action bar
        // getSupportActionBar().setElevation(0);

        // Set layout to shift up when keyboard is open
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establish BT connection
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }



        findViewById(R.id.btnSendObstacleData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Obstacle> obstaclesData = Map.getInstance().getObstacles();
                // Send message 'tr' via BT
                // ;
                String obstacles = new Gson().toJson(obstaclesData);
                Toast.makeText(MainActivity.this, obstacles,
                        Toast.LENGTH_SHORT).show();
                //String obstacleString = TextUtils.join(",", obstaclesData);
                outgoingMessage("STATE," + obstacles);
                mapGrid.invalidate();
                //fragment.sendMsg("f");
                //printMessage("F|");
//                    String navi = "F|";
//                    byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                    BluetoothChatService.write(bytes);

                // Show Popup message
            }
        });


        // To move forward
        findViewById(R.id.btnUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robot.moveRobotForward();
                mapGrid.invalidate();
                String navi = null;
                navi = "MOVE,f";

                // Send message 'tr' via BT
                outgoingMessage(navi);
                //fragment.sendMsg("f");
                //printMessage("F|");
//                    String navi = "F|";
//                    byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                    BluetoothChatService.write(bytes);

                // Show Popup message
                Toast.makeText(MainActivity.this, "Move forward",
                        Toast.LENGTH_SHORT).show();
                if (robot.getX() != -1 && robot.getY() != -1) {

                    // Show coordinates and direction in textView
                    txtX.setText(String.valueOf(robot.getX()));
                    txtY.setText(String.valueOf(robot.getY()));
                    txtDir.setText(String.valueOf(robot.getDirection()));
                } else {

                    // Show -- in textView
                    txtX.setText("-");
                    txtY.setText("-");
                    txtDir.setText("-");
                }

            }
        });


        // To move backwards
        findViewById(R.id.btnDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robot.moveRobotBackward();
                mapGrid.invalidate();
                String navi = null;
                navi = "MOVE,b";

                // Send message 'tr' via BT
                outgoingMessage(navi);
                //fragment.sendMsg("f");
                //printMessage("F|");
//                    String navi = "F|";
//                    byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                    BluetoothChatService.write(bytes);

                // Show Popup message
                Toast.makeText(MainActivity.this, "Move backward",
                        Toast.LENGTH_SHORT).show();
                if (robot.getX() != -1 && robot.getY() != -1) {

                    // Show coordinates and direction in textView
                    txtX.setText(String.valueOf(robot.getX()));
                    txtY.setText(String.valueOf(robot.getY()));
                    txtDir.setText(String.valueOf(robot.getDirection()));
                } else {

                    // Show -- in textView
                    txtX.setText("-");
                    txtY.setText("-");
                    txtDir.setText("-");
                }

            }
        });

        // Turn left
        findViewById(R.id.btnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robot.moveRobotTurnLeft();
                mapGrid.invalidate();
                String navi = null;
                navi = "MOVE,l";

                // Send message 'tr' via BT
                outgoingMessage(navi);
                //ragment.sendMsg("tl");
                //printMessage("L|");
//                String navi = "L|";
//                byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                BluetoothChatService.write(bytes);

                // Show Popup message
                Toast.makeText(MainActivity.this, "Turn Left",
                        Toast.LENGTH_SHORT).show();
                if (robot.getX() != -1 && robot.getY() != -1){

                    // Show coordinates and direction in textView
                    txtX.setText(String.valueOf(robot.getX()));
                    txtY.setText(String.valueOf(robot.getY()));
                    txtDir.setText(String.valueOf(robot.getDirection()));
                }else{

                    // Show -- in textView
                    txtX.setText("-");
                    txtY.setText("-");
                    txtDir.setText("-");
                }
            }
        });

        findViewById(R.id.btnCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String navi = null;
                navi = "MOVE,h";

                // Send message 'tr' via BT
                outgoingMessage(navi);
                //ragment.sendMsg("tl");
                //printMessage("L|");
//                String navi = "L|";
//                byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                BluetoothChatService.write(bytes);

                // Show Popup message
                Toast.makeText(MainActivity.this, "Stop",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Turn right
        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robot.moveRobotTurnRight();
                mapGrid.invalidate();
                String navi = null;
                navi = "MOVE,r";

                // Send message 'tr' via BT
                outgoingMessage(navi);
                //fragment.sendMsg("tr");
                //printMessage("R|");
//                String navi = "R|";
//                byte[] bytes = navi.getBytes(Charset.defaultCharset());
//                BluetoothChatService.write(bytes);

                // Show Popup message
                Toast.makeText(MainActivity.this, "Turn Right",
                        Toast.LENGTH_SHORT).show();
                if (robot.getX() != -1 && robot.getY() != -1){

                    // Show coordinates and direction in textView
                    txtX.setText(String.valueOf(robot.getX()));
                    txtY.setText(String.valueOf(robot.getY()));
                    txtDir.setText(String.valueOf(robot.getDirection()));
                }else{

                    // Show -- in textView
                    txtX.setText("-");
                    txtY.setText("-");
                    txtDir.setText("-");
                }
            }
        });





//        listen.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if(s == "Move"){
//                    if (robot.getX() != -1 && robot.getY() != -1) {
//                        robot.moveRobotForward();
//                        mapGrid.invalidate();
//                        txtX.setText(String.valueOf(robot.getX()));
//                        txtY.setText(String.valueOf(robot.getY()));
//                        txtDir.setText(String.valueOf(robot.getDirection()));
//                    }
//                    Log.d("MainActivity", "MOVE ");
//                }else if (s=="Left"){
//                    if (robot.getX() != -1 && robot.getY() != -1) {
//                        robot.moveRobotTurnLeft();
//                        mapGrid.invalidate();
//                        txtX.setText(String.valueOf(robot.getX()));
//                        txtY.setText(String.valueOf(robot.getY()));
//                        txtDir.setText(String.valueOf(robot.getDirection()));
//                    }
//                    Log.d("MainActivity", "LEFT");
//                }else{
//                    Log.d("MainActivity", "CHANGE VALUE: "+s);
//                }
//            }
//        });
    }

    public void outgoingMessage(String sendMsg) {
        fragment.sendMsg(sendMsg);
        //Toast.makeText(getApplicationContext(),sendMsg,Toast.LENGTH_SHORT).show();
    }

    public static void showObstaclePopup(Context c, View view, Obstacle obstacle) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.obstacle_popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        Button btnN = (Button) popupView.findViewById(R.id.obstacleN);
        Button btnS = (Button) popupView.findViewById(R.id.obstacleS);
        Button btnE = (Button) popupView.findViewById(R.id.obstacleE);
        Button btnW = (Button) popupView.findViewById(R.id.obstacleW);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        MapGrid mapGrid = view.findViewById(R.id.map);

        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapGrid.setObstacleSide(obstacle, 'N');
                popupWindow.dismiss();
            }
        });

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapGrid.setObstacleSide(obstacle, 'S');
                popupWindow.dismiss();
            }
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapGrid.setObstacleSide(obstacle, 'E');
                popupWindow.dismiss();
            }
        });

        btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapGrid.setObstacleSide(obstacle, 'W');
                popupWindow.dismiss();
            }
        });

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public static void updateNewCoordinate(int up, int down, int left, int right, char direction) {
        int oldX = robot.getX();
        int oldY = robot.getY();
        int newX = oldX + right - left;
        int newY = oldY + up - down;
        setRobotPosition(newX, newY, direction);
    }

    public static void setRobotPosition(int x, int y, char direction){
        if (1 <= x && x <= 18 && 1 <= y && y <= 18 && (direction == 'N' || direction == 'S' || direction == 'E' || direction == 'W')){
            robot.setCoordinates(x, y);
            robot.setDirection(direction);
            txtX.setText(String.valueOf(robot.getX()));
            txtY.setText(String.valueOf(robot.getY()));
            txtDir.setText(String.valueOf(robot.getDirection()));
            mapGrid.invalidate();
            //return true;
        }
        //return false;
    }

    public static boolean exploreTarget(int x, int y, int targetID){
        // if obstacle number exists in map, reduce the biggest obstacle number by 1
        for (Obstacle obstacle :  Map.getInstance().getObstacles()) {
            if (obstacle.getX() == x && obstacle.getY() == y) {
                obstacle.explore(targetID);
                mapGrid.invalidate();
                return true;
            }
        }
        return false;
    }

    public static void updateRobotStatus(String status){
        // show robot status in textView
        robot.setStatus(status);
        txtRobotStatus.setText(robot.getStatus());
    }

    public static void moveRobot(String movement) {
        Log.d(TAG, "moveRobot: "+ movement);
        if (movement == "l") {
            robot.moveRobotTurnLeft();
            mapGrid.invalidate();
            if (robot.getX() != -1 && robot.getY() != -1) {

                // Show coordinates and direction in textView
                txtX.setText(String.valueOf(robot.getX()));
                txtY.setText(String.valueOf(robot.getY()));
                txtDir.setText(String.valueOf(robot.getDirection()));
            } else {

                // Show -- in textView
                txtX.setText("-");
                txtY.setText("-");
                txtDir.setText("-");
            }
        } else if (movement == "r") {
            robot.moveRobotTurnRight();
            mapGrid.invalidate();
            if (robot.getX() != -1 && robot.getY() != -1) {

                // Show coordinates and direction in textView
                txtX.setText(String.valueOf(robot.getX()));
                txtY.setText(String.valueOf(robot.getY()));
                txtDir.setText(String.valueOf(robot.getDirection()));
            } else {

                // Show -- in textView
                txtX.setText("-");
                txtY.setText("-");
                txtDir.setText("-");
            }
        } else if (movement == "f") {
            robot.moveRobotForward();
            mapGrid.invalidate();
            if (robot.getX() != -1 && robot.getY() != -1) {

                // Show coordinates and direction in textView
                txtX.setText(String.valueOf(robot.getX()));
                txtY.setText(String.valueOf(robot.getY()));
                txtDir.setText(String.valueOf(robot.getDirection()));
            } else {

                // Show -- in textView
                txtX.setText("-");
                txtY.setText("-");
                txtDir.setText("-");
            }
        } else {
            robot.moveRobotBackward();
            mapGrid.invalidate();
            if (robot.getX() != -1 && robot.getY() != -1) {

                // Show coordinates and direction in textView
                txtX.setText(String.valueOf(robot.getX()));
                txtY.setText(String.valueOf(robot.getY()));
                txtDir.setText(String.valueOf(robot.getDirection()));
            } else {

                // Show -- in textView
                txtX.setText("-");
                txtY.setText("-");
                txtDir.setText("-");
            }
        }
    }

    public static void updateImage(String imageId){
        // show robot status in textView
        txtImage.setText(imageId);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //gyroscope.register();
//    }

//    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.main, menu);
//////        Resources res = getResources();
//////        String[] menuOptions = res.getStringArray(R.array.bluetooth_menu);
//////        for (int i = 0; i<menuOptions.length; i++){
//////            menu.add(0, i, 0, menuOptions[i]).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//////        }
////        return true;
////    }

}