package com.example.a1201509_ahmad_bakrit3;

/* Ahmad Bakri 1201509 */

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {
    ImageView sun, earth, cloud1, cloud2, Car, Earth, Rock, trafficLight;
    // To control the rotate of the car around the earth, and the path of car
    private ObjectAnimator PathOfCar, RotationOfCar;
    // The duration for every round
    private static final long RoundDuration = 12000;
    float RowMove, ColumnMove, RowCenter, ColumnCenter, Radius, initialRow, initialColumn, carX, carY, rockX, rockY;
    double FirstPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sun = findViewById(R.id.Sun);
        earth = findViewById(R.id.Earth);
        cloud1 = findViewById(R.id.Cloud1);
        cloud2 = findViewById(R.id.Cloud2);
        trafficLight = findViewById(R.id.TrafficLight);
        //((AnimationDrawable) trafficLight.getDrawable()).start();

        Car = findViewById(R.id.Car);
        Earth = findViewById(R.id.Earth);
        Rock = findViewById(R.id.Rock);

        /* Call the functions to start the animation */
        Sun_Movement();
        Clouds_Movement();
        Earth.post(this::Car_Movement);
        Traffic_Light();
    }

    private void Sun_Movement() {
        Animation sunRotate = AnimationUtils.loadAnimation(this, R.anim.sun_move);
        sun.startAnimation(sunRotate);
    }
    private void Clouds_Movement() {
        Animation cloud1Move = AnimationUtils.loadAnimation(this, R.anim.cloud1move);
        cloud1.startAnimation(cloud1Move);

        Animation cloud2Move = AnimationUtils.loadAnimation(this, R.anim.cloud2move);
        cloud2.startAnimation(cloud2Move);
    }
    private void Traffic_Light() {
        AnimationDrawable trafficLightAnimation = (AnimationDrawable) trafficLight.getDrawable();
        trafficLight.post(new Runnable() {
            @Override
            public void run() {
                trafficLightAnimation.start();
            }
        });
    }

    private void Car_Movement() {
        /* Coordinates to make the car rotate correctly on earth surface (manually) */
        RowMove = 90f;
        ColumnMove = 20f;
        RowCenter = Earth.getX() + Earth.getWidth() / 7 + RowMove;
        ColumnCenter = Earth.getY() + Earth.getHeight() / 7 + ColumnMove;
        Radius = (Earth.getWidth()) - (Car.getWidth()) - 40;

        /* To center car at the top of earth (manually) */
        Path path = new Path();
        final RectF oval = new RectF(RowCenter - Radius, ColumnCenter - Radius, RowCenter + Radius, ColumnCenter + Radius);
        path.addArc(oval, -90, 340);

        PathOfCar = ObjectAnimator.ofFloat(Car, View.X, View.Y, path);
        PathOfCar.setDuration(RoundDuration);
        PathOfCar.setInterpolator(new LinearInterpolator());
        PathOfCar.setRepeatCount(ObjectAnimator.INFINITE);

        /* Calculation to to put the car initial position directly on the path (manually)*/
        FirstPosition = Math.toRadians(270);
        initialRow = (float)(RowCenter + Radius * Math.cos(FirstPosition));
        initialColumn = (float)(ColumnCenter + Radius * Math.sin(FirstPosition));
        Car.setX(initialRow - Car.getWidth() / 7f + 30);
        Car.setY(initialColumn - Car.getHeight() / 7f + 50);
        Car.setPivotX(Car.getWidth() / 2f);
        Car.setPivotY(Car.getHeight() / 2f);

        /* Control rotation of the car */
        RotationOfCar = ObjectAnimator.ofFloat(Car, "rotation", 0f, 360f);
        RotationOfCar.setDuration(RoundDuration);
        RotationOfCar.setInterpolator(new LinearInterpolator());
        RotationOfCar.setRepeatCount(ObjectAnimator.INFINITE);

        /* Make a pause to stop the car on traffic light (red) at the beginning of the program*/
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            PathOfCar.start();
            RotationOfCar.start();
        }, 5205);


        /* because of the change of the car speed, the first round duration is differ from another rounds
           because the speed is reduced by half (calculation for first round, and another rounds) (manually)*/
        Handler cycleHandler = new Handler(Looper.getMainLooper());
        Runnable stopAndStartCycle = new Runnable() {
            private boolean FirstRound = true;

            @Override
            public void run() {

                PathOfCar.pause();
                RotationOfCar.pause();

                long nextStopDelay = FirstRound ? 5000 : 4100;

                cycleHandler.postDelayed(() -> {
                    PathOfCar.resume();
                    RotationOfCar.resume();

                    long delayForNextCycle = 24000;
                    cycleHandler.postDelayed(this, delayForNextCycle);
                    FirstRound = false;
                }, nextStopDelay);
            }
        };
        cycleHandler.postDelayed(stopAndStartCycle, 29400);

        /* This method to make interact between the car and rock, when the car hit the rock the speed of the car reduced by half
            and the car bounces back due to the collision, and the rock fall to the down left corner */
        PathOfCar.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Collision(Car, Rock, 100)) {
                    
                    PathOfCar.setDuration(RoundDuration * 2);
                    RotationOfCar.setDuration(RoundDuration * 2);

                    RockMovement();
                    animation.removeAllUpdateListeners();


                }
            }
        });
//        PathOfCar.resume();
//        RotationOfCar.resume();
    }




    /* This method to check the distance between the car and rock (manually) */
    private boolean Collision(View car, View rock, float StandardPoint) {
         carX = car.getX() + car.getWidth() / 2;
         carY = car.getY() + car.getHeight() / 2;
         rockX = rock.getX() + rock.getWidth() / 2;
         rockY = rock.getY() + rock.getHeight() / 2;

        return Math.hypot(rockX - carX, rockY - carY) < StandardPoint;
    }

    /* This method control the movement of the rock when the car hit it,
        and set the position at the down left corner */
    private void RockMovement() {
        if (Rock != null) {
            final float screenWidth = getResources().getDisplayMetrics().widthPixels;
            final float screenHeight = getResources().getDisplayMetrics().heightPixels;

            /* Calculation to put the rock at the down left corner */
            final float offsetRight = 150;
            final float targetX = -Rock.getLeft() - Rock.getWidth() + offsetRight;
            final float targetY = screenHeight - Rock.getTop() - Rock.getHeight();
            ObjectAnimator rockFallX = ObjectAnimator.ofFloat(Rock, "translationX", targetX);
            ObjectAnimator rockFallY = ObjectAnimator.ofFloat(Rock, "translationY", targetY);
            ObjectAnimator rockRotate = ObjectAnimator.ofFloat(Rock, "rotation", 0f, 180f);

            /* The duration of the rock falling to corner (manually)*/
            AnimatorSet rockAnimatorSet = new AnimatorSet();
            rockAnimatorSet.playTogether(rockFallX, rockFallY, rockRotate);
            rockAnimatorSet.setDuration(3000);

            /* this method determine the position of the rock (down left corner) (manually)*/
            rockAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Rock.setX(offsetRight);
                    Rock.setY(screenHeight - Rock.getHeight());
                    Rock.setRotation(180f);
                    Rock.setX(0);
                    Rock.setY(1820);
                    Rock.requestLayout();
                }
            });
            rockAnimatorSet.start();
        }
    }
}