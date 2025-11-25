package com.proyectofinal.interfaces;


public interface IObservable {
    public void notify(String type, String data);
    public void add(IObserver o);
}
