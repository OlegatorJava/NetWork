package com.example.network.lesson;

public class Cat extends Animal{
    private String name;

    public Cat(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cat{" + "name='" + name + '\'' + '}';
    }
}
