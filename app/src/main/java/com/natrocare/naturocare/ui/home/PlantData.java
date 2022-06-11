package com.natrocare.naturocare.ui.home;

public class PlantData {
    private int plantImage;
    private String plantName,plantTime;

    public PlantData(int plantImage,String plantName,String plantTime){
        this.plantImage=plantImage;
        this.plantName = plantName;
        this.plantTime = plantTime;
    }

    public void setPlantImage(int plantImage) {
        this.plantImage = plantImage;
    }

    public int getPlantImage() {
        return plantImage;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantTime(String plantTime) {
        this.plantTime = plantTime;
    }

    public String getPlantTime() {
        return plantTime;
    }
}
