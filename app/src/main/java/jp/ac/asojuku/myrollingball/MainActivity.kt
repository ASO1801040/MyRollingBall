package jp.ac.asojuku.myrollingball

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity(),SensorEventListener,SurfaceHolder.Callback {

    private var surfaceWidth: Int = 0
    private var surfaceHeight: Int = 0

    private val radius = 50.0f
    private val coef = 1000.0f

    private var ballX:Float = 0f
    private var ballY:Float = 0f

    private var ballObj1X:Float = 500f
    private var ballObj1Y:Float = 500f
    private var ballObj1radius = 50.0f

    private var ballGoalX:Float = 800f
    private var ballGoalY:Float = 200f
    private var ballGoalRadius = 50.0f


    private var squareObj1XL:Float = 300f
    private var squareObj1YT:Float = 0f
    private var squareObj1XR:Float = 350f
    private var squareObj1YB:Float = 700f

    private var squareObj2XL:Float = 300f
    private var squareObj2YT:Float = 1000f
    private var squareObj2XR:Float = 350f
    private var squareObj2YB:Float = 0f



    private var vx:Float = 0f
    private var vy:Float = 0f
    private var time:Long = 0L

    private var flag = false
    private var flag2 = false



    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        this.surfaceWidth = width
        this.surfaceHeight = height
        this.ballX = 100f
        this.ballY = 100f
        this.squareObj2YB = height.toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)

    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.registerListener(
            this,
            accSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }
    private fun drawCanvas(){
        val canvas = surfaceView.holder.lockCanvas()
        canvas.drawColor(Color.DKGRAY)
        canvas.drawCircle(
            this.ballObj1X,
            this.ballObj1Y,
            this.ballObj1radius,
            Paint().apply {
             this.color = Color.BLACK
         }
        )
        canvas.drawRect(
            this.squareObj1XL,
            this.squareObj1YT,
            this.squareObj1XR,
            this.squareObj1YB,
            Paint().apply {
                this.color = Color.BLACK
            }
        )
        canvas.drawRect(
            this.squareObj2XL,
            this.squareObj2YT,
            this.squareObj2XR,
            this.squareObj2YB,
            Paint().apply {
                this.color = Color.BLACK
            }
        )

        canvas.drawCircle(
            this.ballGoalX,
            this.ballGoalY,
            this.ballGoalRadius,
            Paint().apply {
                this.color = Color.BLUE
            }

        )
        canvas.drawCircle(
            this.ballX,
            this.ballY,
            this.radius,
            Paint().apply {
                this.color = Color.RED
            }

        )


        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
    private fun judgeCircle(posX:Float,posY:Float,length:Float){

        if (posX-length < (this.ballX-radius) && (this.ballX-radius) < posX+length){
            if(posY -length <(this.ballY-radius) && (this.ballY-radius) < posY+length){
                imageView.setImageResource(R.drawable.honda_janken)
                judgeText.setText("残念…")
                this.flag = true
            }
        }
        else if (posX-length < (this.ballX+radius) && (this.ballX+radius) < posX+length){
            if(posY -length <(this.ballY+radius) && (this.ballY+radius) < posY+length){
                imageView.setImageResource(R.drawable.honda_janken)
                judgeText.setText("残念…")
                this.flag = true
            }
        }
    }
    private fun judgeRect(posXL:Float,posYT:Float,posXR:Float,posYB:Float){

        if (posXL < (this.ballX-radius) && (this.ballX-radius) < posXR){
            if(posYT <(this.ballY-radius) && (this.ballY-radius) < posYB){
                imageView.setImageResource(R.drawable.honda_janken)
                judgeText.setText("残念…")
                this.flag = true
            }
        }
        else if (posXL < (this.ballX+radius) && (this.ballX+radius) < posXR){
            if(posYT <(this.ballY+radius) && (this.ballY+radius) < posYB){
                imageView.setImageResource(R.drawable.honda_janken)
                judgeText.setText("残念…")
                this.flag = true
            }
        }
    }
    private fun judgeGoal(posX:Float,posY:Float,length:Float){

        if (posX-length < (this.ballX-radius) && (this.ballX-radius) < posX+length){
            if(posY -length <(this.ballY-radius) && (this.ballY-radius) < posY+length){
                imageView.setImageResource(R.drawable.kachi_1)
                judgeText.setText("おめでとう！")
                this.flag2 = true
            }
        }
        else if (posX-length < (this.ballX+radius) && (this.ballX+radius) < posX+length){
            if(posY -length <(this.ballY+radius) && (this.ballY+radius) < posY+length){
                imageView.setImageResource(R.drawable.kachi_1)
                judgeText.setText("おめでとう！")
                this.flag2 = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val holder = surfaceView.holder
        holder.addCallback(this)


    }

    override fun onResume() {
        super.onResume()
        button.setOnClickListener {

            ballX = 100f
            ballY = 100f

            vx = 0f
            vy = 0f
            time = 0L

            imageView.setImageResource(R.drawable.make_3)
            judgeText.setText("がんばれ！")
            this.flag = false
            this.flag2 = false


        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null){
            return ;
        }

        if(time== 0L){
            time=System.currentTimeMillis()
        }
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val x = event.values[0]*-1
            val y = event.values[1]

            var t = ((System.currentTimeMillis() - time)).toFloat()
            time = System.currentTimeMillis()

            t /= 1000.0f

            val dx = (vx*t) + (x*t*t)/2.0f
            val dy = (vy*t) + (y*t*t)/2.0f

            this.ballX += (dx*coef)
            this.ballY += (dy*coef)

            this.vx +=(x*t)
            this.vy +=(y*t)


            if ((this.ballX-radius)<0 && vx<0){
                vx = -vx / 1.5f
                this.ballX = this.radius

            } else if((this.ballX+radius)>this.surfaceWidth && vx>0){
                vx = -vx/1.5f
                this.ballX = this.surfaceWidth-this.radius
            }
            if((this.ballY - radius)<0 && vy<0){
                vy = -vy/1.5f
                this.ballY = this.radius
            }
            else if((this.ballY+radius)>this.surfaceHeight && vy>0){
                vy = -vy/1.5f
                ballY = surfaceHeight -radius
            }
            if(flag2 == false) {
                judgeCircle(
                    this.ballObj1X,
                    this.ballObj1Y,
                    this.ballObj1radius
                )

                judgeRect(
                    this.squareObj1XL,
                    this.squareObj1YT,
                    this.squareObj1XR,
                    this.squareObj1YB
                )
                judgeRect(
                    this.squareObj2XL,
                    this.squareObj2YT,
                    this.squareObj2XR,
                    this.squareObj2YB
                )
            }
            if(flag == false) {
                judgeGoal(
                    this.ballGoalX,
                    this.ballGoalY,
                    this.ballGoalRadius
                )
                this.drawCanvas()
            }




        }
//        d("TAG01","センサーの値が変わりました")
//        if(event == null){
//            return ;
//        }
//        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
//            val str:String = "x = ${event.values[0].toString()}" +
//                   ", y = ${event.values[1].toString()}" +
//                    ", z = ${event.values[2].toString()}";
//            // デバッグログに出力
//            d("加速度センサー", str);
//
    }

    override fun onPause() {
        super.onPause()
    }

}


