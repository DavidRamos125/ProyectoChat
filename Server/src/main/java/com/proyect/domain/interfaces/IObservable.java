package com.proyect.domain.interfaces;


public interface IObservable {

    public void notify(String data);
    public void add(IObserver o);
}
