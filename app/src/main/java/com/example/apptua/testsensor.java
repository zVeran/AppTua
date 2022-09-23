package com.example.apptua;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;

public class testsensor extends AppCompatActivity {

    private lateinit var bussola: Sensor;
    private  lateinit var acelerometro: Sensor;
    private lateinit var sensorManager: SensorManager;
    private lateinit var listener: SensorEventListener;

        private var ultimoGrau = 0f;
        private var vlrsBussola = FloatArray(3);
        private var vlrsGravidade =  FloatArray(3);
        private var angulosDeOrientacao = FloatArray(3);
        private var matrixDeRotacao = FloatArray(9);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testsensor);

        var imageView: ImageView = findViewById(R.id.imageView)

        /**Listando sensores**/

        sensorManager = getSystemService(Context.SENSOR.SERVICE) as SensorManager
        val sensores: list<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
         sensores.forEach {sensor ->
            Log.i("SENSORES", sensor.ToString())

    }

    /** Pegando um sensor especifico*/
    acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    bussola = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            if(bussola !=null ){
                Log.i("SENSORES","O DISPOSITIVO POSSUI BÚSSOLA!")
            }
            else(bussola !=null ){
            Log.i("SENSORES","O DISPOSITIVO NÃO POSSUI BÚSSOLA!")

            /** Criando SensorEventListener*/

            listener = object: SensorEventListener{
                override fun onSensorChanged(event: SensorEvent?){
                    when(event?.sensor?.type){
                        Sensor.TYPE_ACCELEROMETER ->{
                        vlrsGravidade = event.Values.clone()
                        var x = event.values[0]
                        var x = event.values[1]
                        var x = event.values[2]
                        Log.i("SENSORES", "Sensor,TYPE_ACCELEROMETER -> x = $x, y = $y, z = $z")
                        }
                        Sensor.TYPE_MAGNETIC_FIELD ->{
                            vlrsBussola = event.values.clone()
                            Log.i("SENSORES", "Sensor,TYPE_MAGNETIC_FIELD")
                        }
                      }
                      SensorManager.getRotatioMatrix(matrixDeRotacao, null, vlrsGravidade, vlrsBussola)
                      SensorManager.getOrientation(matrixDeRotacao, angulosDeOrientacao)

                              val radiano: Float = angulosDeOrientacao[0]
                            val grauAtual = (Math.toDegrees(radiano.toDouble())+360).toFloat()% 360

                                    var rotacionar = RotateAnimation(
                                            ultimoGrau, -grauAtual,
                                            Animation.RELATIVE_TO_SELF, 0.5f,
                                            Animation.RELATIVE_TO_SELF, 0.5f,
                                            )
                                    rotacionar.duration = 250
                                    rotacionar.fillAfter = true

                                   imageView.startAnimation(rotacionar)
                                   ultimograu = -grauAtual
                   }
                   override fun onAccurancyChanged(sensor: sensor?, accurancy: Int){

                    }
                }
            }
        }

        override fun onResume(){
        super.onResume();
        sensorManager.registerListener(listener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, bussola, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onPause (){}
    super.onPause()
        sensorManager.unRegisterListener(listener)
}