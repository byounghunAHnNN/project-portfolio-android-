package com.example.happydog;

public class PetVo {
    private Integer id;
    private String dog_name;
    private String breed;
    private String size;
    private String weight;
    private String etc;
    private String dog_image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDog_name() {
        return dog_name;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getDog_image() {
        return dog_image;
    }

    public void setDog_image(String dog_image) {
        this.dog_image = dog_image;
    }

    @Override
    public String toString() {
        return "PetVo{" +
                "id=" + id +
                ", dog_name='" + dog_name + '\'' +
                ", breed='" + breed + '\'' +
                ", size='" + size + '\'' +
                ", weight='" + weight + '\'' +
                ", etc='" + etc + '\'' +
                ", dog_image='" + dog_image + '\'' +
                '}';
    }
}
