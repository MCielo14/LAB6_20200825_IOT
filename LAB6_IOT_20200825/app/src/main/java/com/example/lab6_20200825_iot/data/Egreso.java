package com.example.lab6_20200825_iot.data;


public class Egreso {
    private String id;
    private String titulo;
    private String descripcion;
    private double monto;
    private String fecha;
    private String hora;
    private String userId;

    // Constructor vacío necesario para Firestore
    public Egreso() {
    }

    public Egreso(String id, String titulo, String descripcion, double monto, String fecha, String hora, String userId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.hora = hora;
        this.userId = userId;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

