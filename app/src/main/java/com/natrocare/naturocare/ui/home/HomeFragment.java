package com.natrocare.naturocare.ui.home;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.natrocare.naturocare.R;
import com.natrocare.naturocare.SharedPrefs;


import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    public static String TAG = "MQTT";
    private String username = "FinalYearProj8thSem";
    private String password = "FinalYearProj8thSem";
    private int port = 8884;
    private String host = "cbab12abf4314ac59f44927cb4892572.s1.eu.hivemq.cloud";
    private String serverURL1 = "tcp://broker.hivemq.com:1883";
    private String serverURL = "tcp://"+host+":"+port;

    TextView motorStatus, motorValue, tempValue, sunset, timerValue, deviceStatus;
    Switch motorSwitch;
    LineChart graph;
    CircleImageView userPic;
    ImageButton recommendation;
    //MqttConnectOptions options = new MqttConnectOptions();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.home_fragment, container, false);
        userPic = root.findViewById(R.id.HomeUserPic);
        motorStatus = root.findViewById(R.id.MotorText);
        motorValue = root.findViewById(R.id.MotorValue);
        motorSwitch = root.findViewById(R.id.MotorSwitch);
        graph = root.findViewById(R.id.MoistureLineChart);
        tempValue = root.findViewById(R.id.TemperatureValue);
        sunset = root.findViewById(R.id.HomeSunsetTime);
        timerValue = root.findViewById(R.id.TimerValue);
        deviceStatus = root.findViewById(R.id.DeviceStatus);
        recommendation = root.findViewById(R.id.RecommendationButton);

        String mStatus = SharedPrefs.readSharedSetting(root.getContext(),"motorSwitch","Off");
        motorSwitch.setChecked(mStatus == "On");
        motorStatus.setText(mStatus);

        Mqtt3AsyncClient client = MqttClient.builder()
                .useMqttVersion3()
                .serverHost(host)
                .serverPort(port)
                .sslWithDefaultConfig()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(password.getBytes())
                .applySimpleAuth()
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to publish
                        throwable.printStackTrace();
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                    }
                });


        motorSwitch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(!motorSwitch.isChecked()){
                    motorSwitch.setChecked(true);
                    SharedPrefs.saveSharedSetting(root.getContext(),"motorSwitch","On");
                    motorStatus.setText("On");
                    motorValue.setText(String.format(Locale.getDefault(),"- %02d",80));
                    String topic = "nancy/motor";
                    String payload = "start";
                    client.publishWith()
                            .topic(topic)
                            .payload(payload.getBytes())
                            .qos(MqttQos.EXACTLY_ONCE)
                            .send()
                            .whenComplete((mqtt3Publish, throwable) -> {
                                if (throwable != null) {
                                    // Handle failure to publish
                                    throwable.printStackTrace();
                                } else {
                                    // Handle successful publish, e.g. logging or incrementing a metric
                                }
                            });

                }else{
                    motorSwitch.setChecked(false);
                    SharedPrefs.saveSharedSetting(root.getContext(),"motorSwitch","Off");
                    motorStatus.setText("Off");
                    motorValue.setText(String.format(Locale.getDefault(),"- %02d",0));
                    String topic = "nancy/motor";
                    String payload = "start";
                    client.publishWith()
                            .topic(topic)
                            .payload(payload.getBytes())
                            .qos(MqttQos.EXACTLY_ONCE)
                            .send()
                            .whenComplete((mqtt3Publish, throwable) -> {
                                if (throwable != null) {
                                    // Handle failure to publish
                                    throwable.printStackTrace();
                                } else {
                                    // Handle successful publish, e.g. logging or incrementing a metric
                                }
                            });
                }
            }
        });

        String onTimeTopic = "nancy/ontime";
        String moistValTopic = "nancy/moistval";
        client.subscribeWith()
                .topicFilter(onTimeTopic)
                .callback(publish -> {
                    // Process the received message


                })
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to publish
                        throwable.printStackTrace();
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                    }
                });

        client.subscribeWith()
                .topicFilter(moistValTopic)
                .callback(publish -> {
                    // Process the received message
                })
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to publish
                        throwable.printStackTrace();
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                    }
                });


        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }


}