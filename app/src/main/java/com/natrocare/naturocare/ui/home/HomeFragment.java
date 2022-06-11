package com.natrocare.naturocare.ui.home;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.data.LineData;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.natrocare.naturocare.R;
import com.natrocare.naturocare.SharedPrefs;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private String username = "FinalYearProj8thSem";
    private String password = "FinalYearProj8thSem";
    private int port = 8883;
    private String host = "cbab12abf4314ac59f44927cb4892572.s1.eu.hivemq.cloud";

    TextView motorStatus, motorValue, tempValue, sunset, timerValue, deviceStatus;
    Switch motorSwitch;
    AnyChartView graph;
    CircleImageView userPic;
    ImageButton recommendation;
    ViewGroup viewGroup;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.home_fragment, container, false);
        userPic = root.findViewById(R.id.HomeUserPic);
        motorStatus = root.findViewById(R.id.MotorText);
        motorValue = root.findViewById(R.id.MotorValue);
        motorSwitch = root.findViewById(R.id.MotorSwitch);
        graph = root.findViewById(R.id.MoistureLineChart);
        tempValue = root.findViewById(R.id.MoistureValue);
        sunset = root.findViewById(R.id.HomeSunsetTime);
        timerValue = root.findViewById(R.id.TimerValue);
        deviceStatus = root.findViewById(R.id.DeviceStatus);
        recommendation = root.findViewById(R.id.RecommendationButton);
        viewGroup = root.findViewById(R.id.content);

        return root;
    }

    @Override
    public void onStart() {

        String mStatus = SharedPrefs.readSharedSetting(getActivity(),"motorSwitch","Off");
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
                        deviceStatus.setText("Not Connected");
                        deviceStatus.setTextColor(Color.RED);
                        throwable.printStackTrace();
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                        deviceStatus.setText("Connected");
                        deviceStatus.setTextColor(Color.GREEN);
                    }
                });


        motorSwitch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(!motorSwitch.isChecked()){
                    motorSwitch.setChecked(true);
                    SharedPrefs.saveSharedSetting(getActivity(),"motorSwitch","On");
                    motorStatus.setText("On");
                    motorValue.setText("- 80%");
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
                    SharedPrefs.saveSharedSetting(getActivity(),"motorSwitch","Off");
                    motorStatus.setText("Off");
                    motorValue.setText("- 00%");
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
//                    try {
//                        String moisture = new String(publish.getPayloadAsBytes());
//                        tempValue.setText(Integer.getInteger(moisture));
//                    }catch (Exception e){
//
//                    }

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

        LineChart();

        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = View.inflate(getContext(),R.layout.recommendation_activity,viewGroup);

                ArrayList<PlantData> plantDataArrayList = loadPlantData();
                RecyclerView recyclerView = dialogView.findViewById(R.id.PlantRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(new PlantRecyclerViewAdapter(getContext(),plantDataArrayList));

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        super.onStart();
    }

    private void LineChart() {
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(0d, 20d, 5d, 20d);
        cartesian.yGrid(true);
        cartesian.xGrid(true);

        List<DataEntry> seriesData = new ArrayList<>();

        seriesData.add(new CustomDataEntry("0", 30));
        seriesData.add(new CustomDataEntry("5", 30));
        seriesData.add(new CustomDataEntry("10", 30));
        seriesData.add(new CustomDataEntry("15", 30));
        seriesData.add(new CustomDataEntry("20", 30));
        seriesData.add(new CustomDataEntry("25", 31));
        seriesData.add(new CustomDataEntry("30", 31));
        seriesData.add(new CustomDataEntry("35", 31));
        seriesData.add(new CustomDataEntry("40", 35));
        seriesData.add(new CustomDataEntry("45", 42));
        seriesData.add(new CustomDataEntry("50", 42));
        seriesData.add(new CustomDataEntry("55", 42));
        seriesData.add(new CustomDataEntry("60", 42));
        seriesData.add(new CustomDataEntry("65", 42));
        seriesData.add(new CustomDataEntry("70", 42));
        seriesData.add(new CustomDataEntry("75", 49));
        seriesData.add(new CustomDataEntry("80", 49));
        seriesData.add(new CustomDataEntry("85", 49));
        seriesData.add(new CustomDataEntry("90", 49));
        seriesData.add(new CustomDataEntry("95", 49));
        seriesData.add(new CustomDataEntry("100", 49));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Line line = cartesian.line(series1Mapping);
        line.name("Moisture Level");
        line.stroke("#3863A4");
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        cartesian.background().fill("#D6FFF8");
        graph.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }

    private ArrayList<PlantData> loadPlantData() {
        ArrayList<PlantData> plantDataArrayList = new ArrayList<>();
        PlantData
                plantData = new PlantData(R.drawable.hibicus_plant,"Hibiscus","07:30");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.areca_plant,"Areca Palm","10:00");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.monstera_plant,"Monstera","08:30");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.money_plant,"Money Plant","16:20");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.snake_plant,"Snake Plant","08:40");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.ficus_plant,"Ficus","07:10");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.pothus_plant,"Pothos","06:50");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.dracaena_plant,"Dracaena","08:30");
                plantDataArrayList.add(plantData);
                plantData = new PlantData(R.drawable.parlor_palm,"Parlor Palm","10:30");
                plantDataArrayList.add(plantData);
        return plantDataArrayList;
    }
}